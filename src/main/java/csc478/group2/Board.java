package csc478.group2;

public class Board {

    public static final int boardSize = 15;
    public static final char emptyCell = '\0';
    private char[][] grid;

    public Board() {
        grid = new char[boardSize][boardSize];
    }
    
    //unused rn
//    public char[][] getGrid() {
//        return grid;
//    }
    
    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    public boolean isEmptyAt(int row, int col) {
        return grid[row][col] == emptyCell;
    }

    public void placeTile(int row, int col, Tile tile) {
        grid[row][col] = tile.getLetter();
    }
    
    public void clearPosition(int row, int col) {
        grid[row][col] = emptyCell;
    }

    
}