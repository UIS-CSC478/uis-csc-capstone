package csc478.group2;

public class Square {

    public enum BonusType {
        NONE,
        DOUBLE_LETTER,
        TRIPLE_LETTER,
        DOUBLE_WORD,
        TRIPLE_WORD,
        TIME
    }

    private final int row;
    private final int col;
    private boolean occupied;
    private Tile tile;
    private BonusType bonusType;
    private int bonusValue;
    private boolean bonusUsed;

    public Square(int row, int col, BonusType bonusType, int bonusValue) {
        this.row = row;
        this.col = col;
        this.bonusType = bonusType;
        this.bonusValue = bonusValue;
        this.occupied = false;
        this.tile = null;
        this.bonusUsed = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean placeTile(Tile t) {
        if (occupied) return false;
        tile = t;
        occupied = true;
        return true;
    }

    public Tile removeTile() {
        Tile old = tile;
        tile = null;
        occupied = false;
        return old;
    }

    public char getLetter() {
        return tile == null ? Board.EMPTY_CELL : tile.getLetter();
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public int getBonusValue() {
        return bonusValue;
    }

    public boolean isBonusUsed() {
        return bonusUsed;
    }

    public void consumeBonus() {
        bonusUsed = true;
        bonusType = BonusType.NONE;
        bonusValue = 0;
    }
}