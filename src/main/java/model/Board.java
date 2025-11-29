// Java
package main.java.model;
import java.util.Random;

public class Board {

    private final int PK;
    private int minesLeft;
    private Tile[][] tiles;
    private static final Random RANDOM = new Random();
    private final GameDifficulty gameDifficulty;
    private boolean turn;

    private Board(GameDifficulty gameDifficulty) {
        this.PK = RANDOM.nextInt(99999999);
        this.tiles = new Tile[gameDifficulty.getRows()][gameDifficulty.getCols()];
        this.minesLeft = gameDifficulty.getMineCount();
        this.gameDifficulty = gameDifficulty;
        this.tiles = populateBoard();
        this.turn = false;
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

    protected boolean reveal(int r, int c)
    {
        Tile tile = tiles[r][c];
        boolean activated = tile.isActivated();
        tile.reveal();
        if (tile instanceof MineTile) minesLeft--;
        return activated;
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

    protected void cascade(int r, int c)
    {
        if (r < 0 || c < 0 || r >= getRows() || c >= getCols()) return;
        Tile tile = tiles[r][c];
        if (tile == null) return;

        if (tile.isRevealed() || tile.isFlagged() || tile instanceof MineTile) return;

        reveal(r, c);

        if (tile instanceof NumberTile && ((NumberTile) tile).getAdjacentMines() == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = r + dr;
                    int nc = c + dc;
                    if (nr < 0 || nc < 0 || nr >= getRows() || nc >= getCols()) continue;
                    Tile neigh = tiles[nr][nc];
                    if (neigh == null) continue;
                    if (neigh.isRevealed() || neigh.isFlagged() || neigh instanceof MineTile) continue;
                    cascade(nr, nc);
                }
            }
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
        this.turn = turn;
        return;
    }
}