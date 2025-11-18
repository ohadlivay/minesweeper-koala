package main.java.model;

//SurpriseTile class that extends the abstract SpecialTile class
public class SurpriseTile extends SpecialTile
{
    // Constructors

    public SurpriseTile(double x, double y, boolean isFlagged, boolean isRevealed, boolean isActivated, int activationCost)
    {
        super(x, y, isFlagged, isRevealed, isActivated, activationCost);
    }

    public SurpriseTile(double x, double y, boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(x, y, isFlagged, isRevealed, activationCost);
    }

    public SurpriseTile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        super(x, y, isFlagged, isRevealed);
    }

    public SurpriseTile(double x, double y, boolean isActivated, int activationCost)
    {
        super(x, y, isActivated, activationCost);
    }

    public SurpriseTile(double x, double y, int activationCost)
    {
        super(x, y, activationCost);
    }

    public SurpriseTile(double x, double y)
    {
        super(x, y);
    }

    public SurpriseTile(boolean isFlagged, boolean isRevealed, boolean isActivated, int activationCost)
    {
        super(isFlagged, isRevealed, isActivated, activationCost);
    }

    public SurpriseTile(boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(isFlagged, isRevealed, activationCost);
    }

    public SurpriseTile(boolean isFlagged, boolean isRevealed)
    {
        super(isFlagged, isRevealed);
    }

    public SurpriseTile(boolean isActivated, int activationCost)
    {
        super(isActivated, activationCost);
    }

    public SurpriseTile(int activationCost)
    {
        super(activationCost);
    }

    public SurpriseTile()
    {
        super();
    }
}
