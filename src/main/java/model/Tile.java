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

    //Indicator of whether the tile has been activated
    private boolean isActivated;


    //Constructors

    public Tile()
    {
        this.isFlagged = false;
        this.isRevealed = false;
        this.x = 0;
        this.y = 0;
        this.isActivated = false;
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

    public boolean isActivated()
    {
        return isActivated;
    }

    public void setActivated(boolean activated)
    {
        isActivated = activated;
    }

    //Methods for the tile class



    //Method to get the coordinates of the tile
    public double[] getCoordinates()
    {
        return new double[]{x,y};
    }

    //Reveals the tile if it is not revealed already
    public void reveal()
    {
        if (!isRevealed&&!isFlagged)
        {
            isRevealed = true;
            isActivated = false;
        }

        else
            throw new IllegalMoveException("reveal");
    }

    //Flags the tile if it is not flagged already
    public void flag()
    {
        if (!isFlagged&&!isRevealed)
        {
            isFlagged = true;
            isActivated = true;
        }
        else
            throw new IllegalMoveException("flag");
    }

    //Unflags the tile if it is flagged already
    public void unflag()
    {
        if (isFlagged&&!isRevealed)
        {
            isFlagged = false;
        }

        else
            throw new IllegalMoveException("unflag");
    }

    //Activates the tile if it is not activated already
    public void activate()
    {
        if (!isActivated&&!isFlagged&&isRevealed)
            isActivated = true;
        else
            throw new IllegalMoveException("activate");
    }
}
