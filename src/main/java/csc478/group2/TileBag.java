package csc478.group2;

import java.util.Collections;
import java.util.List;

public class TileBag {
    private List<Tile> tiles;

    public TileBag() {
    	tiles = Tile.createTileBag();
        Collections.shuffle(tiles);
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    //unused 
//    public int size() {
//        return tiles.size();
//    }

    public Tile drawTile() {
        if (tiles.isEmpty()) {
            return null;
        }
        return tiles.remove(0);
    }
}