/* Asserted imports for this class:
java.util.Random
 */

package main.java.model;
import java.util.Random;

public class Board {

    private final int PK;
    private int minesLeft;
    private Tile[][] tiles;
    private static final Random RANDOM = new Random();
    private Difficulty difficulty;

    private Board(Difficulty difficulty) {
        this.PK = RANDOM.nextInt(99999999); //not even sure we need this
        this.tiles = new Tile[difficulty.getRows()][difficulty.getCols()];
        this.minesLeft = difficulty.getMineCount();
        this.difficulty = difficulty;
        this.tiles = populateBoard();
    }

    private Tile[][] populateBoard() {

        /*
        this create a temporary, low weight and high performance board generator.
        since some generations can be faulty, this keeps that process light weight before we actually make the board.
         */
        BoardGenerator boardGenerator = new BoardGenerator(difficulty);
        boardGenerator.generateValidBoard(42);

       int rows = difficulty.getRows();
       int cols = difficulty.getCols();

       Tile[][] tiles = new Tile[1][1]; //temp till logic completes
        return tiles;
    }
}