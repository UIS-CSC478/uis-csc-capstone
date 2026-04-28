package csc478.group2;

// 1.3.6 Time System Requirements
// Class Description: The TimeManager class is responsible for managing the game’s timing by decrementing remaining time and determining when time has expired.
// 1.3.6.0 The GameState shall initialize remainingTime to the initial time limit at game start.
public class TimeManager {
	// 1.3.6.1 The TimeManager shall decrement remaining time only when the game
	// status is RUNNING (decrement).
	public void decrement(GameState state) {
		if (state.getGameStatus() != GameState.GameStatus.RUNNING) {
			return;
		}

		state.setRemainingTime(state.getRemainingTime() - 1);
	}

	// 1.3.6.2 The TimeManager shall update time after each turn using
	// tickAfterTurn(GameState).
	public void tickAfterTurn(GameState state) {
		decrement(state);
	}

	// 1.3.6.3 The TimeManager shall determine if time has expired using
	// checkTimeExpired(GameState).
	public boolean checkTimeExpired(GameState state) {
		return state.getRemainingTime() <= 0;
	}
}