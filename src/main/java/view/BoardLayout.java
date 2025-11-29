package main.java.view;


import main.java.controller.GameSessionController;
import main.java.model.Board;
import main.java.model.Tile;

import javax.swing.*;
import java.awt.*;


public class BoardLayout extends JPanel{
    private final int rows;
    private final int cols;
    private final GameSessionController gameSessionController = GameSessionController.getInstance(); //changed to singleton -ohad
    private Board board; //only use for getters

    public BoardLayout(Board board) {

        //using setter for input checks
        setBoard(board);
        this.rows = board.getRows(); //changed these two to interact with model directly as a getter.
        this.cols = board.getCols();

        initBoardPanel();
        populateBoard();
    }


    //Initialize the board panel
    private void initBoardPanel() {
        setLayout(new GridLayout(rows, cols));
        setBorder(new javax.swing.border.LineBorder(
                new java.awt.Color(220, 220, 17, 255), 2, true));
        /**Q: does this board know if he's the left or right one? maybe the border should be changed by the game session controller?**/
    }

    //Populate the board with the tiles,
    private void populateBoard() {

        removeAll();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile t = null;
                t = board.getTiles()[i][j]; //get tile from board controller
                if (t != null) {
                    add(new TileView(t));
                } else {
                    add(new JButton()); // placeholder button for debugging
                }
            }
        }

        revalidate();
        repaint();
    }

    /**
     * getters & setters
     **/
    public void setBoard(Board board) {
        if (board == null)
            throw new IllegalArgumentException("Board cannot be null");
        this.board = board;
    }
}

