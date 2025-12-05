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
        this.gameSessionController = GameSessionController.getInstance();
        initTile();
        mouseClicked();

    }

    private void initTile() {
        setBackground(Color.darkGray);
        setPreferredSize(new Dimension(30, 30));
        setFocusPainted(false);
        setFont(new Font("Segoe UI Black", Font.BOLD, 12)); // need to change per difficulty
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
            java.net.URL bomb = getClass().getResource("/bomb.png");
            if (bomb != null) {
                ImageIcon icon = new ImageIcon(bomb);
                Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                ImageIcon bombIcon = new ImageIcon(scaledImage);
                setIcon(bombIcon);
                setDisabledIcon(bombIcon);
            }
            setEnabled(false);
        }
        else if (type.equals("S")) {
            java.net.URL surprise = getClass().getResource("/surprise.png");
            if (surprise != null) {
                ImageIcon icon = new ImageIcon(surprise);
                Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                ImageIcon surpriseIcon = new ImageIcon(scaledImage);
                setIcon(surpriseIcon);
            }
            setBackground(Color.YELLOW);
            setEnabled(true);
        }

        else if (type.equals("Q")) {
            setBackground(Color.GREEN);
            setText(type);
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
            java.net.URL flag = getClass().getResource("/red-flag.png");
            if (flag != null) {
                ImageIcon flagIcon = new ImageIcon(flag);
                Image scaledImage = flagIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledImage));
            }
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
}
