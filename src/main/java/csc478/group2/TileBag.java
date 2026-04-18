package csc478.group2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileBag {

    private final List<Tile> tiles;

    public TileBag() {
        this.tiles = createStandardBag();
        Collections.shuffle(this.tiles);
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    public int size() {
        return tiles.size();
    }

    public Tile drawTile() {
        if (tiles.isEmpty()) {
            return null;
        }
        return tiles.remove(0);
    }

    public void returnTile(Tile tile) {
        if (tile != null) {
            tiles.add(tile);
        }
    }

    public void returnTiles(List<Tile> returnedTiles) {
        if (returnedTiles == null) {
            return;
        }

        for (Tile tile : returnedTiles) {
            if (tile != null) {
                tiles.add(tile);
            }
        }
    }

    private List<Tile> createStandardBag() {
        List<Tile> bag = new ArrayList<>();

        addTiles(bag, 'A', 1, 9);
        addTiles(bag, 'B', 3, 2);
        addTiles(bag, 'C', 3, 2);
        addTiles(bag, 'D', 2, 4);
        addTiles(bag, 'E', 1, 12);
        addTiles(bag, 'F', 4, 2);
        addTiles(bag, 'G', 2, 3);
        addTiles(bag, 'H', 4, 2);
        addTiles(bag, 'I', 1, 9);
        addTiles(bag, 'J', 8, 1);
        addTiles(bag, 'K', 5, 1);
        addTiles(bag, 'L', 1, 4);
        addTiles(bag, 'M', 3, 2);
        addTiles(bag, 'N', 1, 6);
        addTiles(bag, 'O', 1, 8);
        addTiles(bag, 'P', 3, 2);
        addTiles(bag, 'Q', 10, 1);
        addTiles(bag, 'R', 1, 6);
        addTiles(bag, 'S', 1, 4);
        addTiles(bag, 'T', 1, 6);
        addTiles(bag, 'U', 1, 4);
        addTiles(bag, 'V', 4, 2);
        addTiles(bag, 'W', 4, 2);
        addTiles(bag, 'X', 8, 1);
        addTiles(bag, 'Y', 4, 2);
        addTiles(bag, 'Z', 10, 1);

        bag.add(new Tile('?', 0, true));
        bag.add(new Tile('?', 0, true));

        return bag;
    }

    private void addTiles(List<Tile> bag, char letter, int value, int count) {
        for (int i = 0; i < count; i++) {
            bag.add(new Tile(letter, value));
        }
    }
}