package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Cost of activating the tile
    protected final int activationCost;
    // Difficulty of the game, for calculating activation cost
    protected final Difficulty difficulty;


    // Constructors

    public SpecialTile(double x, double y, boolean isFlagged, boolean isRevealed, Difficulty difficulty)
    {
        super(x, y, isFlagged, isRevealed);
        if (difficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null");
        this.difficulty = difficulty;
        this.activationCost = difficulty.getActivationCost();
    }

    public SpecialTile(double x, double y, Difficulty difficulty)
    {
        super(x, y);
        if (difficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null");
        this.difficulty = difficulty;
        this.activationCost = difficulty.getActivationCost();
    }

    public SpecialTile(boolean isFlagged, boolean isRevealed, Difficulty difficulty)
    {
        super(isFlagged, isRevealed);
        if (difficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null");
        this.difficulty = difficulty;
        this.activationCost = difficulty.getActivationCost();
    }

    public SpecialTile(Difficulty difficulty)
    {
        super();
        if (difficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null");
        this.difficulty = difficulty;
        this.activationCost = difficulty.getActivationCost();
    }

    // Getters and setters

    public int getActivationCost()
    {
        return activationCost;
    }


}
