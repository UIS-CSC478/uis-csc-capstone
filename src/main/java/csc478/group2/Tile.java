package csc478.group2;

public class Tile {

    private final char letter;
    private final int pointValue;
    private final boolean blank;

    public Tile(char letter, int pointValue) {
        this(letter, pointValue, false);
    }

    public Tile(char letter, int pointValue, boolean blank) {
        this.letter = Character.toUpperCase(letter);
        this.pointValue = pointValue;
        this.blank = blank;
    }

    public char getLetter() {
        return letter;
    }

    public int getPointValue() {
        return blank ? 0 : pointValue;
    }

    public boolean isBlank() {
        return blank;
    }
}