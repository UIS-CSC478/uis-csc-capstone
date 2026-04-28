package csc478.group2;

// 1.3.2 Board and Square Requirements
// Class Description: The primary purpose of the Board class is to represent the game grid and manage all squares, including tile placement, bonus types, and check whether tiles are empty. 

public class Board {

	public static final int SIZE = 15;
	public static final char EMPTY_CELL = '\0';

	private final Square[][] grid;

	// 1.3.2.0 The system shall initialize a 15x15 2D Board grid at game start.
	public Board() {
		// 1.3.2.1 The Board shall contain Square objects.
		// 1.3.2.2 Each Square shall maintain row and column position.
		grid = new Square[SIZE][SIZE];

		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				grid[r][c] = new Square(r, c, Square.BonusType.NONE, 0);
			}
		}

		initializeBonuses();
	}

	// 1.3.2.4 Each Square shall maintain a bonusValue.
	private void initializeBonuses() {
		int[][] tw = { { 0, 0 }, { 0, 7 }, { 0, 14 }, { 7, 0 }, { 7, 14 }, { 14, 0 }, { 14, 7 }, { 14, 14 } };

		for (int[] p : tw) {
			setBonus(p[0], p[1], Square.BonusType.TRIPLE_WORD, 3);
		}

		int[][] dw = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 10, 10 }, { 11, 11 }, { 12, 12 }, { 13, 13 },
				{ 1, 13 }, { 2, 12 }, { 3, 11 }, { 4, 10 }, { 10, 4 }, { 11, 3 }, { 12, 2 }, { 13, 1 }, { 7, 7 } };
		for (int[] p : dw) {
			setBonus(p[0], p[1], Square.BonusType.DOUBLE_WORD, 2);
		}

		int[][] tl = { { 1, 5 }, { 1, 9 }, { 5, 1 }, { 5, 5 }, { 5, 9 }, { 5, 13 }, { 9, 1 }, { 9, 5 }, { 9, 9 },
				{ 9, 13 }, { 13, 5 }, { 13, 9 } };
		for (int[] p : tl) {
			setBonus(p[0], p[1], Square.BonusType.TRIPLE_LETTER, 3);
		}

		int[][] dl = { { 0, 3 }, { 0, 11 }, { 2, 6 }, { 2, 8 }, { 3, 0 }, { 3, 7 }, { 3, 14 }, { 6, 2 }, { 6, 6 },
				{ 6, 8 }, { 6, 12 }, { 7, 3 }, { 7, 11 }, { 8, 2 }, { 8, 6 }, { 8, 8 }, { 8, 12 }, { 11, 0 }, { 11, 7 },
				{ 11, 14 }, { 12, 6 }, { 12, 8 }, { 14, 3 }, { 14, 11 } };
		for (int[] p : dl) {
			setBonus(p[0], p[1], Square.BonusType.DOUBLE_LETTER, 2);
		}

	}

	// 1.3.2.6 Bonus squares shall apply their effect only once when first used.
	private void setBonus(int row, int col, Square.BonusType type, int value) {
		grid[row][col] = new Square(row, col, type, value);
	}

	public Square getSquare(int r, int c) {
		return grid[r][c];
	}

	// 1.3.2.3 Each Square shall maintain an occupied flag.
	// 1.3.2.5 A Square marked as occupied shall not accept additional tiles.
	// 1.3.3.2 Tiles shall only be placed onto unoccupied Squares.
	public boolean isValidPosition(int r, int c) {
		return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
	}

	// 1.3.2.3 Each Square shall maintain an occupied flag.
	// 1.3.2.5 A Square marked as occupied shall not accept additional tiles.
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

	// 1.3.2.3 Each Square shall maintain an occupied flag.
	// 1.3.2.5 A Square marked as occupied shall not accept additional tiles.
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