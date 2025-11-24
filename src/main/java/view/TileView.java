package main.java.view;

import main.java.model.Tile;

import javax.swing.*;
import java.awt.*;

public class TileView extends JButton implements TileListener {
    private final Tile tile;

    public TileView(Tile tile) {
        this.tile = tile;
        initTile();
        tile.addListener(this);
    }

    private void initTile() {
        setText(tile.toString());
        setPreferredSize(new Dimension(40, 40)); // tweak size as you like
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
    }

    public void update() {
        refresh();
    }


    //temporary method for showing if the tile is flagged, revealed or nothing
    private void refresh() {
        if (tile.isFlagged()) {
            setText("F");
        } else if (tile.isRevealed()) {
            setText("R");
        } else {
            setText("");
        }
    }


}
