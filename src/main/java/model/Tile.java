package main.java.model;

import main.java.test.Testable;

//Tile class for the minesweeper game
public class Tile implements Testable
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

    //Tests the tile class
    @Override
    public boolean runClassTests()
    {
        try {
            Tile t = new Tile();
            // defaults
            if (t.isFlagged() || t.isRevealed() || t.isActivated()) return false;
            if (t.getX() != 0 || t.getY() != 0) return false;

            // invalid coordinates should throw
            try { t.setX(-1); return false; } catch (IllegalArgumentException ignored) {}
            try { t.setY(-1); return false; } catch (IllegalArgumentException ignored) {}

            // flag / unflag behavior
            Tile t2 = new Tile();
            t2.flag();
            if (!t2.isFlagged() || !t2.isActivated()) return false;
            try { t2.flag(); return false; } catch (IllegalMoveException ignored) {}
            t2.unflag();
            if (t2.isFlagged()) return false;
            try { t2.unflag(); return false; } catch (IllegalMoveException ignored) {}

            // reveal / activate behavior
            Tile t3 = new Tile();
            t3.reveal();
            if (!t3.isRevealed() || t3.isActivated()) return false;
            try { t3.reveal(); return false; } catch (IllegalMoveException ignored) {}
            try { t3.flag(); return false; } catch (IllegalMoveException ignored) {}

            t3.activate();
            if (!t3.isActivated()) return false;
            try { t3.activate(); return false; } catch (IllegalMoveException ignored) {}

            // coordinates consistency
            double[] coords = t3.getCoordinates();
            if (coords.length != 2) return false;
            return coords[0] == t3.getX() && coords[1] == t3.getY();
        } catch (Exception e) {
            return false;
        }
    }
//hey tom i added this for testing visually, you can remove it or change it however u want -ohad
    @Override
    public String toString(){
        return "T";
    }
}
