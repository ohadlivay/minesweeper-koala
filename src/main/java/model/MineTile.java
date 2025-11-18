package main.java.model;
// Represents a mine tile on the board
public class MineTile extends Tile
{
    // Constructors

    public MineTile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        super(x, y, isFlagged, isRevealed);
    }

    public MineTile(double x, double y)
    {
        super(x, y);
    }

    public MineTile(boolean isFlagged, boolean isRevealed)
    {
        super(isFlagged, isRevealed);
    }

    public MineTile()
    {
        super();
    }

}
