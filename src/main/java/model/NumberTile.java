package main.java.model;
// Represents a tile with a number of adjacent mines
public class NumberTile extends Tile
{

    //Number of adjacent mines
    private int adjacentMines;


    // Constructors

    public NumberTile()
    {
        super();
    }


    //Getters and setters for the NumberTile class
    int getAdjacentMines()
    {
        return adjacentMines;
    }

    void setAdjacentMines(int adjacentMines)
    {
        if (adjacentMines < 0||adjacentMines>8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    //Adds a mine neighbor to the tile
    void addMineNeighbor()
    {
        if (adjacentMines < 8)
            this.adjacentMines++;
        else
            throw new IllegalArgumentException("Invalid number of adjacent mines");
    }

    @Override
    public String toString()
    {
        return adjacentMines + "";
    }

    //Tests the NumberTile class
    @Override
    public boolean runClassTests()
    {
        try {
            // ensure parent tests pass
            if (!super.runClassTests()) return false;

            // default should be 0
            NumberTile nt = new NumberTile();
            if (nt.getAdjacentMines() != 0) return false;

            // valid range 0..8 via setter
            for (int v = 0; v <= 8; v++) {
                nt.setAdjacentMines(v);
                if (nt.getAdjacentMines() != v) return false;
            }

            // setter rejects values outside 0..8
            boolean threw = false;
            try {
                nt.setAdjacentMines(-1);
            } catch (IllegalArgumentException ex) {
                threw = true;
            }
            if (!threw) return false;

            threw = false;
            try {
                nt.setAdjacentMines(9);
            } catch (IllegalArgumentException ex) {
                threw = true;
            }
            if (!threw) return false;

            // addMineNeighbor increments correctly up to 8, then throws
            nt.setAdjacentMines(6);
            nt.addMineNeighbor(); // -> 7
            if (nt.getAdjacentMines() != 7) return false;
            nt.addMineNeighbor(); // -> 8
            if (nt.getAdjacentMines() != 8) return false;

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
}
