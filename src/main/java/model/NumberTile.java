package main.java.model;
// Represents a tile with a number of adjacent mines
public class NumberTile extends Tile
{

    //Number of adjacent mines
    private int adjacentMines;


    // Constructors

    public NumberTile(double x, double y, boolean isFlagged, boolean isRevealed, int adjacentMines)
    {
        super(x, y, isFlagged, isRevealed);
        if (adjacentMines < 0||adjacentMines>8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    public NumberTile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        super(x, y, isFlagged, isRevealed);
        this.adjacentMines = 0;
    }

    public NumberTile(double x, double y, int adjacentMines)
    {
        super(x, y);
        if (adjacentMines < 0||adjacentMines>8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    public NumberTile(double x, double y)
    {
        super(x, y);
        this.adjacentMines = 0;
    }


    public NumberTile(boolean isFlagged, boolean isRevealed, int adjacentMines)
    {
        super(isFlagged, isRevealed);
        if (adjacentMines < 0||adjacentMines>8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    public NumberTile(boolean isFlagged, boolean isRevealed)
    {
        super(isFlagged, isRevealed);
        this.adjacentMines = 0;
    }

    public NumberTile(int adjacentMines)
    {
        super();
        if (adjacentMines < 0||adjacentMines>8)
            throw new IllegalArgumentException("Invalid number of adjacent mines");
        this.adjacentMines = adjacentMines;
    }

    public NumberTile()
    {
        super();
        this.adjacentMines = 0;
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
}
