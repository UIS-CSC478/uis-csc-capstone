package csc478.group2;

// 1.3.5 Scoring Requirements
// The ScoreCalculator class is responsible for determining the total score of a turn by evaluating all words formed from a placement.

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 1.3.5.0 All scoring shall be handled by ScoreCalculator.
// 1.3.5.9 Scoring logic shall be deterministic and independent of the UI layer.
public class ScoreCalculator {
	// 1.3.5.1 The ScoreCalculator shall calculate the total score for a submitted
	// turn using calculateTurnScore(GameState, List<PlacedTile>).
	// 1.3.5.3 The ScoreCalculator shall compute each word score by summing the
	// point values of all tiles in that word.
	public int calculateTurnScore(GameState state, List<PlacedTile> placedTiles) {
		Board board = state.getBoard();

		List<WordData> words = getWordsFormed(board, placedTiles);

		int total = 0;

		for (WordData word : words) {
			int wordScore = 0;
			int wordMultiplier = 1;

			// 1.3.5.4 The ScoreCalculator shall apply letter bonuses and word bonuses only
			// to newly placed tiles on unused bonus squares.
			// 1.3.5.5 The ScoreCalculator shall prevent duplicate word scoring by tracking
			// already-scored word positions.
			for (Cell cell : word.cells) {
				Square square = board.getSquare(cell.row, cell.col);
				Tile tile = square.getTile();

				int letterScore = tile.getPointValue();

				if (isNewTile(placedTiles, cell.row, cell.col) && !square.isBonusUsed()) {
					switch (square.getBonusType()) {
					case DOUBLE_LETTER -> letterScore *= 2;
					case TRIPLE_LETTER -> letterScore *= 3;
					case DOUBLE_WORD -> wordMultiplier *= 2;
					case TRIPLE_WORD -> wordMultiplier *= 3;
					default -> {
					}
					}
				}

				wordScore += letterScore;
			}

			total += wordScore * wordMultiplier;
		}
		// 1.3.5.6 The ScoreCalculator shall add a 50-point bonus when all seven rack
		// tiles are used in one turn.
		if (placedTiles.size() == 7) {
			total += 50;
		}
		// 1.3.5.7 The ScoreCalculator shall consume used bonus squares after scoring so
		// bonuses apply only once.
		for (PlacedTile placed : placedTiles) {
			Square square = board.getSquare(placed.getRow(), placed.getCol());
			if (!square.isBonusUsed()) {
				square.consumeBonus();
			}
		}
		// 1.3.5.8 The ScoreCalculator shall return the total score for the move.
		return total;
	}

	// 1.3.5.2 The ScoreCalculator shall identify all words formed by the move using
	// getWordsFormed(Board, List<PlacedTile>).
	private List<WordData> getWordsFormed(Board board, List<PlacedTile> placedTiles) {
		List<WordData> words = new ArrayList<>();
		Set<String> seen = new HashSet<>();

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
			WordData mainWord = buildWord(board, first.getRow(), first.getCol(), 0, 1);
			addWordIfValid(words, seen, mainWord);

			for (PlacedTile placed : placedTiles) {
				WordData crossWord = buildWord(board, placed.getRow(), placed.getCol(), 1, 0);
				addWordIfValid(words, seen, crossWord);
			}

		} else if (sameCol) {
			WordData mainWord = buildWord(board, first.getRow(), first.getCol(), 1, 0);
			addWordIfValid(words, seen, mainWord);

			for (PlacedTile placed : placedTiles) {
				WordData crossWord = buildWord(board, placed.getRow(), placed.getCol(), 0, 1);
				addWordIfValid(words, seen, crossWord);
			}
		}

		return words;
	}

	private void addWordIfValid(List<WordData> words, Set<String> seen, WordData word) {
		if (word == null || word.word.length() < 2) {
			return;
		}

		String key = word.getKey();

		if (!seen.contains(key)) {
			seen.add(key);
			words.add(word);
		}
	}

	private WordData buildWord(Board board, int row, int col, int dRow, int dCol) {
		int currentRow = row;
		int currentCol = col;

		while (board.isValidPosition(currentRow - dRow, currentCol - dCol)
				&& !board.isEmptyAt(currentRow - dRow, currentCol - dCol)) {
			currentRow -= dRow;
			currentCol -= dCol;
		}

		StringBuilder word = new StringBuilder();
		List<Cell> cells = new ArrayList<>();

		while (board.isValidPosition(currentRow, currentCol) && !board.isEmptyAt(currentRow, currentCol)) {

			word.append(board.getCell(currentRow, currentCol));
			cells.add(new Cell(currentRow, currentCol));

			currentRow += dRow;
			currentCol += dCol;
		}

		return new WordData(word.toString(), cells);
	}

	private boolean isNewTile(List<PlacedTile> placedTiles, int row, int col) {
		for (PlacedTile placed : placedTiles) {
			if (placed.getRow() == row && placed.getCol() == col) {
				return true;
			}
		}
		return false;
	}

	private static class WordData {
		private final String word;
		private final List<Cell> cells;

		private WordData(String word, List<Cell> cells) {
			this.word = word;
			this.cells = cells;
		}

		private String getKey() {
			StringBuilder key = new StringBuilder();

			for (Cell cell : cells) {
				key.append(cell.row).append(",").append(cell.col).append(";");
			}

			return key.toString();
		}
	}

	private static class Cell {
		private final int row;
		private final int col;

		private Cell(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
}