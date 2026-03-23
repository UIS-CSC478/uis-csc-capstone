package csc478.group2;

public class GameState {

	private int currentScore;
	private int remainingTime;
	private int targetScore;
	private Board board;
	private GameStatus gameStatus;
	
	public enum GameStatus {
	    RUNNING,
	    WON,
	    LOST
	}
	
	public GameState() {
		board = new Board();
        currentScore = 0;
        remainingTime = 300;
        targetScore = 100;
        gameStatus = GameStatus.RUNNING;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

 
    void setCurrentScore(int score) {
        this.currentScore = score;
    }

    void setRemainingTime(int time) {
        this.remainingTime = time;
    }

    void setBoard(Board board) {
        this.board = board;
    }

    void setGameStatus(GameStatus status) {
        this.gameStatus = status;
    }
}
