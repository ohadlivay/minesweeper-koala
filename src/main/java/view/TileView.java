package main.java.view;

import main.java.controller.GameSessionController;
import main.java.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TileView extends JButton{
    private final Tile tile;
    private final GameSessionController gameSessionController;

    public TileView(Tile tile) {
        this.tile = tile;
        initTile();
        mouseClicked();
        this.gameSessionController = GameSessionController.getInstance();
    }

    private void initTile() {
        setPreferredSize(new Dimension(40, 40)); // tweak size as you like
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
    }

    private void mouseClicked() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameSessionController==null) return;

                if (SwingUtilities.isRightMouseButton(e)) {
                    gameSessionController.tileRightClick(tile);
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    gameSessionController.tileLeftClick(tile);
                }

                if (tile.isFlagged()) {
                    setBackground(Color.RED);
                    setText("F");
                }

                else if (tile.isRevealed()) {
                    setBackground(Color.BLUE);
                    setEnabled(false);
                    setText(tile.toString());
                }
                else {
                    setText("");
                    setBackground(null);
                }


                repaint();
                revalidate();
            }
        });
    }
    //temporary method for getting the tile's state from the model
    /* @Override
    public void update() {
        if (tile.isFlagged()) {
            setText("F");
        } else if (tile.isRevealed()) {
            setText("R");
        } else {
            setText("");
        }
    }*/



}
