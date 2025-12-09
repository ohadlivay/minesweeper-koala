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
    private final double dynamicSize; //dynamic tile size per difficulty
    private final int iconSize; //dynamic icon size per difficulty


    public TileView(Tile tile, int dynamicSize) {
        this.tile = tile;
        this.dynamicSize = dynamicSize;
        this.iconSize = (int) (dynamicSize * 0.75);
        this.gameSessionController = GameSessionController.getInstance();
        initTile();
        mouseClicked();
    }

    private void initTile() {
        setBackground(Color.darkGray);
        setPreferredSize(new Dimension((int)dynamicSize, (int)dynamicSize));
        setFocusPainted(false);
        setFont(new Font("Segoe UI Black", Font.BOLD, (int) (dynamicSize/2.5)));
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
        String type = tile.toString();
        if (type.equals("M")) {
            setupIcon("/bomb.png", true);
            setEnabled(false);
        }
        else if (type.equals("S")) {
            setupIcon("/surprise.png", false);
            setBackground(Color.YELLOW);
            setEnabled(true);
        }

        else if (type.equals("Q")) {
            setupIcon("/question.png", false);
            setBackground(Color.GREEN);
            setEnabled(true);
        }
        else
        {
            if (type.equals("0")) {
                setText("");
                setBackground(Color.black);
            }
            else
            {
                setText(type);
                setBackground(Color.black);
            }

            if (type.equals("1")) setForeground(Color.RED);
            else if (type.equals("2")) setForeground(new Color(0,128,0));
            else if (type.equals("3")) setForeground(Color.BLUE);
            else if (type.equals("4")) setForeground(Color.MAGENTA);
            else if (type.equals("5")) setForeground(Color.ORANGE);
            else if (type.equals("6")) setForeground(Color.CYAN);
            else if (type.equals("7")) setForeground(Color.PINK);
            else if (type.equals("8")) setForeground(Color.GRAY);
            setEnabled(false);
        }
        System.out.println("tileview: i got updated that tile was Revealed: " + type);
    }

    @Override
    public void updateFlagged(boolean flagged) {

        if (flagged) {
            setupIcon("/red-flag.png", false);
            System.out.println("tileview: i got updated that tile was flagged");
        }
        else {
            setIcon(null);
            setText("");
        }

    }

    @Override
    public void onSpecialTileActivated()
    {
        setBackground(Color.BLACK);
        setEnabled(false);
    }

    public void setTileTurn(boolean turn) {
        if(tile==null || !tile.isRevealed())
            return;

        //needs better implementation
        if (turn) {
            //this.setEnabled(true);
        }
        else {
            //this.setEnabled(false);
        }
    }

    //helper method for resizing icons
    private void setupIcon(String resourcePath, boolean visDisabled) {
        java.net.URL iconURL = getClass().getResource(resourcePath);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            setIcon(scaledIcon);

            //make sure only mine tile is still fully visible when the button is disabled
            if (visDisabled) {
                setDisabledIcon(scaledIcon);
            }
            else {
                setDisabledIcon(null);
            }
        }
    }
}
