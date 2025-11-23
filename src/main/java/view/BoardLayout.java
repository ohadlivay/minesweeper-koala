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

    public void setTiles() {
        this.tiles =  BoardController.getBoard();
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

    private void initBoardPanel() {
        setLayout(new GridLayout(rows, cols));
        setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(187, 187, 29), 2, true));
    }

    private void populateBoard() {
        for (int i = 0; i<getRows(); i++)
            for (int j = 0; j<getCols(); j++)
                TileButton tileBtn = new TileButton(tiles[i][j]);
                 add(tileBtn);

    }
}


