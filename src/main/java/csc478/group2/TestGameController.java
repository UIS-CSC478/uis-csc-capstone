package csc478.group2;

//ChatGPT implemented since it will be deleted once UI is in place
public class TestGameController {  
    
    public static void main(String[] args) {

        GameController controller = new GameController();

        // Start the game
        controller.startNewGame();

        // Show starting rack
        System.out.println("Starting Rack:");
        printRack(controller);

        // ⚠️ For testing, we will FORCE the rack to be CAT
        controller.getRack().clear();
        controller.getRack().add(new Tile('C', 3));
        controller.getRack().add(new Tile('A', 1));
        controller.getRack().add(new Tile('T', 1));

        System.out.println("\nForced Rack (for testing CAT):");
        printRack(controller);

        // Place C
        controller.selectTile(0);
        controller.placeSelectedTile(7, 7);

        // Place A
        controller.selectTile(0); // index shifts after removal
        controller.placeSelectedTile(7, 8);

        // Place T
        controller.selectTile(0);
        controller.placeSelectedTile(7, 9);


        
      // Show word being formed
      String word = controller.getWordForCurrentTurn();
      System.out.println("\nWord formed: " + word);

      // Submit turn
      boolean success = controller.submitTurn();

      System.out.println("\nWas move accepted? " + success);
      System.out.println("Score: " + controller.getGameState().getCurrentScore());

      // Show board
      System.out.println("\nBoard after placing CAT:");
      printBoard(controller);
      
      
      
   // --- TEST INVALID WORD (FAIL CASE) ---
      controller.getRack().clear();
      controller.getRack().add(new Tile('X', 8));
      controller.getRack().add(new Tile('Y', 4));
      controller.getRack().add(new Tile('Z', 10));

      System.out.println("\nForced Rack (for invalid word XYZ):");
      printRack(controller);

      // Place X
      controller.selectTile(0);
      controller.placeSelectedTile(6, 7);

      // Place Y
      controller.selectTile(0);
      controller.placeSelectedTile(6, 8);

      // Place Z
      controller.selectTile(0);
      controller.placeSelectedTile(6, 9);

      // Try to submit invalid word
      String badWord = controller.getWordForCurrentTurn();
      System.out.println("\nInvalid Word formed: " + badWord);

      boolean badSuccess = controller.submitTurn();
      System.out.println("Was invalid move accepted? " + badSuccess);

      // Show board (should NOT include XYZ if cancel worked)
      System.out.println("\nBoard after invalid attempt:");
      printBoard(controller);
      
      
   // --- TEST SECOND WORD ON SAME BOARD ---
      controller.getRack().clear();
      controller.getRack().add(new Tile('D', 2));
      controller.getRack().add(new Tile('O', 1));
      controller.getRack().add(new Tile('G', 2));

      System.out.println("\nForced Rack (for DOG):");
      printRack(controller);

      // Place D (connecting to CAT)
      controller.selectTile(0);
      controller.placeSelectedTile(4, 2);

      // Place O
      controller.selectTile(0);
      controller.placeSelectedTile(5, 2);

      // Place G
      controller.selectTile(0);
      controller.placeSelectedTile(6, 2);

      // Show word
      String secondWord = controller.getWordForCurrentTurn();
      System.out.println("\nSecond Word formed: " + secondWord);

      // Submit turn
      boolean secondSuccess = controller.submitTurn();
      System.out.println("Was second move accepted? " + secondSuccess);
      System.out.println("Score: " + controller.getGameState().getCurrentScore());

      // Show updated board
      System.out.println("\nBoard after second word:");
      printBoard(controller);
        

    }

    private static void printRack(GameController controller) {
        for (Tile tile : controller.getRack()) {
            System.out.print(tile.getLetter() + "(" + tile.getPointValue() + ") ");
        }
        System.out.println();
    }

    private static void printBoard(GameController controller) {
    	Board board = controller.getGameState().getBoard();

    	for (int r = 0; r < 15; r++) {
    	    for (int c = 0; c < 15; c++) {
    	        char ch = board.getCell(r, c);
    	        System.out.print((ch == '\0' ? '.' : ch) + " ");
    	    }
    	    System.out.println();
    	}
    }
    
}