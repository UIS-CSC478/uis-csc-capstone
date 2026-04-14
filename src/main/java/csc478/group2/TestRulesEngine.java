package csc478.group2;
import java.util.ArrayList;
import java.util.List;

//AI Created to test rules engine
public class TestRulesEngine {
	    public static void main(String[] args) {
	    	 
	    	Board board = new Board();
	         RulesEngine rules = new RulesEngine();
	         WordValidation validator = new WordValidation();

	         List<PlacedTile> tiles = new ArrayList<>();

	         // Create tiles for word "CAT"
	         Tile t1 = new Tile('C', 3);
	         Tile t2 = new Tile('A', 1);
	         Tile t3 = new Tile('T', 1);

	         // Place them at center row (valid first move)
	         tiles.add(new PlacedTile(7, 7, t1));
	         tiles.add(new PlacedTile(7, 8, t2));
	         tiles.add(new PlacedTile(7, 9, t3));

	         // Simulate placing on board (IMPORTANT)
	         board.placeTile(7, 7, t1);
	         board.placeTile(7, 8, t2);
	         board.placeTile(7, 9, t3);

	         System.out.println("=== TEST START ===");

	         boolean result = rules.validateMove(board, tiles, validator);

	         System.out.println("");
	         System.out.println("Valid move: " + result);
	    }
}


