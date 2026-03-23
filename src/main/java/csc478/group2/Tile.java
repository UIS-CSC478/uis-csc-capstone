package csc478.group2;

import java.util.ArrayList;
import java.util.List;

public class Tile {

	private final char letter;
	private final int pointValue;

	public Tile(char letter, int pointValue) {
		this.letter = letter;
		this.pointValue = pointValue;
	}

	public char getLetter() {
		return letter;
	}

	public int getPointValue() {
		return pointValue;
	}
	
	private static final List<Tile> SAMPLE_BAG = List.of(
			new Tile('A', 1), new Tile('A', 1), new Tile('A', 1),
			new Tile('A', 1), new Tile('A', 1), new Tile('A', 1), 
			new Tile('A', 1), new Tile('A', 1), new Tile('A', 1),
			
			new Tile('B', 3), new Tile('B', 3),

			new Tile('C', 3), new Tile('C', 3),

			new Tile('D', 2), new Tile('D', 2), new Tile('D', 2), 
			new Tile('D', 2),

			new Tile('E', 1), new Tile('E', 1), new Tile('E', 1), 
			new Tile('E', 1), new Tile('E', 1), new Tile('E', 1),
			new Tile('E', 1), new Tile('E', 1), new Tile('E', 1), 
			new Tile('E', 1), new Tile('E', 1), new Tile('E', 1),

			new Tile('F', 4), new Tile('F', 4),

			new Tile('G', 2), new Tile('G', 2), 
			new Tile('G', 2),

			new Tile('H', 4), new Tile('H', 4),

			new Tile('I', 1), new Tile('I', 1), new Tile('I', 1), 
			new Tile('I', 1), new Tile('I', 1), new Tile('I', 1),
			new Tile('I', 1), new Tile('I', 1), new Tile('I', 1),

			new Tile('J', 8),

			new Tile('K', 5),

			new Tile('L', 1), new Tile('L', 1), new Tile('L', 1), 
			new Tile('L', 1),

			new Tile('M', 3), new Tile('M', 3),

			new Tile('N', 1), new Tile('N', 1), new Tile('N', 1), 
			new Tile('N', 1), new Tile('N', 1), new Tile('N', 1),

			new Tile('O', 1), new Tile('O', 1), new Tile('O', 1), 
			new Tile('O', 1), new Tile('O', 1), new Tile('O', 1),
			new Tile('O', 1), new Tile('O', 1),

			new Tile('P', 3), new Tile('P', 3),

			new Tile('Q', 10),

			new Tile('R', 1), new Tile('R', 1), new Tile('R', 1), 
			new Tile('R', 1), new Tile('R', 1), new Tile('R', 1),

			new Tile('S', 1), new Tile('S', 1), new Tile('S', 1), 
			new Tile('S', 1),

			new Tile('T', 1), new Tile('T', 1), new Tile('T', 1), 
			new Tile('T', 1), new Tile('T', 1), new Tile('T', 1),

			new Tile('U', 1), new Tile('U', 1), new Tile('U', 1), 
			new Tile('U', 1),

			new Tile('V', 4), new Tile('V', 4),

			new Tile('W', 4), new Tile('W', 4),

			new Tile('X', 8),

			new Tile('Y', 4), new Tile('Y', 4),

			new Tile('Z', 10),

			new Tile('!', 0), new Tile('!', 0));


	public static List<Tile> createTileBag() {
		return new ArrayList<>(SAMPLE_BAG);
	}

}
