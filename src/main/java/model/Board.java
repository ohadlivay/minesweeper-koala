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
        this.tiles = populateBoard();
    }

    private Tile[][] populateBoard() {

        /*
        create temporary, low weight and high performance board generator.
        since some generations can be faulty, this keeps that process light weight before we actually construct Tiles
         */
        BoardGenerator boardGenerator = new BoardGenerator(this.gameDifficulty);
        //
        return boardGenerator.generateValidBoard(42); //currently set at 42 for testing
    }
    //factory design pattern
    //anyone can use this method and they are guaranteed a valid board with all tiles
    //contemplating merging this into c'tor since logic isnt really that complicated in the end
    public static Board createNewBoard(GameDifficulty gameDifficulty){
        Board board = new Board(gameDifficulty);
        board.populateBoard();
        return board;
    }

    boolean reveal(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.reveal();
        return activated;
    }
    boolean flag(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.flag();
        return activated;
    }
    boolean unflag(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.unflag();
        return activated;
    }

// for controller use
    public Tile[][] getTiles() {
        return tiles;
    }
    public int getRows() {
        return (tiles == null) ? 0 : tiles.length;
    }

    public int getCols() {
        return (tiles == null || tiles.length == 0) ? 0 : tiles[0].length;
    }
}
