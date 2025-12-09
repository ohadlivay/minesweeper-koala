package main.java.view;


import main.java.controller.GameSessionController;
import main.java.model.Board;
import main.java.model.MinesLeftListener;
import main.java.model.Tile;
import main.java.model.TurnListener;

import javax.swing.*;
import java.awt.*;


public class BoardLayout extends JPanel implements TurnListener, MinesLeftListener {
    private final int rows;
    private final int cols;
    private final TileView[][] tileViewGrid; // to hold references to TileView components
    private final GameSessionController gameSessionController = GameSessionController.getInstance(); //changed to singleton -ohad
    private Board board; //only use for getters
    private static final int boardSize = 450;

    private Color tileColor;

    public BoardLayout(Board board) {

        //using setter for input checks
        setBoard(board);
        this.rows = board.getRows(); //changed these two to interact with model directly as a getter.
        this.cols = board.getCols();
        this.tileViewGrid = new TileView[rows][cols];
        this.tileColor = randomColor();

        initBoardPanel();
        populateBoard();
    }


    //Initialize the board panel
    private void initBoardPanel() {
        setBackground(ColorsInUse.BOARD_BACKGROUND.get());
        setLayout(new GridLayout(rows, cols));
        board.setTurnListener(this); // could this be the responsibility of the controller?

        //calculation for the tile size to dynamically change per difficulty
        //divide the board size by the number of rows
        int calculatedTileSize = boardSize / rows;
        //recalculate board size to make avoid gaps if the division output wasn't an int
        int finalBoardSize = calculatedTileSize * rows;
        int borderSize = 10;
        setMaximumSize(new Dimension(finalBoardSize + borderSize, finalBoardSize + borderSize));
        this.updateTurn();
    }

    //Populate the board with the tiles,
    private void populateBoard() {

        removeAll();

        int calculatedTileSize = boardSize / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile t = null;
                t = board.getTiles()[i][j]; //get tile from board controller
                if (t != null) {
                    tileViewGrid[i][j] = new TileView(t, calculatedTileSize, tileColor);
                    add(tileViewGrid[i][j]);
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
            //This turn
            setBorder(BorderFactory.createMatteBorder(
                    5,5,5,5,
                    ColorsInUse.BOARD_ACTIVE_BORDER.get())   // translucent yellow
            );
            updateTileEnabledState(true);
        } else {
            //not this turn
            setBorder(BorderFactory.createMatteBorder(
                    5,5,5,5,
                    ColorsInUse.BOARD_BORDER_DEFAULT.get()
            )); // translucent black

            updateTileEnabledState(false);
        }
        repaint();
        revalidate();
    }

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


    @Override
    public void updateMinesLeft(int minesLeft) {
        /*
        whoever implements the mines left in this view;
        rest assured that this method will be used whenever minesLeft is updated.
        you just have to implement the gui thingies
         */
        return;
    }

    //Helper methods

    //get random color
    private boolean isTooSimilar(Color c1, Color c2) {
        float dr = c1.getRed() / 255f - c2.getRed() / 255f;
        float dg = c1.getGreen() / 255f - c2.getGreen() / 255f;
        float db = c1.getBlue() / 255f - c2.getBlue() / 255f;

        float distance = (float) Math.sqrt(dr*dr + dg*dg + db*db);
        return distance < 0.40f;
    }

    private boolean isTooBrightOrDark(Color c) {
        float r = c.getRed() / 255f;
        float g = c.getGreen() / 255f;
        float b = c.getBlue() / 255f;

        float brightness = (r + g + b) / 3f;
        return brightness < 0.25f || brightness > 0.80f;
    }

    private Color randomColor() {
        Color toReturn = new Color(
                (float) Math.random(),
                (float) Math.random(),
                (float) Math.random()
        );

        for (ColorsInUse color : ColorsInUse.values()) {
            Color reserved = color.get();

            // reject if EXACT match or TOO SIMILAR match or TOO BRIGHT or TOO DARK
            if (toReturn.equals(reserved) || isTooSimilar(toReturn, reserved) || isTooBrightOrDark(toReturn)) {
                return randomColor(); // regenerate
            }
        }

        return toReturn;
    }
}

