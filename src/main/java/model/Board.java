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

    // Fixed: safe accessor for tiles (return null when out of bounds)
    public Tile getTileAt(int r, int c)
    {
        if (!inBounds(r, c)) return null;
        return tiles[r][c];
    }

    // Fixed: central bounds check (true when inside bounds)
    private boolean inBounds(int r, int c)
    {
        return r >= 0 && c >= 0 && r < getRows() && c < getCols();
    }

    // RevealResult to return both previous activation state and the revealed tile
    public static class RevealResult {
        public final boolean wasActivated;
        public final Tile revealedTile;
        public RevealResult(boolean wasActivated, Tile revealedTile) {
            this.wasActivated = wasActivated;
            this.revealedTile = revealedTile;
        }
    }

    // New: FlagResult to return both previous activation state and the flagged tile
    public static class FlagResult {
        public final boolean wasActivated;
        public final Tile flaggedTile;
        public FlagResult(boolean wasActivated, Tile flaggedTile) {
            this.wasActivated = wasActivated;
            this.flaggedTile = flaggedTile;
        }
    }

    // Changed: reveal now returns RevealResult instead of boolean
    protected RevealResult reveal(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.reveal();
        if (tile instanceof MineTile) minesLeft--;
        return new RevealResult(activated, tile);
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

    // Changed: flag now returns FlagResult instead of boolean
    protected FlagResult flag(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.flag();
        return new FlagResult(activated, tile);
    }
    protected void unflag(int r, int c)
    {
        Tile tile = tiles[r][c];
        tile.unflag();
    }

    protected void cascade(int r, int c)
    {
        // bounds check
        if (!inBounds(r, c)) return;
        Tile tile = getTileAt(r, c);
        if (tile == null) return;

        // skip already activated, flagged or mine tiles
        if (tile.isRevealed() || tile.isFlagged() || tile instanceof MineTile) return;

        // reveal this tile (updates state and minesLeft via reveal helper)
        reveal(r, c);

        // only expand if it's a number tile with zero adjacent mines
        if (tile instanceof NumberTile && ((NumberTile) tile).getAdjacentMines() == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = r + dr;
                    int nc = c + dc;
                    if (!inBounds(nr, nc)) continue;
                    Tile neigh = getTileAt(nr, nc);
                    if (neigh == null) continue;
                    if (neigh.isRevealed() || neigh.isFlagged() || neigh instanceof MineTile) continue;
                    // recurse to reveal neighbor (cascade will reveal and expand further if needed)
                    cascade(nr, nc);
                }
            }
        }
    }
    protected boolean allMinesRevealed()
    {
        return minesLeft == 0;
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
