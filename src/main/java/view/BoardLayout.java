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
    private final TileView[][] tileViewGrid; // to hold references to TileView components
    private final GameSessionController gameSessionController = GameSessionController.getInstance(); //changed to singleton -ohad
    private Board board; //only use for getters
    private static final int boardSize = 450;

    private final Color tileColor;
    private final ComponentAnimator animator = new ComponentAnimator();

    public BoardLayout(Board board, Color color) {

        //using setter for input checks
        setBoard(board);
        this.rows = board.getRows(); //changed these two to interact with model directly as a getter.
        this.cols = board.getCols();
        this.tileViewGrid = new TileView[rows][cols];
        this.tileColor = color;

        initBoardPanel();
        populateBoard();
        this.updateTurn();
    }


    //Initialize the board panel
    private void initBoardPanel() {
        setBackground(ColorsInUse.BOARD_BACKGROUND.get());
        setLayout(new GridLayout(rows, cols));
        board.setTurnListener(this);

        //calculation for the tile size to dynamically change per difficulty
        //divide the board size by the number of rows
        int calculatedTileSize = boardSize / rows;
        //recalculate board size to make avoid gaps if the division output wasn't an int
        int finalBoardSize = calculatedTileSize * rows;
        int borderSize = 10;
        setMaximumSize(new Dimension(finalBoardSize + borderSize, finalBoardSize + borderSize));
        //this.updateTurn();
    }

    //Populate the board with the tiles,
    private void populateBoard() {

        removeAll();

        int calculatedTileSize = boardSize / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile t;
                t = board.getTiles()[i][j]; //get tile from board controller
                if (t != null) {
                    TileView tv = new TileView(t, calculatedTileSize, tileColor);
                    tileViewGrid[i][j] = tv;

                    // Wrap ONLY for paint-over effects
                    add(animator.withEffects(tv));   // <-- use your ComponentAnimator instance

                } else {
                    add(new JButton()); // placeholder button for debugging
                }
            }
        }

        revalidate();
        repaint();
    }

    //Update the board based on turn changes
    @Override
    public void updateTurn() {
        boolean turn = board.getTurn();

        setBorder(BorderFactory.createMatteBorder(
                5,5,5,5,
                ColorsInUse.getBoardBorderColor(turn))
        );
        updateTileEnabledState(turn);
        repaint();
        revalidate();
    }

    //Enable or disable tiles based on turn
    private void updateTileEnabledState(boolean turn) {
        for (TileView[] tileViews : tileViewGrid) {
            for (TileView tileView : tileViews) {
                if (tileView == null) continue;
                tileView.setTileTurn(turn);
            }
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

