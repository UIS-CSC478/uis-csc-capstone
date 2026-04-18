package csc478.group2;

public class EndConditionEvaluator {

    public boolean checkWinCondition(int score, int targetScore) {
        return score >= targetScore;
    }

    public boolean checkLoseCondition(int timeRemaining) {
        return timeRemaining <= 0;
    }

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