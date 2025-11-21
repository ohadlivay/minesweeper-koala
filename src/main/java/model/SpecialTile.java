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

    @Override
    public boolean runClassTests()
    {
        // Run parent tests first
        if (!super.runClassTests()) {
            return false;
        }

        // Check that the constructor rejects null difficulty
        try {
            // instantiate an anonymous subclass to exercise the abstract class constructor
            SpecialTile s = new SpecialTile(null) { };
            // If we get here, no exception was thrown — test fails
            return false;
        } catch (IllegalArgumentException expected) {
            // expected behavior
        } catch (Exception e) {
            // unexpected exception type
            return false;
        }

        // Verify activationCost matches the difficulty's activation cost
        if (this.gameDifficulty == null) {
            return false; // should not happen for a correctly constructed instance
        }
        if (this.activationCost != this.gameDifficulty.getActivationCost()) {
            return false;
        }

        return true;
    }


}
