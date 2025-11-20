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
}
