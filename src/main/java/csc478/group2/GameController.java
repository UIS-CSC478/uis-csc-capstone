package csc478.group2;

// 1.3.8 Game Flow Control Requirements
// Class Description: The GameController class manages the overall game by coordinating the game state, rack, tile bag, rules validation, scoring, time updates, and win/loss evaluation. It processes submitted turns, along with most game actions. 

import java.util.ArrayList;
import java.util.List;

public class GameController {

	private final GameState state;
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

		rack.refill(tileBag);
	}

	public GameState getGameState() {
		return state;
	}

	public List<Tile> getRackTiles() {
		return new ArrayList<>(rack.getTiles());
	}

	public void returnTilesToRack(List<PlacedTile> tiles) {
		for (PlacedTile t : tiles) {
			rack.addTile(t.getTile());
		}
	}

	public boolean submitTurn(List<PlacedTile> placedTiles) {

		if (state.getGameStatus() != GameState.GameStatus.RUNNING) {
			return false;
		}

		if (placedTiles == null || placedTiles.isEmpty()) {
			return false;
		}
		// 1.3.8.1 GameController shall call RulesEngine for validation.
		if (!rulesEngine.validateMove(state, placedTiles, wordValidation)) {
			return false;
		}
		// 1.3.8.2 Upon successful validation, GameController shall update GameState.
		applyPlacedTilesToBoard(placedTiles);

		// 1.3.8.3 GameController shall trigger ScoreCalculator for scoring.
		int score = scoreCalculator.calculateTurnScore(state, placedTiles);
		state.setCurrentScore(state.getCurrentScore() + score);

		rack.refill(tileBag);
		// 1.3.8.4 GameController shall trigger TimeManager updates as needed.
		// 1.3.8.5 After each move, GameController shall invoke EndConditionEvaluator.
		timeManager.tickAfterTurn(state);
		state.setGameStatus(endEvaluator.evaluate(state));

		return true;
	}

	// 1.3.8.0 All placement input shall be processed by GameController.
	private void applyPlacedTilesToBoard(List<PlacedTile> placedTiles) {
		Board board = state.getBoard();

		for (PlacedTile placed : placedTiles) {
			board.placeTile(placed.getRow(), placed.getCol(), placed.getTile());
			rack.removeTile(placed.getTile());
		}
	}

	public void tickClock() {
		timeManager.decrement(state);
		state.setGameStatus(endEvaluator.evaluate(state));
	}
}