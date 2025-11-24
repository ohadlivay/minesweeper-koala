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
    private final GameDifficulty gameDifficulty;

    private Board(GameDifficulty gameDifficulty) {
        this.PK = RANDOM.nextInt(99999999); //not even sure we need this
        this.tiles = new Tile[gameDifficulty.getRows()][gameDifficulty.getCols()];
        this.minesLeft = gameDifficulty.getMineCount();
        this.gameDifficulty = gameDifficulty;
        this.tiles = populateBoard(gameDifficulty);
    }

    private Tile[][] populateBoard() {

        /*
        this create a temporary, low weight and high performance board generator.
        since some generations can be faulty, this keeps that process light weight before we actually make the board.
         */
        BoardGenerator boardGenerator = new BoardGenerator(this.gameDifficulty);
        // Tile[][] tiles = boardGenerator.generateValidBoard(42); // 
        Tile[][] tiles = new Tile[1][1]; //temp till logic completes. tom please provide public method for initializing tiles.
        return tiles;
    }
    //factory design pattern
    //anyone can use this method and they are guaranteed a valid board with all tiles.
    public static Board createNewBoard(GameDifficulty gameDifficulty){
        Board board = new Board(gameDifficulty);
        board.populateBoard();
        return board;
    }
}
