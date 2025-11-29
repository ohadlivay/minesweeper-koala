package main.java.view;

import main.java.controller.GameSessionController;
import main.java.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TileView extends JButton implements RevealListener, FlagListener {
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
        tile.setRevealListener(this);
        tile.setFlagListener(this);

    }

    private void mouseClicked() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameSessionController==null) return;

                if (SwingUtilities.isRightMouseButton(e)) {
                    gameSessionController.tileRightClick(tile); // i didnt work on this yet -ohad
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    gameSessionController.tileLeftClick(tile); //cascade works
                }

               /* if (tile.isFlagged()) {
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
                }*/


                repaint();
                revalidate();
            }
        });
    }

    @Override
    public void updateRevealed() {

        if (tile.toString().equals("M")) {
            setBackground(Color.GREEN);
            System.out.println("tileview: i got updated that tile was a mine");
        }
        else
            setBackground(Color.BLUE);
        setEnabled(false);
        setText(tile.toString());
        System.out.println("tileview: i got updated that tile was Revealed");
    }

    @Override
    public void updateFlagged() {

        setBackground(Color.RED);
        setText("F");
        System.out.println("tileview: i got updated that tile was flagged");

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
