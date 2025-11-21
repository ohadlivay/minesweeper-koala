package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Cost of activating the tile
    protected final int activationCost;
    // Difficulty of the game, for calculating activation cost
    protected final GameDifficulty gameDifficulty;


    // Constructors

    public SpecialTile(GameDifficulty gameDifficulty)
    {
        super();
        if (gameDifficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null");
        this.gameDifficulty = gameDifficulty;
        this.activationCost = gameDifficulty.getActivationCost();
    }

    // Getters and setters

    public int getActivationCost()
    {
        return activationCost;
    }


}
