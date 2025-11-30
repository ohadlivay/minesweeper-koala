package main.java.view;


import main.java.controller.GameSessionController;
import main.java.model.Board;
import main.java.model.Tile;
import main.java.model.TurnListener;

import javax.swing.*;
import java.awt.*;


public class BoardLayout extends JPanel implements TurnListener {
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
        board.setTurnListener(this);
        if (board.getTurn()) {
            setBorder(BorderFactory.createMatteBorder(
                    5, 5, 5, 5,
                    new Color(255, 255, 0, 150)   // translucent yellow
            ));
        } else {
            setBorder(BorderFactory.createMatteBorder(
                    5,5,5,5,
                    new Color(0, 0, 0, 150)
            )); // translucent black
        }
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

    @Override
    public void updateTurn() {
        if (board.getTurn()) {
            setBorder(BorderFactory.createMatteBorder(
                    5,5,5,5,
                    new Color(255, 255, 0, 150)   // translucent yellow
            ));
        } else {
            setBorder(BorderFactory.createMatteBorder(
                    5,5,5,5,
                    new Color(0, 0, 0, 150)
            )); // translucent black
        }
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

