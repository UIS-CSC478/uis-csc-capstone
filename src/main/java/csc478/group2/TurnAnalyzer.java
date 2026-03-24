package csc478.group2;

//TO BE RE-WRITTEN MISSSING CRUCIAL FUNCTIONALITY AI HELP - ONLY BUILT TO HELP WITH UI DEVELOPMENT
import java.util.Comparator;
import java.util.List;

public class TurnAnalyzer {

    public boolean isStraightLine(List<PlacedTile> placedTiles) {
        if (placedTiles == null || placedTiles.isEmpty()) {
            return false;
        }

        if (placedTiles.size() == 1) {
            return true;
        }

        boolean sameRow = true;
        boolean sameCol = true;

        int firstRow = placedTiles.get(0).getRow();
        int firstCol = placedTiles.get(0).getCol();

        for (PlacedTile placedTile : placedTiles) {
            if (placedTile.getRow() != firstRow) {
                sameRow = false;
            }

            if (placedTile.getCol() != firstCol) {
                sameCol = false;
            }
        }

        return sameRow || sameCol;
    }

    public String buildWordFromBoard(Board board, List<PlacedTile> placedTiles) {
        if (placedTiles == null || placedTiles.isEmpty()) {
            return "";
        }

        if (placedTiles.size() == 1) {
            int row = placedTiles.get(0).getRow();
            int col = placedTiles.get(0).getCol();

            String horizontalWord = buildHorizontalWord(board, row, col);
            String verticalWord = buildVerticalWord(board, row, col);

            return horizontalWord.length() >= verticalWord.length()
                    ? horizontalWord
                    : verticalWord;
        }

        boolean sameRow = true;
        int firstRow = placedTiles.get(0).getRow();

        for (PlacedTile placedTile : placedTiles) {
            if (placedTile.getRow() != firstRow) {
                sameRow = false;
                break;
            }
        }

        if (sameRow) {
            placedTiles.sort(Comparator.comparingInt(PlacedTile::getCol));
            int row = placedTiles.get(0).getRow();
            int col = placedTiles.get(0).getCol();
            return buildHorizontalWord(board, row, col);
        } else {
            placedTiles.sort(Comparator.comparingInt(PlacedTile::getRow));
            int row = placedTiles.get(0).getRow();
            int col = placedTiles.get(0).getCol();
            return buildVerticalWord(board, row, col);
        }
    }

    public String buildHorizontalWord(Board board, int row, int col) {
        int startCol = col;

        while (startCol > 0 && board.getCell(row, startCol - 1) != Board.emptyCell) {
            startCol--;
        }

        StringBuilder word = new StringBuilder();
        int currentCol = startCol;

        while (currentCol < Board.boardSize && board.getCell(row, currentCol) != Board.emptyCell) {
            word.append(board.getCell(row, currentCol));
            currentCol++;
        }

        return word.toString();
    }

    public String buildVerticalWord(Board board, int row, int col) {
        int startRow = row;

        while (startRow > 0 && board.getCell(startRow - 1, col) != Board.emptyCell) {
            startRow--;
        }

        StringBuilder word = new StringBuilder();
        int currentRow = startRow;

        while (currentRow < Board.boardSize && board.getCell(currentRow, col) != Board.emptyCell) {
            word.append(board.getCell(currentRow, col));
            currentRow++;
        }

        return word.toString();
    }

    public int calculateTurnScore(List<PlacedTile> placedTiles) {
        int score = 0;

        for (PlacedTile placedTile : placedTiles) {
            score += placedTile.getTile().getPointValue();
        }

        return score;
    }
}