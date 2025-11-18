package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Cost of activating the tile
    private int activationCost;


    // Constructors


    public SpecialTile(double x, double y, boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(x, y, isFlagged, isRevealed);
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        super(x, y, isFlagged, isRevealed);
        this.activationCost = 0;
    }

    public SpecialTile(double x, double y, int activationCost)
    {
        super(x, y);
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(double x, double y)
    {
        super(x, y);
        this.activationCost = 0;
    }

    public SpecialTile(boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(isFlagged, isRevealed);
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(boolean isFlagged, boolean isRevealed)
    {
        super(isFlagged, isRevealed);
        this.activationCost = 0;
    }

    public SpecialTile(int activationCost)
    {
        super();
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile()
    {
        super();
        this.activationCost = 0;
    }

    public void setActivationCost(int activationCost)
    {
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }
}
