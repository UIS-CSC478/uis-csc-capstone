package csc478.group2;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameState gameState;
    private TileBag tileBag;
    private Rack rack;
    private List<PlacedTile> tilesPlacedThisTurn;
    private WordValidation wordValidator;
    
    //CALLS CLASS WHICH NEEDS TO BE RE-WRITTEN HERE FOR UI TESTING PURPOSES
    private TurnAnalyzer turnAnalyzer;

    
    public void startNewGame() {
        gameState = new GameState();
        tileBag = new TileBag();
        rack = new Rack();
        tilesPlacedThisTurn = new ArrayList<>();
        wordValidator = new WordValidation();
        refillRack();
        
      //CALLS CLASS WHICH NEEDS TO BE RE-WRITTEN HERE FOR UI TESTING PURPOSES
        turnAnalyzer = new TurnAnalyzer();
    }

    public GameController() {
        startNewGame();
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Tile> getRack() {
        return rack.getTiles();
    }
    
    public void refillRack() {
        rack.refill(tileBag);
    }

    public boolean selectTile(int rackIndex) {
        return rack.selectTile(rackIndex);
    }

    public void clearSelectedTile() {
        rack.clearSelectedTile();
    }

    public boolean placeSelectedTile(int row, int col) {
    	//initializes board so it looks cleaner
        Board board = gameState.getBoard();
    	
        //check if tile was selected at all
    	if (!rack.hasSelectedTile()) {
            return false;
        }
    	
        //make sure selection is not outside of the 15x15 board
        if (!board.isValidPosition(row, col)) {
            return false;
        }
        
        //make sure there isn't another tile in place
        if (!board.isEmptyAt(row, col)) {
            return false;
        }
        
       //initialize the selected tile to place
        Tile tileToPlace = rack.getSelectedTile();
        //put tile onto board
        board.placeTile(row, col, tileToPlace);
        //keep track of tiles played this turn
        tilesPlacedThisTurn.add(new PlacedTile(row, col, tileToPlace));
        //remove the tile from the  player once placed
        rack.removeSelectedTile();
        
        return true;
    }

    public void cancelTurn() {
    	//initializes board so it looks cleaner
        Board board = gameState.getBoard();

        //loop through all tiles played this turn
        for (PlacedTile placedTile : tilesPlacedThisTurn) {
        	//remove tiles placed at the positions played this turn
            board.clearPosition(placedTile.getRow(), placedTile.getCol());
            //place back into players rack
            rack.addTile(placedTile.getTile());
        }
        //clear the list of tiles played this turn
        tilesPlacedThisTurn.clear();
        //get rid of any selected tiles as well
        rack.clearSelectedTile();
    }
    
    //below is partly AI written, will need to be written again in our own code
  public String getWordForCurrentTurn() {
	//check if anything was placed
    if (tilesPlacedThisTurn.isEmpty()) {
        return "";
    }
    //check if the tiles placed are in a straight line  up or down
    //if not then return blank
    if (!turnAnalyzer.isStraightLine(tilesPlacedThisTurn)) {
        return "";
    }
    //build the word
    return turnAnalyzer.buildWordFromBoard(gameState.getBoard(), tilesPlacedThisTurn);
}

  
  	//CALLS CLASS WHICH NEEDS TO BE RE-WRITTEN HERE FOR UI TESTING PURPOSES
	public boolean submitTurn() {
	    if (tilesPlacedThisTurn.isEmpty()) {
	        return false;
	    }
	
	    Board board = gameState.getBoard();
	
	    if (!turnAnalyzer.isStraightLine(tilesPlacedThisTurn)) {
	        cancelTurn();
	        return false;
	    }
	
	    String word = turnAnalyzer.buildWordFromBoard(board, tilesPlacedThisTurn);
	
	    if (word == null || word.isBlank()) {
	        cancelTurn();
	        return false;
	    }
	
	    if (!wordValidator.isValidWord(word)) {
	        cancelTurn();
	        return false;
	    }
	
	    int turnScore = turnAnalyzer.calculateTurnScore(tilesPlacedThisTurn);
	    int newTotal = gameState.getCurrentScore() + turnScore;
	    gameState.setCurrentScore(newTotal);
	
	    if (gameState.getCurrentScore() >= gameState.getTargetScore()) {
	        gameState.setGameStatus(GameState.GameStatus.WON);
	    }
	
	    tilesPlacedThisTurn.clear();
	    rack.clearSelectedTile();
	    refillRack();
	
	    return true;
	} 
    
}