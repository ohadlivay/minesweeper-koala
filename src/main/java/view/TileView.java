package main.java.view;

import main.java.controller.GameSessionController;
import main.java.model.*;
import main.java.util.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TileView extends JButton implements RevealListener, FlagListener, SpecialTileActivationListener {
    private final Tile tile;
    private final GameSessionController gameSessionController;
    private final double dynamicSize; //dynamic tile size per difficulty
    private final int iconSize; //dynamic icon size per difficulty

    private Color tileColor;



    public TileView(Tile tile, int dynamicSize) {
        this.tile = tile;
        this.dynamicSize = dynamicSize;
        this.iconSize = (int) (dynamicSize * 0.75);
        this.gameSessionController = GameSessionController.getInstance();
        this.tileColor = ColorsInUse.TILE_DEFAULT.get();
        initTile();
        mouseClicked();
    }

    public TileView(Tile tile, int dynamicSize, Color tileColor) {
        this.tile = tile;
        this.dynamicSize = dynamicSize;
        this.iconSize = (int) (dynamicSize * 0.75);
        this.gameSessionController = GameSessionController.getInstance();
        this.tileColor = tileColor;
        initTile();
        mouseClicked();
    }

    private void initTile() {
        setBackground(tileColor);
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
        if (tile.getIsFlagged())
            setupIcon(null, false);
        if (type.equals("M")) {
            setupIcon("/bomb.png", true);
            setEnabled(false);
        }
        else if (type.equals("S")) {
            setupIcon("/surprise.png", false);
            setTileColor(ColorsInUse.SURPRISE_TILE.get());
            setEnabled(true);
        }

        else if (type.equals("Q")) {
            setupIcon("/question.png", false);
            setTileColor(ColorsInUse.QUESTION_TILE.get());
            setEnabled(true);
        }
        else
        {
            if (type.equals("0")) {
                setText("");
                setTileColor(ColorsInUse.REVEALED_BG.get());
            }
            else
            {
                setText(type);
                setTileColor(ColorsInUse.REVEALED_BG.get());
            }

            if (type.equals("1")) setForeground(ColorsInUse.NUMBER_1.get());
            else if (type.equals("2")) setForeground(ColorsInUse.NUMBER_2.get());
            else if (type.equals("3")) setForeground(ColorsInUse.NUMBER_3.get());
            else if (type.equals("4")) setForeground(ColorsInUse.NUMBER_4.get());
            else if (type.equals("5")) setForeground(ColorsInUse.NUMBER_5.get());
            else if (type.equals("6")) setForeground(ColorsInUse.NUMBER_6.get());
            else if (type.equals("7")) setForeground(ColorsInUse.NUMBER_7.get());
            else if (type.equals("8")) setForeground(ColorsInUse.NUMBER_8.get());
            setEnabled(false);
        }
        System.out.println("tileview: i got updated that tile was Revealed: " + type);
    }

    public void setTileColor(Color color) {
        tileColor = color;
        setBackground(tileColor);
    }


    // 2 methods only used to show if board is active or not, --dont set tileColor here!--
    protected void recolorTile() {
        setBackground(tileColor);
    }

    protected void uncolorTile() {
        setBackground(Color.black);
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
        setTileColor(Color.BLACK);
        setEnabled(false);
    }

    // changes on turn change to visibly show the active board
    public void setTileTurn(boolean turn) {
        if(tile==null)
            return;

        if (turn) {
            recolorTile();
        }
        else {
            uncolorTile();
        }
    }

    //helper method for resizing icons
    private void setupIcon(String resourcePath, boolean visDisabled) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            setIcon(null);
            return;
        }
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
