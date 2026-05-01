package csc478.group2;

// 1.3.9 UI Layer Requirements
// Class Description: Handles all user interaction and rendering for the Scrabble game.
// This class displays game data and collects input through drag-and-drop, but does not directly commit changes to GameState.
// Note: The process for creating this class involved significant leveraging of LLMs for formatting assistance. 

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
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
	private Stage primaryStage;

	private GridPane boardGrid;
	private StackPane[][] boardCells;
	private HBox rackBox;

	private Label scoreLabel;
	private Label timerLabel;
	private Label messageLabel;

	private Timeline timeline;

	// Stores tiles placed during the current turn before they are submitted.
	private final List<PlacedTile> pendingTiles = new ArrayList<>();
	private Tile selectedTileUI;

	// Starts the JavaFX application and displays the start screen.
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		showStartScreen();
	}

	// Displays the start menu before the game begins.
	private void showStartScreen() {
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(30));

		Label title = new Label("Scrabble");
		title.setFont(Font.font(36));

		Button startButton = new Button("Start Game");
		startButton.setFont(Font.font(18));

		startButton.setOnAction(e -> startGame());

		root.getChildren().addAll(title, startButton);

		primaryStage.setScene(new Scene(root, 950, 800));
		primaryStage.setTitle("Scrabble");
		primaryStage.show();
	}

	// Initializes a new game and builds the main gameplay screen.
	private void startGame() {
		controller = new GameController();
		pendingTiles.clear();
		selectedTileUI = null;

		VBox root = new VBox(10);
		root.setPadding(new Insets(10));
		root.setAlignment(Pos.CENTER);

		// Header displays the title, score, and timer.
		VBox header = new VBox(5);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(10));
		header.setStyle("-fx-background-color: linear-gradient(to right, #4b0082, #8a2be2);");

		Label titleLabel = new Label("SCRABBLE");
		titleLabel.setFont(Font.font("Arial", 32));
		titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

		HBox infoRow = new HBox(40);
		infoRow.setAlignment(Pos.CENTER);

		// 1.3.9.1 The JavaFX UI shall display currentScore using scoreLabel in
		// refreshUI.
		scoreLabel = new Label();
		scoreLabel.setFont(Font.font("Arial", 18));
		scoreLabel.setStyle("-fx-text-fill: white;");

		// 1.3.9.2 The JavaFX UI shall display remainingTime using timerLabel in
		// refreshUI.
		timerLabel = new Label();
		timerLabel.setFont(Font.font("Arial", 18));
		timerLabel.setStyle("-fx-text-fill: white;");

		infoRow.getChildren().addAll(scoreLabel, timerLabel);
		header.getChildren().addAll(titleLabel, infoRow);

		// 1.3.9.0 The JavaFX UI shall render the Board using updateBoardUI.
		boardGrid = new GridPane();
		boardGrid.setAlignment(Pos.CENTER);
		boardCells = new StackPane[Board.SIZE][Board.SIZE];
		initializeBoardUI();

		// Rack displays available tiles and supports drag input.
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

		root.getChildren().addAll(header, boardGrid, rackBox, buttonBox, messageLabel);

		primaryStage.setScene(new Scene(root, 950, 800));
		primaryStage.setTitle("Scrabble");

		refreshUI();
		startTimer();
	}

	// 1.3.9.3 The JavaFX UI shall display game results on the end screen using
	// showGameOverScreen.
	// Displays the final win/loss result and final score.
	private void showGameOverScreen() {
		if (timeline != null) {
			timeline.stop();
		}

		int finalScore = controller.getGameState().getCurrentScore();

		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(30));

		GameState.GameStatus status = controller.getGameState().getGameStatus();

		String resultText = status == GameState.GameStatus.WON ? "You Won!" : "You Lost!";

		Label gameOverLabel = new Label(resultText);
		gameOverLabel.setFont(Font.font(36));

		Label finalScoreLabel = new Label("Final Score: " + finalScore);
		finalScoreLabel.setFont(Font.font(24));

		Button restartButton = new Button("Start New Game");
		restartButton.setFont(Font.font(18));
		restartButton.setOnAction(e -> startGame());

		Button quitButton = new Button("Quit");
		quitButton.setFont(Font.font(18));
		quitButton.setOnAction(e -> primaryStage.close());

		HBox buttons = new HBox(20, restartButton, quitButton);
		buttons.setAlignment(Pos.CENTER);

		root.getChildren().addAll(gameOverLabel, finalScoreLabel, buttons);

		primaryStage.setScene(new Scene(root, 950, 800));
	}

	// Builds the board UI grid.
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

	// 1.3.9.5 The JavaFX UI shall collect placement input by dropping tiles onto
	// board squares.
	// Creates a board cell and handles drag/drop placement input.
	private StackPane createBoardCell(int row, int col) {
		StackPane cell = new StackPane();
		cell.setPrefSize(CELL_SIZE, CELL_SIZE);
		cell.setStyle(getBaseCellStyle(row, col));

		cell.setOnDragOver(event -> {
			if (selectedTileUI != null && controller.getGameState().getGameStatus() == GameState.GameStatus.RUNNING) {
				event.acceptTransferModes(TransferMode.MOVE);
			}
			event.consume();
		});

		cell.setOnDragDropped(event -> {
			boolean success = false;

			if (selectedTileUI != null && controller.getGameState().getBoard().isValidPosition(row, col)
					&& controller.getGameState().getBoard().isEmptyAt(row, col) && !isPendingAt(row, col)
					&& controller.getGameState().getGameStatus() == GameState.GameStatus.RUNNING) {

				// 1.3.9.6 The JavaFX UI shall store unsubmitted placements locally in
				// pendingTiles until Submit Turn is pressed with blank tile handling.
				Tile tileToPlace = selectedTileUI;

				if (tileToPlace.isBlank()) {
					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Blank Tile");
					dialog.setHeaderText("Choose a letter for the blank tile");
					dialog.setContentText("Letter:");

					String input = dialog.showAndWait().orElse("");

					if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
						event.setDropCompleted(false);
						event.consume();
						return;
					}

					tileToPlace = tileToPlace.asBlankLetter(input.charAt(0));
				}

				pendingTiles.add(new PlacedTile(row, col, tileToPlace));

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

	// Refreshes score, timer, board, and rack display from the controller/state.
	private void refreshUI() {
		GameState state = controller.getGameState();

		scoreLabel.setText("Score: " + state.getCurrentScore());
		timerLabel.setText(formatTime(state.getRemainingTime()));

		updateBoardUI();
		updateRackUI();
	}

	// 1.3.9.0 The JavaFX UI shall render the Board using updateBoardUI.
	// Updates board visuals for committed tiles, pending tiles, and bonus squares.
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

	// 1.3.9.4 The JavaFX UI shall collect tile input through drag-and-drop from the
	// rack.
	// Displays rack tiles and allows the player to drag tiles onto the board.
	private void updateRackUI() {
		rackBox.getChildren().clear();

		List<Tile> rackTiles = new ArrayList<>(controller.getRackTiles());

		// Hide tiles that are already pending on the board this turn.
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

	// 1.3.9.7 The JavaFX UI shall submit pending placements to GameController using
	// submitTurn.
	// Sends pending tiles to GameController for validation, scoring, and state
	// update.
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

		if (controller.getGameState().getGameStatus() != GameState.GameStatus.RUNNING) {
			showGameOverScreen();
		}
	}

	// 1.3.9.8 The JavaFX UI shall cancel unsubmitted placements using cancelTurn
	// without changing the committed Board.
	// Clears pending placements and restores the UI to the last committed board
	// state.
	private void cancelTurn() {
		pendingTiles.clear();
		selectedTileUI = null;
		messageLabel.setText("Turn canceled.");
		refreshUI();
	}

	// Runs the UI timer and delegates time updates to GameController.
	private void startTimer() {
		if (timeline != null) {
			timeline.stop();
		}

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			controller.tickClock();
			refreshUI();

			if (controller.getGameState().getGameStatus() == GameState.GameStatus.LOST) {
				showGameOverScreen();
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	// Checks whether a board location already contains a pending tile.
	private boolean isPendingAt(int row, int col) {
		return getPendingTileAt(row, col) != null;
	}

	// Returns the pending tile at a board location, if one exists.
	private Tile getPendingTileAt(int row, int col) {
		for (PlacedTile placed : pendingTiles) {
			if (placed.getRow() == row && placed.getCol() == col) {
				return placed.getTile();
			}
		}
		return null;
	}

	// Formats remaining time as MM:SS.
	private String formatTime(int seconds) {
		int safeSeconds = Math.max(0, seconds);
		int min = safeSeconds / 60;
		int sec = safeSeconds % 60;
		return String.format("Time: %02d:%02d", min, sec);
	}

	// Returns the background style for a board square based on its bonus type.
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

	// Returns the label text displayed on bonus squares.
	private String getSquareText(int row, int col) {
		Square square = controller.getGameState().getBoard().getSquare(row, col);

		if (!square.isBonusUsed()) {
			switch (square.getBonusType()) {
			case DOUBLE_WORD:
				return row == 7 && col == 7 ? "★" : "DW";
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

		return "";
	}
}