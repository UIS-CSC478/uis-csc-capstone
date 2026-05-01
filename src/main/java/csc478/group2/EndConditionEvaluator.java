package csc478.group2;

// 1.3.7 End Condition Requirements
// Class Description: The EndConditionEvaluator class determines the current game outcome by evaluating whether the player has met the win condition or lose condition, and returns the appropriate game status.

public class EndConditionEvaluator {

	// 1.3.7.0 EndConditionEvaluator shall check win condition using
	// checkWinCondition(score, targetScore).
	public boolean checkWinCondition(int score, int targetScore) {
		// 1.3.7.1 A win shall occur when currentScore >= targetScore.
		return score >= targetScore;
	}

	// 1.3.6.4 If time expires, the EndConditionEvaluator shall update the GameState
	// to LOST.
	// 1.3.7.2 EndConditionEvaluator shall check lose condition using
	// checkLoseCondition(timeRemaining).
	public boolean checkLoseCondition(int timeRemaining) {
		// 1.3.7.3 A loss shall occur when remainingTime <= 0.
		return timeRemaining <= 0;
	}

	// 1.3.7.4 Upon win, GameState.gameStatus shall be set to WON.
	// 1.3.7.5 Upon loss, GameState.gameStatus shall be set to LOST.
	public GameState.GameStatus evaluate(GameState state) {
		if (checkWinCondition(state.getCurrentScore(), state.getTargetScore())) {
			return GameState.GameStatus.WON;
		}
		if (checkLoseCondition(state.getRemainingTime())) {
			return GameState.GameStatus.LOST;
		}
		return GameState.GameStatus.RUNNING;
	}
}