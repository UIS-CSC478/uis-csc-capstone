package csc478.group2;

import java.util.List;

public class ScoreCalculator {

    public int calculateTurnScore(GameState state, List<PlacedTile> placedTiles) {
        int total = 0;
        int wordMultiplier = 1;

        for (PlacedTile placed : placedTiles) {
            Square square = state.getBoard().getSquare(placed.getRow(), placed.getCol());
            int value = placed.getTile().getPointValue();

            if (!square.isBonusUsed()) {
                switch (square.getBonusType()) {
                    case DOUBLE_LETTER -> value *= 2;
                    case TRIPLE_LETTER -> value *= 3;
                    case DOUBLE_WORD -> wordMultiplier *= 2;
                    case TRIPLE_WORD -> wordMultiplier *= 3;
                    default -> { }
                }
            }

            total += value;
        }

        total *= wordMultiplier;

        for (PlacedTile placed : placedTiles) {
            Square square = state.getBoard().getSquare(placed.getRow(), placed.getCol());
            if (!square.isBonusUsed()) {
                square.consumeBonus();
            }
        }

        return total;
    }
}