package csc478.group2;

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