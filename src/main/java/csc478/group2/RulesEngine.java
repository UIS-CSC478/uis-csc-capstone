package csc478.group2;

import java.util.Comparator;
import java.util.List;

public class RulesEngine {
	
	TurnAnalyzer analyzer = new TurnAnalyzer();
	
	public boolean isStraightLine(List<PlacedTile> placedTiles) {
       
		if (placedTiles == null || placedTiles.isEmpty()) {
            return false;
        }

        if (placedTiles.size() == 1) {
            return true;
        }

        boolean sameRow = true;
        boolean sameCol = true;

        int firstRow = placedTiles.get(0).getRow();
        int firstCol = placedTiles.get(0).getCol();

        for (PlacedTile placedTile : placedTiles) {
            if (placedTile.getRow() != firstRow) {
                sameRow = false;
            }

            if (placedTile.getCol() != firstCol) {
                sameCol = false;
            }
        }

        return sameRow || sameCol;
    }
	

	public boolean isContinuous(Board board, List<PlacedTile> tiles) {
		
		if(tiles.size() <= 1) {
			return true;
		}
		
		boolean sameRow = tiles.stream().allMatch(t -> t.getRow() == tiles.get(0).getRow());
		
		if(sameRow = true) {
			
			tiles.sort(Comparator.comparingInt(PlacedTile::getCol));
			int row = tiles.get(0).getRow();
		
			for(int col = tiles.get(0).getCol(); col <= tiles.get(tiles.size() - 1).getCol(); col++) {
				
				final int currentCol = col;
				
				if(board.getCell(row, currentCol) == Board.emptyCell &&
				tiles.stream().noneMatch(t -> t.getCol() == currentCol)) {
					return false;
				}
			}
		} else {
			
			tiles.sort(Comparator.comparingInt(PlacedTile::getRow));
			int col = tiles.get(0).getCol(); 
			
			for(int row = tiles.get(0).getRow(); row <= tiles.get(tiles.size() - 1).getRow(); row++) {
				
				final int currentRow = row;
				if(board.getCell(currentRow,  col) == Board.emptyCell &&
					tiles.stream().noneMatch(t -> t.getRow() == currentRow)) {
					return false;
				}
					
			}
		}
		
		return true;
	}
	
	//Center at (7,7)
	
	public boolean isCentered(Board board, List<PlacedTile> tiles) {
		
		if(!board.isEmpty()) {
			return true;
		}
		
		for(PlacedTile pt : tiles) {
			if(pt.getRow() == 7 && pt.getCol() == 7) {
				return true;
			}
		}
		
		return false;
	}
	
	// New tiles connected to old tiles
	
	public boolean isConnected(Board board, List<PlacedTile> tiles) {
		
		if(board.isEmpty()) {
			return true;
		}
		
		for(PlacedTile pt : tiles) {
			
			int r = pt.getRow();
			int c = pt.getCol();
			
			if((r > 0 && board.getCell(r - 1, c) != Board.emptyCell) ||
			  (r < Board.boardSize - 1 && board.getCell(r + 1,  c) != Board.emptyCell) ||
			  (c > 0 && board.getCell(r,  c - 1) != Board.emptyCell) || 
			  (c < Board.boardSize - 1 && board.getCell(r,  c + 1) != Board.emptyCell)){
				  
				return true;
			}
		}
		
		return false;
	}
	
	public boolean fitsBoard(List<PlacedTile> tiles, Board board) {
		
		for(PlacedTile pt : tiles) {
			if(!board.isValidPosition(pt.getRow(), pt.getCol())) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean minWordLength(String word) {
		
		return word != null && word.length() >= 2;
	}
	
	public boolean squareAvailability(List<PlacedTile> tiles, Board board) {
		
		for(PlacedTile pt : tiles) {
			if(!board.isEmptyAt(pt.getRow(), pt.getCol())) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean wordExists(String word, WordValidation validator) {
		return validator.isValidWord(word);
	}
		
	public boolean validateMove(Board board, List<PlacedTile> tiles, WordValidation validator) {
		
		if(!isStraightLine(tiles)) {
			return false;
		}
		
		if(!isContinuous(board, tiles)) {
			return false;
		}
		
		if(!isCentered(board, tiles)) {
			return false;
		}
		
		if(!isConnected(board, tiles)) {
			return false;
		}
		
		
		
		String word = analyzer.buildWordFromBoard(board, tiles);
		
		if(!minWordLength(word)) {
			return false;
		}
		
		if(!wordExists(word, validator)) {
			return false;
		}
		
		// output for the test rules engine class,,
		
		System.out.println("Straight line: " + isStraightLine(tiles));
		System.out.println("Continuous: " + isContinuous(board, tiles));
		System.out.println("First centered: " + isCentered(board, tiles));
		System.out.println("Connected: " + isConnected(board, tiles));

		System.out.println("Word: " + word);

		System.out.println("Valid word: " + validator.isValidWord(word));
		
		return true;
	
	}
	
}
