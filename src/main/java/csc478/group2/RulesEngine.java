package csc478.group2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RulesEngine {

    public boolean validateMove(GameState state, List<PlacedTile> placedTiles, WordValidation dictionary) {
        Board board = state.getBoard();

        if (!validateSquaresAvailable(board, placedTiles)) return false;
        if (!validateStraightLine(placedTiles)) return false;
        if (!validateContinuous(board, placedTiles)) return false;
        if (!validateFirstMoveCenter(board, placedTiles)) return false;
        if (!validateConnected(board, placedTiles)) return false;

        Board tempBoard = copyBoard(board);
        for (PlacedTile placed : placedTiles) {
            tempBoard.placeTile(placed.getRow(), placed.getCol(), placed.getTile());
        }

        List<String> words = getAllWordsFormed(tempBoard, placedTiles);
        if (words.isEmpty()) return false;

        for (String word : words) {
            if (word.length() > 1 && !dictionary.isValidWord(word)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateSquaresAvailable(Board board, List<PlacedTile> placedTiles) {
        for (PlacedTile placed : placedTiles) {
            if (!board.isValidPosition(placed.getRow(), placed.getCol())) return false;
            if (!board.isEmptyAt(placed.getRow(), placed.getCol())) return false;
        }
        return true;
    }

    private boolean validateStraightLine(List<PlacedTile> placedTiles) {
        boolean sameRow = true;
        boolean sameCol = true;

        int firstRow = placedTiles.get(0).getRow();
        int firstCol = placedTiles.get(0).getCol();

        for (PlacedTile placed : placedTiles) {
            if (placed.getRow() != firstRow) sameRow = false;
            if (placed.getCol() != firstCol) sameCol = false;
        }

        return sameRow || sameCol;
    }

    private boolean validateContinuous(Board board, List<PlacedTile> placedTiles) {
        if (placedTiles.size() == 1) return true;

        boolean sameRow = true;
        int row = placedTiles.get(0).getRow();
        int col = placedTiles.get(0).getCol();

        for (PlacedTile placed : placedTiles) {
            if (placed.getRow() != row) {
                sameRow = false;
                break;
            }
        }

        Board tempBoard = copyBoard(board);
        for (PlacedTile placed : placedTiles) {
            tempBoard.placeTile(placed.getRow(), placed.getCol(), placed.getTile());
        }

        if (sameRow) {
            int minCol = Integer.MAX_VALUE;
            int maxCol = Integer.MIN_VALUE;

            for (PlacedTile placed : placedTiles) {
                minCol = Math.min(minCol, placed.getCol());
                maxCol = Math.max(maxCol, placed.getCol());
            }

            for (int c = minCol; c <= maxCol; c++) {
                if (tempBoard.isEmptyAt(row, c)) return false;
            }
        } else {
            int minRow = Integer.MAX_VALUE;
            int maxRow = Integer.MIN_VALUE;

            for (PlacedTile placed : placedTiles) {
                minRow = Math.min(minRow, placed.getRow());
                maxRow = Math.max(maxRow, placed.getRow());
            }

            for (int r = minRow; r <= maxRow; r++) {
                if (tempBoard.isEmptyAt(r, col)) return false;
            }
        }

        return true;
    }

    private boolean validateFirstMoveCenter(Board board, List<PlacedTile> placedTiles) {
        if (!board.isEmpty()) return true;

        for (PlacedTile placed : placedTiles) {
            if (placed.getRow() == 7 && placed.getCol() == 7) {
                return true;
            }
        }
        return false;
    }

    private boolean validateConnected(Board board, List<PlacedTile> placedTiles) {
        if (board.isEmpty()) return true;

        for (PlacedTile placed : placedTiles) {
            int r = placed.getRow();
            int c = placed.getCol();

            if ((r > 0 && !board.isEmptyAt(r - 1, c)) ||
                (r < Board.SIZE - 1 && !board.isEmptyAt(r + 1, c)) ||
                (c > 0 && !board.isEmptyAt(r, c - 1)) ||
                (c < Board.SIZE - 1 && !board.isEmptyAt(r, c + 1))) {
                return true;
            }
        }

        return false;
    }

    private List<String> getAllWordsFormed(Board board, List<PlacedTile> placedTiles) {
        Set<String> words = new HashSet<>();

        for (PlacedTile placed : placedTiles) {
            String horizontal = buildWord(board, placed.getRow(), placed.getCol(), 0, 1);
            String vertical = buildWord(board, placed.getRow(), placed.getCol(), 1, 0);

            if (horizontal.length() > 1) words.add(horizontal);
            if (vertical.length() > 1) words.add(vertical);
        }

        return new ArrayList<>(words);
    }

    private String buildWord(Board board, int row, int col, int dRow, int dCol) {
        int r = row;
        int c = col;

        while (board.isValidPosition(r - dRow, c - dCol) && !board.isEmptyAt(r - dRow, c - dCol)) {
            r -= dRow;
            c -= dCol;
        }

        StringBuilder word = new StringBuilder();

        while (board.isValidPosition(r, c) && !board.isEmptyAt(r, c)) {
            word.append(board.getCell(r, c));
            r += dRow;
            c += dCol;
        }

        return word.toString();
    }

    private Board copyBoard(Board board) {
        Board copy = new Board();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Square original = board.getSquare(r, c);
                Square target = copy.getSquare(r, c);

                if (original.isOccupied()) {
                    copy.placeTile(r, c, original.getTile());
                }

                if (target.getBonusType() != original.getBonusType() ||
                    target.getBonusValue() != original.getBonusValue()) {
                    copy = copyWithBonuses(board);
                    return copy;
                }
            }
        }

        return copy;
    }

    private Board copyWithBonuses(Board originalBoard) {
        Board copy = new Board();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Square original = originalBoard.getSquare(r, c);
                Square replacement = new Square(r, c, original.getBonusType(), original.getBonusValue());
                if (original.isOccupied()) {
                    replacement.placeTile(original.getTile());
                }
                if (replacement.isBonusUsed() != original.isBonusUsed() && !original.isBonusUsed()) {
                    // no-op
                }
            }
        }
        return copy;
    }
}