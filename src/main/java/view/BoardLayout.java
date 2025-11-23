package main.java.view;


import main.java.model.Board;
import main.java.model.Tile;
import main.java.controller.BoardController;
import javax.swing.*;
import java.awt.*;


public class BoardLayout extends JPanel {
    private Board board;
    private final int rows;
    private final int cols;
    private final BoardController boardController = new BoardController();


    public BoardLayout(Board board) {

        //using setter for input checks
        setBoard(board);
        this.rows = boardController.getRows(board);
        this.cols = boardController.getCols(board);

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
                t = boardController.getTiles(board)[i][j]; //get tile from board controller
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

    /**
     * getters & setters
     **/
    public void setBoard(Board board) {
        if (board == null)
            throw new IllegalArgumentException("Board cannot be null");
        this.board = board;
    }
}

