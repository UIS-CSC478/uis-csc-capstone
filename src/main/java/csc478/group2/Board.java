package csc478.group2;

public class Board {

    public static final int SIZE = 15;
    public static final char EMPTY_CELL = '\0';

    private final Square[][] grid;

    public Board() {
        grid = new Square[SIZE][SIZE];

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = new Square(r, c, Square.BonusType.NONE, 0);
            }
        }

        initializeBonuses();
    }

    private void initializeBonuses() {
        // Triple Word
        setBonus(0, 0, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(0, 7, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(0, 14, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(7, 0, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(7, 14, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(14, 0, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(14, 7, Square.BonusType.TRIPLE_WORD, 3);
        setBonus(14, 14, Square.BonusType.TRIPLE_WORD, 3);

        // Double Word
        int[][] dw = {
            {1,1},{2,2},{3,3},{4,4},
            {10,10},{11,11},{12,12},{13,13},
            {1,13},{2,12},{3,11},{4,10},
            {10,4},{11,3},{12,2},{13,1},
            {7,7}
        };
        for (int[] p : dw) {
            setBonus(p[0], p[1], Square.BonusType.DOUBLE_WORD, 2);
        }

        // Triple Letter
        int[][] tl = {
            {1,5},{1,9},
            {5,1},{5,5},{5,9},{5,13},
            {9,1},{9,5},{9,9},{9,13},
            {13,5},{13,9}
        };
        for (int[] p : tl) {
            setBonus(p[0], p[1], Square.BonusType.TRIPLE_LETTER, 3);
        }

        // Double Letter
        int[][] dl = {
            {0,3},{0,11},
            {2,6},{2,8},
            {3,0},{3,7},{3,14},
            {6,2},{6,6},{6,8},{6,12},
            {7,3},{7,11},
            {8,2},{8,6},{8,8},{8,12},
            {11,0},{11,7},{11,14},
            {12,6},{12,8},
            {14,3},{14,11}
        };
        for (int[] p : dl) {
            setBonus(p[0], p[1], Square.BonusType.DOUBLE_LETTER, 2);
        }

    }

    private void setBonus(int row, int col, Square.BonusType type, int value) {
        grid[row][col] = new Square(row, col, type, value);
    }

    public Square getSquare(int r, int c) {
        return grid[r][c];
    }

    public boolean isValidPosition(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public boolean isEmptyAt(int r, int c) {
        return !grid[r][c].isOccupied();
    }

    public boolean placeTile(int r, int c, Tile tile) {
        return grid[r][c].placeTile(tile);
    }

    public void clearPosition(int r, int c) {
        grid[r][c].removeTile();
    }

    public char getCell(int r, int c) {
        return grid[r][c].isOccupied() ? grid[r][c].getLetter() : EMPTY_CELL;
    }

    public boolean isEmpty() {
        for (Square[] row : grid) {
            for (Square square : row) {
                if (square.isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }
}