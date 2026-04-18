package csc478.group2;

public class TimeManager {

    // called every second by UI
    public void decrement(GameState state) {
        if (state.getGameStatus() != GameState.GameStatus.RUNNING) {
            return;
        }

        state.setRemainingTime(state.getRemainingTime() - 1);
    }

    // called after a turn
    public void tickAfterTurn(GameState state) {
        decrement(state);
    }

    public void applyTimeBonus(GameState state, int bonus) {
        state.setRemainingTime(state.getRemainingTime() + bonus);
    }

    public boolean checkTimeExpired(GameState state) {
        return state.getRemainingTime() <= 0;
    }
}