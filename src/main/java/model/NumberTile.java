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
    public int getAdjacentMines()
    {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines)
    {
        if (adjacentMines < 0||adjacentMines>8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    @Override
    public boolean runClassTests()
    {
        try {
            // run parent tests first
            if (!super.runClassTests()) return false;

            // the default value should be 0
            NumberTile nt = new NumberTile();
            if (nt.getAdjacentMines() != 0) return false;

            // valid range 0..8
            for (int v = 0; v <= 8; v++) {
                nt.setAdjacentMines(v);
                if (nt.getAdjacentMines() != v) return false;
            }

            // invalid values should throw
            try { nt.setAdjacentMines(-1); return false; } catch (IllegalArgumentException ignored) {}
            try { nt.setAdjacentMines(9); return false; } catch (IllegalArgumentException ignored) {}

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
