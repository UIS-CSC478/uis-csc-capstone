package csc478.group2;

// Class Description: The PlacedTile class represents a tile placed on the board during a turn, storing its row, column position, and associated tile.

public class PlacedTile {
	private final int row;
	private final int col;
	private final Tile tile;

	public PlacedTile(int row, int col, Tile tile) {
		this.row = row;
		this.col = col;
		this.tile = tile;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Tile getTile() {
		return tile;
	}
}