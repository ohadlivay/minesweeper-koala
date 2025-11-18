package main.java.model;
//Tile class for the minesweeper game
public class Tile
{
    //Coordinates of the tile
    private double x;
    private double y;

    //Indicators of whether the tile is flagged or revealed
    private boolean isFlagged;
    private boolean isRevealed;


    //Constructors

    public Tile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Invalid coordinates");
        this.x = x;
        this.y = y;
        this.isFlagged = isFlagged;
        this.isRevealed = isRevealed;
    }

    public Tile(double x, double y) {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Invalid coordinates");
        this.isFlagged = false;
        this.isRevealed = false;
        this.x = x;
        this.y = y;
    }

    public Tile(boolean isFlagged, boolean isRevealed)
    {
        this.isFlagged = isFlagged;
        this.isRevealed = isRevealed;
        this.x = 0;
        this.y = 0;
    }

    public Tile()
    {
        this.isFlagged = false;
        this.isRevealed = false;
        this.x = 0;
        this.y = 0;
    }

    //Getters and setters for the tile class
    public boolean isFlagged()
    {
        return isFlagged;
    }

    public void setFlagged(boolean flagged)
    {
        isFlagged = flagged;
    }

    public boolean isRevealed()
    {
        return isRevealed;
    }

    public void setRevealed(boolean revealed)
    {
        isRevealed = revealed;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        if (x < 0)
            throw new IllegalArgumentException("Invalid coordinates");
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        if (y < 0)
            throw new IllegalArgumentException("Invalid coordinates");
        this.y = y;
    }

    //Methods for the tile class

    //Reveals the tile if it is not revealed already
    public void reveal()
    {
        if (!isRevealed&&!isFlagged)
            isRevealed = true;
        else
            throw new IllegalMoveException("reveal");
    }

    //Flags the tile if it is not flagged already
    public void flag()
    {
        if (!isFlagged&&!isRevealed)
            isFlagged = true;
        else
            throw new IllegalMoveException("flag");
    }

    //Unflags the tile if it is flagged already
    public void unflag()
    {
        if (isFlagged&&!isRevealed)
            isFlagged = false;
        else
            throw new IllegalMoveException("unflag");
    }
}
