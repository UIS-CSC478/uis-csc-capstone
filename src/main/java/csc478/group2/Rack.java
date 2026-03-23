package csc478.group2;

import java.util.ArrayList;
import java.util.List;

public class Rack {
    private List<Tile> tiles;
    private Tile selectedTile;

    public Rack() {
        tiles = new ArrayList<>();
        selectedTile = null;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public boolean selectTile(int rackIndex) {
        if (rackIndex < 0 || rackIndex >= tiles.size()) {
            return false;
        }

        selectedTile = tiles.get(rackIndex);
        return true;
    }

    public void clearSelectedTile() {
        selectedTile = null;
    }

    public void refill(TileBag tileBag) {
        while (tiles.size() < 7 && !tileBag.isEmpty()) {
        	Tile drawnTile = tileBag.drawTile();
        	if (drawnTile != null) {
        	    tiles.add(drawnTile);
        	}
        }
    }

    public boolean hasSelectedTile() {
        return selectedTile != null;
    }

    public Tile removeSelectedTile() {
        if (selectedTile == null) {
            return null;
        }

        Tile tileToRemove = selectedTile;
        tiles.remove(selectedTile);
        selectedTile = null;
        return tileToRemove;
    }

    public void addTile(Tile tile) {
        if (tile != null) {
            tiles.add(tile);
        }
    }

}