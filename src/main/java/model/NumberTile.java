package main.java.model;

/**
 * Represents a tile that indicates the number of mines in adjacent squares.
 * This is the standard safe tile in Minesweeper.
 */
public class NumberTile extends Tile {

    /** Number of mines in the 8 adjacent cells. */
    private int adjacentMines;

    // Constructors

    public NumberTile() {
        super();
    }

    // Getters and setters for the NumberTile class
    /**
     * Gets the count of adjacent mines.
     * 
     * @return The number of neighboring mines.
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    void setAdjacentMines(int adjacentMines) {
        if (adjacentMines < 0 || adjacentMines > 8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    /**
     * Increments the mine neighbor count.
     * Used during board generation when placing mines.
     * 
     * @throws IllegalArgumentException if the count exceeds the maximum of 8.
     */
    void addMineNeighbor() {
        if (adjacentMines < 8)
            this.adjacentMines++;
        else
            throw new IllegalArgumentException("Invalid number of adjacent mines");
    }

    @Override
    public String toString() {
        return adjacentMines + "";
    }

    /**
     * Runs internal tests to verify the integrity of the NumberTile logic.
     * Checks initial values, boundary conditions for adjacent mines (0-8), and
     * error handling.
     * 
     * @return true if all tests pass, false otherwise.
     */
    @Override
    public boolean runClassTests() {
        try {
            // ensure parent tests pass
            if (!super.runClassTests())
                return false;

            // default should be 0
            NumberTile nt = new NumberTile();
            if (nt.getAdjacentMines() != 0)
                return false;

            // valid range 0..8 via setter
            for (int v = 0; v <= 8; v++) {
                nt.setAdjacentMines(v);
                if (nt.getAdjacentMines() != v)
                    return false;
            }

            // setter rejects values outside 0..8
            boolean threw = false;
            try {
                nt.setAdjacentMines(-1);
            } catch (IllegalArgumentException ex) {
                threw = true;
            }
            if (!threw)
                return false;

            threw = false;
            try {
                nt.setAdjacentMines(9);
            } catch (IllegalArgumentException ex) {
                threw = true;
            }
            if (!threw)
                return false;

            // addMineNeighbor increments correctly up to 8, then throws
            nt.setAdjacentMines(6);
            nt.addMineNeighbor(); // -> 7
            if (nt.getAdjacentMines() != 7)
                return false;
            nt.addMineNeighbor(); // -> 8
            if (nt.getAdjacentMines() != 8)
                return false;

            threw = false;
            try {
                nt.addMineNeighbor(); // should throw when >8
            } catch (IllegalArgumentException ex) {
                threw = true;
            }
            return threw;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Strategy method: Determines if this tile stops the recursive clearing of
     * tiles.
     * Number tiles with > 0 adjacent mines stop the cascade. Empty tiles (0 mines)
     * allow it to continue.
     */
    @Override
    public boolean stopsExpansion() {
        // Rule: If it's a number > 0, stop the spread.
        return getAdjacentMines() > 0;
    }

    /**
     * Strategy method: Determines if this tile can be safely revealed.
     * Number tiles are always safe to reveal (unlike mines).
     */
    @Override
    public boolean isRevealable() {
        // Rule: Numbers are always safe to reveal.
        return true;
    }
}
