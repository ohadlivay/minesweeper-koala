package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Cost of activating the tile
    protected final int activationCost;
    // Difficulty of the game for calculating activation cost
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

    public GameDifficulty getGameDifficulty()
    {
        return gameDifficulty;
    }

    // Class tests
    public boolean runClassTests()
    {
        // --- 1. Run Parent Tests ---
        try {
            if (!super.runClassTests()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        // --- 2. Test Case: Null Parameter (Failure Case) ---
        try {
            // We must use a concrete subclass to call the abstract class constructor
            SpecialTile s = new SurpriseTile(null);
            // Test Failure: Exception was NOT thrown
            return false;
        } catch (IllegalArgumentException expected) {
            // Test Success: Expected exception was thrown
        } catch (Exception e) {
            // Test Failure: Threw the wrong type of exception
            return false;
        }


        try {
            // Instantiate a new instance for the success test
            SpecialTile s = new SurpriseTile(GameDifficulty.EASY);

            // Verify the gameDifficulty field
            if (s.getGameDifficulty() != GameDifficulty.EASY) {
                return false;
            }

            // Verify activationCost field
            if (s.getActivationCost() != GameDifficulty.EASY.getActivationCost()) {
                return false;
            }
        } catch (Exception e) {
            // Test Failure: Unexpected exception during successful construction
            return false;
        }

        // If all tests pass
        return true;
    }


}
