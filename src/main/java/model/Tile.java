package main.java.model;

import main.java.test.Testable;

import java.util.ArrayList;
import java.util.List;

/*
tom i think might want to reconsider the access modifiers of the setters here (true for GameSession too)
and we need to re-evaluate the use of isActivated here, so it won't be confused with special tiles
-ohad
 */
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

    public boolean isRevealed()
    {
        return isRevealed;
    }

    double getX()
    {
        return x;
    }

    void setX(double x)
    {
        if (x < 0)
            throw new IllegalArgumentException("Invalid coordinates");
        this.x = x;
    }

    double getY()
    {
        return y;
    }

    void setY(double y)
    {
        if (y < 0)
            throw new IllegalArgumentException("Invalid coordinates");
        this.y = y;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    //Methods for the tile class


    //Method to get the coordinates of the tile
    double[] getCoordinates()
    {
        return new double[]{x,y};
    }

    //Reveals the tile if it is not revealed already
    public void reveal()
    {
        if (!isRevealed&&!isFlagged)
        {
            isRevealed = true;
            activate();
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
            activate();

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
    protected void activate()
    {
        isActivated = true;
    }

    /*observer pattern to notify TileView of changes
    * holds a list of all listeners that need to know when the tile state changes
     */

    //Tests the tile class
    @Override
    public boolean runClassTests()
    {
        try {
            Tile t = new Tile();
            // --- Default State Test ---
            // defaults: isFlagged, isRevealed, isActivated should be false
            if (t.isFlagged() || t.isRevealed() || t.isActivated()) return false;
            if (t.getX() != 0 || t.getY() != 0) return false;
            t.setX(10);
            t.setY(20);
            if (t.getX() != 10 || t.getY() != 20) return false;


            // --- Invalid Coordinate Test ---
            // invalid coordinates should throw
            try { t.setX(-1); return false; } catch (IllegalArgumentException ignored) {}
            try { t.setY(-1); return false; } catch (IllegalArgumentException ignored) {}


            // --- Flag / Unflag Behavior Test ---
            Tile t2 = new Tile();

            // Flagging should set isFlagged=true and isActivated=true
            t2.flag();
            if (!t2.isFlagged() || !t2.isActivated() || t2.isRevealed()) return false;

            // Flagging again when already flagged should throw IllegalMoveException
            try { t2.flag(); return false; } catch (IllegalMoveException ignored) {}

            // Unflagging should set isFlagged=false. isActivated should remain true.
            t2.unflag();
            if (t2.isFlagged() || !t2.isActivated() || t2.isRevealed()) return false;

            // Unflagging again when not flagged should throw IllegalMoveException
            try { t2.unflag(); return false; } catch (IllegalMoveException ignored) {}


            // --- Reveal Behavior Test ---
            Tile t3 = new Tile();

            // Revealing should set isRevealed=true and isActivated=true
            t3.reveal();
            if (!t3.isRevealed() || !t3.isActivated() || t3.isFlagged()) return false;

            // Revealing again when already revealed should throw IllegalMoveException
            try { t3.reveal(); return false; } catch (IllegalMoveException ignored) {}

            // Trying to flag a revealed tile should throw IllegalMoveException
            try { t3.flag(); return false; } catch (IllegalMoveException ignored) {}


            // --- Flag/Reveal Conflict Test ---
            Tile t4 = new Tile();
            t4.flag(); // t4 isFlagged=true, isActivated=true

            // Trying to reveal a flagged tile should throw IllegalMoveException
            try { t4.reveal(); return false; } catch (IllegalMoveException ignored) {}

            t4.unflag(); // t4 isFlagged=false, isActivated=true
            t4.reveal(); // t4 isRevealed=true, isActivated=true (still true)


            // --- Activate Method Consistency Test ---
            // The private activate() method just sets isActivated = true, it doesn't check state or throw.
            // Calling it again should have no effect, not throw an exception.
            t4.activate();
            if (!t4.isActivated()) return false; // Should still be true


            // --- Coordinates Consistency Test ---
            t4.setX(5.5);
            t4.setY(9.9);
            double[] coords = t4.getCoordinates();
            if (coords.length != 2) return false;
            return coords[0] == t4.getX() && coords[1] == t4.getY();

        } catch (Exception e) {
            // Catch any unexpected exceptions and fail the test
            return false;
        }
    }
//hey tom i added this for testing visually, you can remove it or change it however u want -ohad
    @Override
    public String toString(){
        return "T";
    }
}
