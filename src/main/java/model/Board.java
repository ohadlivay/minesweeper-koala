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

    private Board(){
        this.PK = RANDOM.nextInt(99999999);
        this.tiles = new Tile[10][10];
    }

    private void populateBoard(Difficulty difficulty){
        //need GameSession to send me the difficulty enum

        /*
        this create a temporary, low weight and high performance board generator.
        since some generations can be faulty, this keeps that process light weight before we actually make the board.
         */
        BoardGenerator boardGenerator = new BoardGenerator(difficulty);
        boardGenerator.getValidBoard(int seed);