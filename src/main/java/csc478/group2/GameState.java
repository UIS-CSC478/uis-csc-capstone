package csc478.group2;

// 1.3.1 Core Game State Requirements
// Class Description: The GameState class serves as the central source of truth for the game, storing the current score, remaining time, target score, board state, and overall game status. It is updated only by core logic components to ensure consistent and controlled game behavior.
// 1.3.1.6 Only the RulesEngine and GameController shall modify GameState.
// 1.3.1.7 Invalid placement attempts shall not modify GameState.

public class GameState {

	public enum GameStatus {
		RUNNING, WON, LOST
	}

	private int currentScore;
	private int remainingTime;
	private int targetScore;
	private final Board board;
	private GameStatus gameStatus;

	// 1.3.1.0 The system shall maintain a single GameState object as the
	// authoritative source of truth for all game data.
	public GameState() {
		this.board = new Board();
		this.currentScore = 0;
		this.remainingTime = 300;
		this.targetScore = 150;
		this.gameStatus = GameStatus.RUNNING;
	}

	// 1.3.1.1 The GameState shall contain currentScore.
	public int getCurrentScore() {
		return currentScore;
	}

	// 1.3.1.2 The GameState shall contain remainingTime.
	public int getRemainingTime() {
		return remainingTime;
	}

	// 1.3.1.3 The GameState shall contain targetScore.
	public int getTargetScore() {
		return targetScore;
	}

	// 1.3.1.4 The GameState shall contain boardState.
	public Board getBoard() {
		return board;
	}

	// 1.3.1.5 The GameState shall contain gameStatus with possible values: RUNNING,
	// WON, LOST.
	public GameStatus getGameStatus() {
		return gameStatus;
	}

	// 1.3.1.1 The GameState shall contain currentScore.
	void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	// 1.3.1.2 The GameState shall contain remainingTime.
	void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	// 1.3.1.5 The GameState shall contain gameStatus with possible values: RUNNING,
	// WON, LOST.
	void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
}