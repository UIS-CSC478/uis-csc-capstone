package csc478.group2;

//1.3.3
//Class Description: The primary purpose of the Tile class is to represent an individual letter tile, storing its character and point value. It is used for placing letters on the board and calculating scores.

public class Tile {
	// 1.3.3.0 Each Tile shall contain a character.
	private final char letter;
	// 1.3.3.1 Each Tile shall derive scoring from word length logic.
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

	public Tile asBlankLetter(char chosenLetter) {
		if (!blank) {
			return this;
		}

		return new Tile(Character.toUpperCase(chosenLetter), 0, true);
	}
}