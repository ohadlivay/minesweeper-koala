package main.java.view;


import main.java.controller.BoardController;
import main.java.model.GameDifficulty;
import main.java.model.Tile;

import javax.swing.*;
import java.awt.*;


public class BoardLayout extends JPanel {
    private Tile[][] tiles;
    private final int rows;
    private final int cols;


    public BoardLayout(GameDifficulty difficulty) {
        this.rows = difficulty.getRows();
        this.cols = difficulty.getCols();

        setTiles();

        initBoardPanel();
        populateBoard();
    }

    private void initBoardPanel() {
        setLayout(new GridLayout(rows, cols));
        setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(220, 220, 17, 255), 2, true));
    }

    private void populateBoard() {
        removeAll();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile t = null;
                if (tiles != null && i < tiles.length && tiles[i] != null && j < tiles[i].length) {
                    t = tiles[i][j];
                }

                if (t != null) {
                    add(new TileButton(t));
                } else {
                    add(new JButton()); // placeholder button for debugging
                }
            }
        }

        revalidate();
        repaint();

    }

    //getters & setters

    public void setTiles() {
        Tile[][] board;
        try {
            board = BoardController.getBoard();
        } catch (Exception e) {
            // if retrieval failed, treat as empty board and deal later
            board = null;
        }

        if (board == null) {
            this.tiles = null;
            return;
        }

        this.tiles = board;     //need to make sure later to handle null board
                                //and matching or rows&cols to difficulty?
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}


