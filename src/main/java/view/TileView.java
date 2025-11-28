package main.java.view;

import main.java.controller.BoardsController;
import main.java.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TileView extends JButton {
    private final Tile tile;
    private final BoardsController boardController;

    public TileView(Tile tile) {
        this.tile = tile;
        initTile();
        mouseClicked();
        this.boardController = BoardsController.getInstance();
    }

    private void initTile() {
        setPreferredSize(new Dimension(40, 40));
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
    }

    private void mouseClicked() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boardController==null) return;

                if (SwingUtilities.isRightMouseButton(e)) {
                    boardController.tileRightClick(tile);
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    boardController.tileLeftClick(tile);
                }
            }
        });
    }
    //temporary method for getting the tile's state from the model
    public void update() {
        if (tile.isFlagged()) {
            setText("F");
        } else if (tile.isRevealed()) {
            setText("R");
        } else {
            setText("");
        }
    }



}
