package csc478.group2;

// Class Description: The RulesEngine class is responsible for validating player moves by enforcing all game rules, including placement legality, word formation, connectivity, and dictionary validation. It ensures that only valid moves are accepted and that no changes are made to the game state when a move is invalid. :contentReference[oaicite:0]{index=0}
// Note: Upon combining implementations from each team member, the RulesEngine was modified using LLM assistance to better fit specific requirements documentation. 

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RulesEngine {

	// 1.3.4.5 The RulesEngine shall validate that the move forms at least one valid
	// word of length two or greater (validateMove).
	// 1.3.4.7 The RulesEngine shall validate that all words formed exist in the
	// dictionary (validateMove).
	// 1.3.4.8 The RulesEngine shall validate all cross-words created by the
	// placement (getAllWordsFormed + validateMove).
	// 1.3.4.10 If validation fails, validateMove shall return false and no state
	// mutation shall occur.
	// 1.3.4.11 If validation succeeds, validateMove shall return true and the
	// GameController shall proceed with scoring and state updates.
	public boolean validateMove(GameState state, List<PlacedTile> placedTiles, WordValidation dictionary) {
		if (state == null || placedTiles == null || placedTiles.isEmpty() || dictionary == null) {
			return false;
		}

		Board board = state.getBoard();
		// 1.3.4.4 The RulesEngine shall validate that all tiles are within board
		// boundaries (validateSquaresAvailable).
		// 1.3.4.6 The RulesEngine shall validate that all placed tiles are on
		// unoccupied squares (validateSquaresAvailable).
		if (!validateSquaresAvailable(board, placedTiles))
			return false;
		if (!validateStraightLine(placedTiles))
			return false;
		if (!validateContinuous(board, placedTiles))
			return false;
		if (!validateFirstMoveCenter(board, placedTiles))
			return false;
		if (!validateConnected(board, placedTiles))
			return false;

		Board tempBoard = copyBoard(board);

		for (PlacedTile placed : placedTiles) {
			tempBoard.placeTile(placed.getRow(), placed.getCol(), placed.getTile());
		}

		List<String> words = getAllWordsFormed(tempBoard, placedTiles);

		boolean foundWord = false;

		for (String word : words) {
			if (word.length() > 1) {
				foundWord = true;

				if (!dictionary.isValidWord(word)) {
					return false;
				}
			}
		}

		return foundWord;
	}

	private boolean validateSquaresAvailable(Board board, List<PlacedTile> placedTiles) {
		for (PlacedTile placed : placedTiles) {
			int row = placed.getRow();
			int col = placed.getCol();

			if (!board.isValidPosition(row, col)) {
				return false;
			}

			if (!board.isEmptyAt(row, col)) {
				return false;
			}
		}

		return true;
	}

	private boolean validateStraightLine(List<PlacedTile> placedTiles) {
		boolean sameRow = true;
		boolean sameCol = true;

		int baseRow = placedTiles.get(0).getRow();
		int baseCol = placedTiles.get(0).getCol();

		for (PlacedTile placed : placedTiles) {
			if (placed.getRow() != baseRow)
				sameRow = false;
			if (placed.getCol() != baseCol)
				sameCol = false;
		}

		return sameRow || sameCol;
	}

	private boolean validateContinuous(Board board, List<PlacedTile> placedTiles) {
		Board tempBoard = copyBoard(board);

		for (PlacedTile placed : placedTiles) {
			tempBoard.placeTile(placed.getRow(), placed.getCol(), placed.getTile());
		}

		boolean sameRow = true;
		boolean sameCol = true;

		int baseRow = placedTiles.get(0).getRow();
		int baseCol = placedTiles.get(0).getCol();

		for (PlacedTile placed : placedTiles) {
			if (placed.getRow() != baseRow)
				sameRow = false;
			if (placed.getCol() != baseCol)
				sameCol = false;
		}

		if (!sameRow && !sameCol) {
			return false;
		}

		if (sameRow) {
			int minCol = Integer.MAX_VALUE;
			int maxCol = Integer.MIN_VALUE;

			for (PlacedTile placed : placedTiles) {
				minCol = Math.min(minCol, placed.getCol());
				maxCol = Math.max(maxCol, placed.getCol());
			}

			for (int col = minCol; col <= maxCol; col++) {
				if (tempBoard.isEmptyAt(baseRow, col)) {
					return false;
				}
			}

		} else {
			int minRow = Integer.MAX_VALUE;
			int maxRow = Integer.MIN_VALUE;

			for (PlacedTile placed : placedTiles) {
				minRow = Math.min(minRow, placed.getRow());
				maxRow = Math.max(maxRow, placed.getRow());
			}

			for (int row = minRow; row <= maxRow; row++) {
				if (tempBoard.isEmptyAt(row, baseCol)) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean validateFirstMoveCenter(Board board, List<PlacedTile> placedTiles) {
		if (!board.isEmpty()) {
			return true;
		}

		for (PlacedTile placed : placedTiles) {
			if (placed.getRow() == 7 && placed.getCol() == 7) {
				return true;
			}
		}

		return false;
	}

	private boolean validateConnected(Board board, List<PlacedTile> placedTiles) {
		if (board.isEmpty()) {
			return true;
		}

		for (PlacedTile placed : placedTiles) {
			int row = placed.getRow();
			int col = placed.getCol();

			if ((row > 0 && !board.isEmptyAt(row - 1, col)) || (row < Board.SIZE - 1 && !board.isEmptyAt(row + 1, col))
					|| (col > 0 && !board.isEmptyAt(row, col - 1))
					|| (col < Board.SIZE - 1 && !board.isEmptyAt(row, col + 1))) {
				return true;
			}
		}

		return false;
	}

	// 1.3.4.8 The RulesEngine shall validate all cross-words created by the
	// placement (getAllWordsFormed + validateMove).
	// 1.3.4.9 The RulesEngine shall construct all words formed from the placement
	// for validation (getAllWordsFormed).
	private List<String> getAllWordsFormed(Board board, List<PlacedTile> placedTiles) {
		Set<String> words = new HashSet<>();

		boolean sameRow = true;
		boolean sameCol = true;

		int baseRow = placedTiles.get(0).getRow();
		int baseCol = placedTiles.get(0).getCol();

		for (PlacedTile placed : placedTiles) {
			if (placed.getRow() != baseRow)
				sameRow = false;
			if (placed.getCol() != baseCol)
				sameCol = false;
		}

		PlacedTile first = placedTiles.get(0);

		if (sameRow) {
			String mainWord = buildWord(board, first.getRow(), first.getCol(), 0, 1);

			if (mainWord.length() > 1) {
				words.add(mainWord);
			}

			for (PlacedTile placed : placedTiles) {
				String crossWord = buildWord(board, placed.getRow(), placed.getCol(), 1, 0);

				if (crossWord.length() > 1) {
					words.add(crossWord);
				}
			}

		} else if (sameCol) {
			String mainWord = buildWord(board, first.getRow(), first.getCol(), 1, 0);

			if (mainWord.length() > 1) {
				words.add(mainWord);
			}

			for (PlacedTile placed : placedTiles) {
				String crossWord = buildWord(board, placed.getRow(), placed.getCol(), 0, 1);

				if (crossWord.length() > 1) {
					words.add(crossWord);
				}
			}
		}

		return new ArrayList<>(words);
	}

	private String buildWord(Board board, int row, int col, int dRow, int dCol) {
		int currentRow = row;
		int currentCol = col;

		while (board.isValidPosition(currentRow - dRow, currentCol - dCol)
				&& !board.isEmptyAt(currentRow - dRow, currentCol - dCol)) {
			currentRow -= dRow;
			currentCol -= dCol;
		}

		StringBuilder word = new StringBuilder();

		while (board.isValidPosition(currentRow, currentCol) && !board.isEmptyAt(currentRow, currentCol)) {
			word.append(board.getCell(currentRow, currentCol));
			currentRow += dRow;
			currentCol += dCol;
		}

		return word.toString();
	}

	private Board copyBoard(Board board) {
		Board copy = new Board();

		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				Square original = board.getSquare(row, col);

				if (original.isOccupied()) {
					copy.placeTile(row, col, original.getTile());
				}
			}
		}

		return copy;
	}
}