package main.java.model;

import main.java.test.Testable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game board in Minesweeper.
 * Manages the grid of tiles, mine placement, game state (turn, mines left),
 * and handles core game logic such as revealing tiles and flagging.
 */
public class Board implements Testable {

    /** Unique identifier for this board instance. */
    private final int PK;

    /** Number of mines remaining to be found (or flagged). */
    private int minesLeft;

    /** 2D array representing the grid of tiles. */
    private Tile[][] tiles;

    private static final Random RANDOM = new Random();

    /** The difficulty level of the current game. */
    private final GameDifficulty gameDifficulty;

    /** Indicates if it is currently the player's turn to act. */
    private boolean turn;

    /** Reference to the active game session. */
    private GameSession gameSession;

    // -- Listeners for game events --
    private TurnListener turnListener;
    private MinesLeftListener minesLeftListener;

    /**
     * Private constructor to initialize the board.
     * Use {@link #createNewBoard(GameDifficulty)} to create instances.
     * 
     * @param gameDifficulty The difficulty level determining size and mine count.
     */
    private Board(GameDifficulty gameDifficulty) {
        this.PK = RANDOM.nextInt(99999999);
        this.tiles = new Tile[gameDifficulty.getRows()][gameDifficulty.getCols()];
        this.minesLeft = gameDifficulty.getMineCount();
        this.gameDifficulty = gameDifficulty;
        this.tiles = populateBoard();
        this.turn = false;
        this.gameSession = GameSession.getInstance();
    }

    /**
     * Generates and populates the board with tiles and mines based on difficulty.
     * 
     * @return A 2D array of populated Tile objects.
     */
    private Tile[][] populateBoard() {
        BoardGenerator boardGenerator = new BoardGenerator(this.gameDifficulty);
        int seed = RANDOM.nextInt(); // use a fresh random seed per board
        Tile[][] tiles = boardGenerator.generateValidBoard(seed);

        // give the tiles their parent board
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.setParentBoard(this);
            }
        }
        return tiles;
    }

    /**
     * Factory method to create a new initialized Board instance.
     * 
     * @param gameDifficulty The configuration for the board.
     * @return A new Board object.
     */
    public static Board createNewBoard(GameDifficulty gameDifficulty) {
        return new Board(gameDifficulty);
    }

    /**
     * Attempts to reveal a specific tile.
     * Handles mine detonation, empty tile cascading, and standard reveals.
     * 
     * @param tile The tile to reveal.
     * @return 1 if a single tile was revealed, >1 if a cascade occurred, 0 if
     *         nothing happened.
     */
    public int reveal(Tile tile) {
        if (tile.isRevealed())
            return 0;
        if (tile.isFlagged())
            return 0;
        System.out.println("Revealing tile " + tile);

        if (tile instanceof MineTile) {
            setMinesLeft(getMinesLeft() - 1);
            tile.setIsRevealed(true);
            return 1;
        }
        if (tile instanceof NumberTile) {
            if (((NumberTile) tile).getAdjacentMines() == 0) {
                System.out.println("cascading " + tile);
                return this.cascade(tile);
            } else {
                tile.setIsRevealed(true);
                return 1;
            }
        }
        return 0;
    }

    /**
     * Reveals every tile on the board.
     * Typically used when the game is over (win or loss) to show the full state.
     */
    protected void revealAll() {
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getCols(); c++) {
                Tile tile = this.getTiles()[r][c];
                tile.forceReveal();
                if (tile instanceof SpecialTile specialTile) {
                    specialTile.setUsed();
                }
            }
        }
    }

    /**
     * Reveals a single random unrevealed mine.
     * Used for hints or end-game sequences.
     */
    protected void revealRandomMine() {
        if (getMinesLeft() == 0) {
            System.out.println("No mines left to reveal");
            return;
        }
        int row, col;
        Tile tile;
        do {
            row = RANDOM.nextInt(getRows());
            col = RANDOM.nextInt(getCols());
            tile = this.getTiles()[row][col];
        } while (!(tile instanceof MineTile) || tile.isRevealed());
        System.out.println("Revealing random mine at (" + (row + 1) + "," + (col + 1) + ")");
        reveal(tile);

    }

    /**
     * Logic for a "lifeline" or special move that reveals a safe 3x3 grid area
     * if possible. It tries to find the best spot with the fewest unrevealed tiles
     * to maximize information gain or safety.
     */
    protected void revealGrid() {
        if (this.getCols() < 3 || this.getRows() < 3) {
            System.out.println("Cannot reveal grid, board too small");
            return;
        }
        // Find all possible 3x3 grids and count unrevealed tiles
        List<int[]> candidates = new ArrayList<>();
        int minUnrevealedCount = 0;
        for (int r = 0; r <= this.getRows() - 3; r++)
            for (int c = 0; c <= this.getCols() - 3; c++) {
                int unRevealedCount = 0;
                boolean hasRevealedTile = false;
                for (int i = 0; i < 3; i++)
                    for (int j = 0; j < 3; j++) {
                        Tile tile = this.getTiles()[r + i][c + j];
                        if (!tile.isRevealed()) {
                            unRevealedCount++;
                            hasRevealedTile = true;
                        }
                    }
                if (hasRevealedTile) {
                    candidates.add(new int[] { r, c, unRevealedCount });
                    minUnrevealedCount = Math.max(minUnrevealedCount, unRevealedCount);
                }
            }
        // Select optimal candidates with minimum unrevealed tiles
        if (candidates.isEmpty()) {
            System.out.println("No grid found");
            return;
        }
        List<int[]> optimalCandidates = new ArrayList<>();
        for (int[] candidate : candidates) {
            int unrevealed = candidate[2];
            if (unrevealed == minUnrevealedCount)
                optimalCandidates.add(candidate);
        }
        if (optimalCandidates.isEmpty())
            optimalCandidates = candidates;
        int randomIndex = RANDOM.nextInt(optimalCandidates.size());
        int[] optimalCandidate = optimalCandidates.get(randomIndex);
        int r = optimalCandidate[0];
        int c = optimalCandidate[1];
        System.out.println("Revealing " + minUnrevealedCount + " tiles in grid at (" + (r + 1) + "," + (c + 1) + ")");
        // Reveal all tiles in the grid
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Tile tile = this.getTiles()[r + i][c + j];
                if (tile instanceof MineTile && !tile.isRevealed())
                    setMinesLeft(getMinesLeft() - 1);
                tile.forceReveal();
            }

    }

    /**
     * Toggles the flag state of a tile.
     * Prevents flagging revealed tiles.
     * 
     * @param tile The tile to flag.
     */
    protected void flag(Tile tile) {
        if (tile.isRevealed()) {

            System.out.println("Cannot flag a revealed tile");
            return;
        }
        if (tile.isFlagged()) {
            System.out.println("Cannot flag a flagged tile, umflagging");
            this.unflag(tile);
            return;
        }
        if (tile instanceof MineTile) {
            System.out.println("Cannot flag a mine tile");
            reveal(tile);
            return;
        }
        System.out.println("Flagging tile: " + tile);

        tile.setIsFlagged(true);

    }

    /**
     * Removes the flag from a tile.
     * 
     * @param tile The tile to unflag.
     */
    protected void unflag(Tile tile) {
        System.out.println("Unflagging tile: " + tile);
        tile.setIsFlagged(false);
    }

    /**
     * Initiates a cascade reveal starting from the given tile.
     * This happens when an empty tile (0 adjacent mines) is revealed,
     * automatically revealing neighbors.
     * 
     * @param tile The starting tile.
     * @return The number of tiles revealed by the cascade.
     */
    private int cascade(Tile tile) {
        Cascader cascader = new Cascader(tile, this.tiles);
        int revealedCount = 0;
        for (Tile t : cascader.getTilesToReveal()) {
            t.setIsRevealed(true);
            revealedCount++;
        }
        return revealedCount;
    }

    /**
     * Checks if the win condition is met (all mines accounted for).
     * 
     * @return true if minesLeft is 0.
     */
    protected boolean allMinesRevealed() {
        return getMinesLeft() == 0;
    }

    // Getters and setters
    public Tile[][] getTiles() {
        return tiles;
    }

    public int getRows() {
        return (tiles == null) ? 0 : tiles.length;
    }

    public int getCols() {
        return (tiles == null || tiles.length == 0) ? 0 : tiles[0].length;
    }

    public boolean getTurn() {
        return this.turn;
    }

    public void setTurn(boolean turn) {
        if (this.getTurn() == turn)
            return; // no state change

        this.turn = turn;
        if (turnListener != null) {
            turnListener.updateTurn();
        }
    }

    public void setTurnListener(TurnListener turnListener) {
        this.turnListener = turnListener;
    }

    public void setMinesLeftListener(MinesLeftListener minesLeftListener) {
        this.minesLeftListener = minesLeftListener;
    }

    private void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
        if (minesLeftListener != null) {
            minesLeftListener.updateMinesLeft(getMinesLeft(), this);
        }
    }

    public int getMinesLeft() {
        return this.minesLeft;
    }

    @Override
    public boolean runClassTests() {
        return false;
    }
}