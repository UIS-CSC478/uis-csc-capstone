package csc478.group2;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private final GameState state;

    // ✔ moved here (NOT in GameState)
    private final Rack rack;
    private final TileBag tileBag;

    private final RulesEngine rulesEngine;
    private final ScoreCalculator scoreCalculator;
    private final TimeManager timeManager;
    private final EndConditionEvaluator endEvaluator;
    private final WordValidation wordValidation;

    public GameController() {
        this.state = new GameState();

        this.rack = new Rack();
        this.tileBag = new TileBag();

        this.rulesEngine = new RulesEngine();
        this.scoreCalculator = new ScoreCalculator();
        this.timeManager = new TimeManager();
        this.endEvaluator = new EndConditionEvaluator();
        this.wordValidation = new WordValidation();

        rack.refill(tileBag); // initial rack
    }

    public GameState getGameState() {
        return state;
    }

    // =========================
    // RACK ACCESS (for UI)
    // =========================

    public List<Tile> getRackTiles() {
        return new ArrayList<>(rack.getTiles());
    }

    public void returnTilesToRack(List<PlacedTile> tiles) {
        for (PlacedTile t : tiles) {
            rack.addTile(t.getTile());
        }
    }

    // =========================
    // TURN SUBMISSION
    // =========================

    public boolean submitTurn(List<PlacedTile> placedTiles) {

        if (state.getGameStatus() != GameState.GameStatus.RUNNING) {
            return false;
        }

        if (placedTiles == null || placedTiles.isEmpty()) {
            return false;
        }

        // validate BEFORE modifying state
        if (!rulesEngine.validateMove(state, placedTiles, wordValidation)) {
            return false;
        }

        applyPlacedTilesToBoard(placedTiles);

        int score = scoreCalculator.calculateTurnScore(state, placedTiles);
        state.setCurrentScore(state.getCurrentScore() + score);

        applyTimeBonuses(placedTiles);

        rack.refill(tileBag); // refill AFTER move

        timeManager.tickAfterTurn(state);
        state.setGameStatus(endEvaluator.evaluate(state));

        return true;
    }

    // =========================
    // INTERNAL HELPERS
    // =========================

    private void applyPlacedTilesToBoard(List<PlacedTile> placedTiles) {
        Board board = state.getBoard();

        for (PlacedTile placed : placedTiles) {
            board.placeTile(placed.getRow(), placed.getCol(), placed.getTile());
            rack.removeTile(placed.getTile()); // ✔ FIXED
        }
    }

    private void applyTimeBonuses(List<PlacedTile> placedTiles) {
        for (PlacedTile placed : placedTiles) {
            Square square = state.getBoard().getSquare(placed.getRow(), placed.getCol());

            if (!square.isBonusUsed() &&
                square.getBonusType() == Square.BonusType.TIME) {

                timeManager.applyTimeBonus(state, square.getBonusValue());
                square.consumeBonus();
            }
        }
    }

    // =========================
    // TIMER (UI CALLS THIS)
    // =========================

    public void tickClock() {
        timeManager.decrement(state);
        state.setGameStatus(endEvaluator.evaluate(state));
    }
}