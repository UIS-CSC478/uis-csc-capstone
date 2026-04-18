package csc478.group2;

public class GameState {

    public enum GameStatus {
        RUNNING,
        WON,
        LOST
    }

    private int currentScore;
    private int remainingTime;
    private int targetScore;
    private final Board board;
    private GameStatus gameStatus;

    public GameState() {
        this.board = new Board();
        this.currentScore = 0;
        this.remainingTime = 300;
        this.targetScore = 100;
        this.gameStatus = GameStatus.RUNNING;
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

    void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}