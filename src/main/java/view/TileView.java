package main.java.view;

import main.java.controller.GameSessionController;
import main.java.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TileView extends JButton implements RevealListener, FlagListener, SpecialTileActivationListener {
    private final Tile tile;
    private final GameSessionController gameSessionController;

    public TileView(Tile tile) {
        this.tile = tile;
        initTile();
        mouseClicked();
        this.gameSessionController = GameSessionController.getInstance();
    }

    private void initTile() {
        setPreferredSize(new Dimension(30, 30));
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        tile.setRevealListener(this);
        tile.setFlagListener(this);
        if (tile instanceof SpecialTile)
            ((SpecialTile) tile).setSpecialTileActivationListener(this);

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

                repaint();
                revalidate();
            }
        });
    }

    @Override
    public void updateRevealed() {

        if (tile.toString().equals("M"))
        {
            setBackground(Color.GREEN);
            System.out.println("tileview: i got updated that tile was a mine");
        }
        else
        {
            setBackground(Color.BLUE);
        }
        setEnabled(false);
        if (tile.toString().equals("S"))
        {
            setBackground(Color.YELLOW);
            setEnabled(true);
        }
        setText(tile.toString());
        System.out.println("tileview: i got updated that tile was Revealed");
    }

    @Override
    public void updateFlagged(boolean flagged) {

        if (flagged) {
            setBackground(Color.RED);
            setText("F");
            System.out.println("tileview: i got updated that tile was flagged");
        }
        else {
            setBackground(null);
            setText("");
        }

    }


    @Override
    public void onSpecialTileActivated()
    {
        setBackground( Color.BLACK);
        setEnabled(false);
    }
}
