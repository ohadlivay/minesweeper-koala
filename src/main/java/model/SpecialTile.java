package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Indicator of whether the tile has been activated
    private boolean isActivated;
    // Cost of activating the tile
    private int activationCost;


    // Constructors

    public SpecialTile(double x, double y, boolean isFlagged, boolean isRevealed, boolean isActivated, int activationCost)
    {
        super(x, y, isFlagged, isRevealed);
        this.isActivated = isActivated;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(double x, double y, boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(x, y, isFlagged, isRevealed);
        this.isActivated = false;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        super(x, y, isFlagged, isRevealed);
        this.isActivated = false;
        this.activationCost = 0;
    }

    public SpecialTile(double x, double y, boolean isActivated, int activationCost)
    {
        super(x, y);
        this.isActivated = isActivated;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(double x, double y, int activationCost)
    {
        super(x, y);
        this.isActivated = false;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(double x, double y)
    {
        super(x, y);
        this.isActivated = false;
        this.activationCost = 0;
    }

    public SpecialTile(boolean isFlagged, boolean isRevealed, boolean isActivated, int activationCost)
    {
        super(isFlagged, isRevealed);
        this.isActivated = isActivated;
        this.activationCost = activationCost;
    }

    public SpecialTile(boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(isFlagged, isRevealed);
        this.isActivated = false;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(boolean isFlagged, boolean isRevealed)
    {
        super(isFlagged, isRevealed);
        this.isActivated = false;
        this.activationCost = 0;
    }

    public SpecialTile(boolean isActivated, int activationCost)
    {
        this.isActivated = isActivated;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile(int activationCost)
    {
        super();
        this.isActivated = false;
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public SpecialTile()
    {
        super();
        this.isActivated = false;
        this.activationCost = 0;
    }


    //Getters and setters for the SpecialTile class
    public boolean isActivated()
    {
        return isActivated;
    }

    public int getActivationCost()
    {
        return activationCost;
    }

    public void setActivationCost(int activationCost)
    {
        if (activationCost < 0)
            throw new IllegalArgumentException("Invalid activation cost");
        this.activationCost = activationCost;
    }

    public void setActivated(boolean activated)
    {
        isActivated = activated;
    }

    //Activates the tile if it is not activated already
    public void activate()
    {
        if (!isActivated&&!super.isFlagged()&&!super.isRevealed())
            isActivated = true;
        else
            throw new IllegalMoveException("activate");
    }
}
