package csc478.group2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class JavaFX extends Application {

    private static final int CELL_SIZE = 40;

    private GameController controller;

    private GridPane boardGrid;
    private StackPane[][] boardCells;
    private HBox rackBox;

    private Label scoreLabel;
    private Label timerLabel;
    private Label statusLabel;
    private Label messageLabel;

    private Timeline timeline;

    // UI-only pending placements for this turn
    private final List<PlacedTile> pendingTiles = new ArrayList<>();
    private Tile selectedTileUI;

    @Override
    public void start(Stage primaryStage) {
        controller = new GameController();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.CENTER);

        timerLabel = new Label();
        timerLabel.setFont(Font.font(16));

        Label titleLabel = new Label("Scrabble");
        titleLabel.setFont(Font.font(24));

        scoreLabel = new Label();
        scoreLabel.setFont(Font.font(16));

        statusLabel = new Label();
        statusLabel.setFont(Font.font(16));

        topBox.getChildren().addAll(timerLabel, titleLabel, scoreLabel, statusLabel);

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardCells = new StackPane[Board.SIZE][Board.SIZE];
        initializeBoardUI();

        rackBox = new HBox(10);
        rackBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit Turn");
        Button cancelButton = new Button("Cancel Turn");

        submitButton.setOnAction(e -> submitTurn());
        cancelButton.setOnAction(e -> cancelTurn());

        HBox buttonBox = new HBox(20, submitButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        messageLabel = new Label();
        messageLabel.setFont(Font.font(14));

        root.getChildren().addAll(topBox, boardGrid, rackBox, buttonBox, messageLabel);

        refreshUI();
        startTimer();

        Scene scene = new Scene(root, 950, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scrabble");
        primaryStage.show();
    }

    private void initializeBoardUI() {
        boardGrid.getChildren().clear();

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                StackPane cell = createBoardCell(row, col);
                boardCells[row][col] = cell;
                boardGrid.add(cell, col, row);
            }
        }
    }

    private StackPane createBoardCell(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setStyle(getBaseCellStyle(row, col));

        cell.setOnDragOver(event -> {
            if (selectedTileUI != null &&
                controller.getGameState().getGameStatus() == GameState.GameStatus.RUNNING) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            boolean success = false;

            if (selectedTileUI != null
                    && controller.getGameState().getBoard().isValidPosition(row, col)
                    && controller.getGameState().getBoard().isEmptyAt(row, col)
                    && !isPendingAt(row, col)
                    && controller.getGameState().getGameStatus() == GameState.GameStatus.RUNNING) {

                pendingTiles.add(new PlacedTile(row, col, selectedTileUI));
                selectedTileUI = null;
                success = true;
                messageLabel.setText("");
                refreshUI();
            }

            event.setDropCompleted(success);
            event.consume();
        });

        return cell;
    }

    private void refreshUI() {
        GameState state = controller.getGameState();

        scoreLabel.setText("Score: " + state.getCurrentScore());
        timerLabel.setText(formatTime(state.getRemainingTime()));
        statusLabel.setText("Status: " + state.getGameStatus());

        updateBoardUI();
        updateRackUI();
    }

    private void updateBoardUI() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                StackPane cell = boardCells[row][col];
                cell.getChildren().clear();
                cell.setStyle(getBaseCellStyle(row, col));

                char committedLetter = controller.getGameState().getBoard().getCell(row, col);
                Tile pendingTile = getPendingTileAt(row, col);

                if (committedLetter != Board.EMPTY_CELL) {
                    Label tileLabel = new Label(String.valueOf(committedLetter));
                    tileLabel.setFont(Font.font(16));
                    cell.getChildren().add(tileLabel);
                    cell.setStyle("-fx-border-color: black; -fx-background-color: wheat;");
                } else if (pendingTile != null) {
                    Label tileLabel = new Label(String.valueOf(pendingTile.getLetter()));
                    tileLabel.setFont(Font.font(16));
                    cell.getChildren().add(tileLabel);
                    cell.setStyle("-fx-border-color: black; -fx-background-color: khaki;");
                } else {
                    String squareText = getSquareText(row, col);
                    if (!squareText.isEmpty()) {
                    	Label bonusLabel = new Label(squareText);
                    	bonusLabel.setFont(Font.font(10));

                    	if (squareText.equals("TW") || squareText.equals("DW")) {
                    	    bonusLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                    	} else if (squareText.equals("TL") || squareText.equals("DL")) {
                    	    bonusLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    	} else if (squareText.equals("★")) {
                    	    bonusLabel.setStyle("-fx-text-fill: black; -fx-font-size: 16;");
                    	} else {
                    	    bonusLabel.setStyle("-fx-text-fill: black;");
                    	}

                    	cell.getChildren().add(bonusLabel);
                    }
                }
            }
        }
    }

    private void updateRackUI() {
        rackBox.getChildren().clear();

        List<Tile> rackTiles = new ArrayList<>(controller.getRackTiles());

        // remove tiles already pending this turn from displayed rack
        for (PlacedTile pending : pendingTiles) {
            rackTiles.remove(pending.getTile());
        }

        for (Tile tile : rackTiles) {
            Label tileLabel = new Label(String.valueOf(tile.getLetter()));
            tileLabel.setFont(Font.font(16));
            tileLabel.setStyle("-fx-border-color: black; -fx-background-color: wheat; -fx-padding: 8px;");

            tileLabel.setOnDragDetected(event -> {
                if (controller.getGameState().getGameStatus() != GameState.GameStatus.RUNNING) {
                    return;
                }

                Dragboard db = tileLabel.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(tile.getLetter()));
                db.setContent(content);

                selectedTileUI = tile;
                event.consume();
            });

            rackBox.getChildren().add(tileLabel);
        }
    }

    private void submitTurn() {
        if (pendingTiles.isEmpty()) {
            messageLabel.setText("No tiles placed.");
            return;
        }

        boolean success = controller.submitTurn(new ArrayList<>(pendingTiles));

        if (success) {
            pendingTiles.clear();
            selectedTileUI = null;
            messageLabel.setText("Move accepted.");
        } else {
            messageLabel.setText("Invalid move.");
        }

        refreshUI();
    }

    private void cancelTurn() {
        pendingTiles.clear();
        selectedTileUI = null;
        messageLabel.setText("Turn canceled.");
        refreshUI();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            controller.tickClock();
            refreshUI();

            if (controller.getGameState().getGameStatus() != GameState.GameStatus.RUNNING) {
                timeline.stop();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private boolean isPendingAt(int row, int col) {
        return getPendingTileAt(row, col) != null;
    }

    private Tile getPendingTileAt(int row, int col) {
        for (PlacedTile placed : pendingTiles) {
            if (placed.getRow() == row && placed.getCol() == col) {
                return placed.getTile();
            }
        }
        return null;
    }

    private String formatTime(int seconds) {
        int safeSeconds = Math.max(0, seconds);
        int min = safeSeconds / 60;
        int sec = safeSeconds % 60;
        return String.format("Time: %02d:%02d", min, sec);
    }

    private String getBaseCellStyle(int row, int col) {
        Square square = controller.getGameState().getBoard().getSquare(row, col);

        if (!square.isBonusUsed()) {
            switch (square.getBonusType()) {
                case DOUBLE_WORD:
                    return "-fx-border-color: black; -fx-background-color: pink;";
                case TRIPLE_WORD:
                    return "-fx-border-color: black; -fx-background-color: red;";
                case DOUBLE_LETTER:
                    return "-fx-border-color: black; -fx-background-color: lightblue;";
                case TRIPLE_LETTER:
                    return "-fx-border-color: black; -fx-background-color: deepskyblue;";
                case TIME:
                    return "-fx-border-color: black; -fx-background-color: lightgreen;";
                default:
                    break;
            }
        }

        if (row == 7 && col == 7 && controller.getGameState().getBoard().isEmptyAt(row, col)) {
            return "-fx-border-color: black; -fx-background-color: gold;";
        }

        return "-fx-border-color: black; -fx-background-color: beige;";
    }

    private String getSquareText(int row, int col) {
        Square square = controller.getGameState().getBoard().getSquare(row, col);

        if (!square.isBonusUsed()) {
            switch (square.getBonusType()) {
                case DOUBLE_WORD:
                    return "DW";
                case TRIPLE_WORD:
                    return "TW";
                case DOUBLE_LETTER:
                    return "DL";
                case TRIPLE_LETTER:
                    return "TL";
                case TIME:
                    return "T";
                default:
                    break;
            }
        }

        if (row == 7 && col == 7 && controller.getGameState().getBoard().isEmptyAt(row, col)) {
            return "★";
        }

        return "";
    }
}