package csc478.group2;

// Class Description: The Rack class manages the current collection of tiles, allowing tiles to be added, removed, refilled from the tile bag, and accessed during gameplay.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rack {

	private static final int MAX_TILES = 7;

	private final List<Tile> tiles;

	public Rack() {
		this.tiles = new ArrayList<>();
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public boolean isFull() {
		return tiles.size() >= MAX_TILES;
	}

	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	public int size() {
		return tiles.size();
	}

	public void refill(TileBag tileBag) {
		while (tiles.size() < MAX_TILES && !tileBag.isEmpty()) {
			Tile drawn = tileBag.drawTile();
			if (drawn != null) {
				tiles.add(drawn);
			}
		}
	}

	public void addTile(Tile tile) {
		if (tile != null && tiles.size() < MAX_TILES) {
			tiles.add(tile);
		}
	}

	public boolean removeTile(Tile tile) {
		return tiles.remove(tile);
	}

	public Tile getTileAt(int index) {
		if (index < 0 || index >= tiles.size()) {
			return null;
		}
		return tiles.get(index);
	}

	public void clear() {
		tiles.clear();
	}

	public List<Tile> snapshot() {
		return new ArrayList<>(tiles);
	}

	public void restore(List<Tile> savedTiles) {
		tiles.clear();
		tiles.addAll(savedTiles);
	}

	public void sortAlphabetically() {
		Collections.sort(tiles, (a, b) -> Character.compare(a.getLetter(), b.getLetter()));
	}
}