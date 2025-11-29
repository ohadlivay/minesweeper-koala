// Java
package main.java.model;
import main.java.view.TurnListener;

import java.util.Random;

public class Board {

    private final int PK;
    private int minesLeft;
    private Tile[][] tiles;
    private static final Random RANDOM = new Random();
    private final GameDifficulty gameDifficulty;
    private boolean turn;
    private GameSession gameSession;
    private TurnListener turnListener;

    private Board(GameDifficulty gameDifficulty) {
        this.PK = RANDOM.nextInt(99999999);
        this.tiles = new Tile[gameDifficulty.getRows()][gameDifficulty.getCols()];
        this.minesLeft = gameDifficulty.getMineCount();
        this.gameDifficulty = gameDifficulty;
        this.tiles = populateBoard();
        this.turn = false;
        this.gameSession = GameSession.getInstance();
    }

    private Tile[][] populateBoard() {
        BoardGenerator boardGenerator = new BoardGenerator(this.gameDifficulty);
        int seed = RANDOM.nextInt(); // use a fresh random seed per board
        Tile[][] tiles = boardGenerator.generateValidBoard(seed);

        //give the tiles their parent board
        for(Tile[] row : tiles) {
            for(Tile tile : row) {
                tile.setParentBoard(this);
            }
        }
        return tiles;
    }

    // factory design pattern
    public static Board createNewBoard(GameDifficulty gameDifficulty){
        return new Board(gameDifficulty);
    }

    protected void reveal(Tile tile) {
        if(tile.isRevealed())
            return;
        if(tile.isFlagged())
            return;
        System.out.println("Revealing tile " + tile);

        if (tile instanceof MineTile){
            minesLeft--;
            tile.setIsRevealed(true);
        }
        if (tile instanceof NumberTile){
            if(((NumberTile) tile).getAdjacentMines() == 0){
                System.out.println("cascading " + tile);
                this.cascade(tile); //this should not call board.reveal!!
            }
            else{
                tile.setIsRevealed(true);
            }
        }
    }

    private int getTileRow(Tile tile) {
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                if (tiles[r][c].equals(tile)) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("Tile not found in grid");
    }

    private int getTileCol(Tile tile) {
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                if (tiles[r][c].equals(tile)) {
                    return c;
                }
            }
        }
        throw new IllegalArgumentException("Tile not found in grid");
    }

    private Tile getTileAt(int row, int col) {
        if (row < 0 || row >= tiles.length)
            throw new IndexOutOfBoundsException("Invalid row: " + row);

        if (col < 0 || col >= tiles[row].length)
            throw new IndexOutOfBoundsException("Invalid col: " + col);

        return tiles[row][col];
    }


    protected void revealAll()
    {
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getCols(); c++) {
                Tile tile = tiles[r][c];
                tile.forceReveal();
            }
        }
    }

    protected boolean flag(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.flag();
        return activated;
    }
    protected void unflag(int r, int c)
    {
        Tile tile = tiles[r][c];
        tile.unflag();
    }

    private void cascade(Tile tile) {
        Cascader casader = new Cascader(tile,this.tiles);
        for( Tile t : casader.getTilesToReveal()){
            t.setIsRevealed(true);
        }
    }
    protected boolean allMinesRevealed()
    {
        return minesLeft == 0;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public int getRows() {
        return (tiles == null) ? 0 : tiles.length;
    }

    public int getCols() {
        return (tiles == null || tiles.length == 0) ? 0 : tiles[0].length;
    }

    public boolean getTurn(){
        return this.turn;
    }
    public void setTurn(boolean turn){
        if (this.turn == turn) return;  // no state change

        this.turn = turn;
        if (turnListener != null) {
            turnListener.updateTurn();
        }
    }


    public void setTurnListener(TurnListener turnListener) {
        this.turnListener = turnListener;
    }

    private void setMinesLeft(int minesLeft){
        this.minesLeft = minesLeft;
    };

    private int getMinesLeft(){
        return this.minesLeft;
    }
}