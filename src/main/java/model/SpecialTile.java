package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Cost of activating the tile
    protected final int activationCost;
    // Difficulty of the game for calculating activation cost
    protected final GameDifficulty gameDifficulty;
    // Indicates whether the tile has been initiated or not
    protected boolean isUsed;


    // Constructors

    public SpecialTile(GameDifficulty gameDifficulty)
    {
        super();
        if (gameDifficulty == null)
            throw new IllegalArgumentException("Difficulty cannot be null");
        this.gameDifficulty = gameDifficulty;
        this.activationCost = gameDifficulty.getActivationCost();
        this.isUsed = false;
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

    public boolean isUsed()
    {
        return isUsed;
    }

    protected void setUsed()
    {
        isUsed = true;
    }


    // Methods for the special tile class
    public abstract void initiate();

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

        // --- 2. Test Case: Null Parameter (Accept either exception or successful construction) ---
        try {
            SpecialTile s = null;
            try {
                s = new SurpriseTile(null);
                // If construction succeeds, ensure gameDifficulty is set to something non-null
                if (s.getGameDifficulty() == null) {
                    return false;
                }
            } catch (IllegalArgumentException expected) {
                // Acceptable: implementation may reject null difficulty
            } catch (Exception e) {
                // Unexpected exception type
                return false;
            }

            // --- 3. Test Case: Successful Construction and behavior ---
            SpecialTile valid = new SurpriseTile(GameDifficulty.EASY);

            // Verify the gameDifficulty field
            if (valid.getGameDifficulty() != GameDifficulty.EASY) {
                return false;
            }

            // Verify activationCost field
            if (valid.getActivationCost() != GameDifficulty.EASY.getActivationCost()) {
                return false;
            }

            // isUsed should be false initially
            if (valid.isUsed()) {
                return false;
            }

            // toggle used flag via protected setter and verify
            valid.setUsed();
            if (!valid.isUsed()) {
                return false;
            }

            // initiate should be callable and not throw
            try {
                valid.initiate();
            } catch (Exception e) {
                return false;
            }

        } catch (Exception e) {
            // Test Failure: Unexpected exception during tests
            return false;
        }

        // If all tests pass
        return true;
    }


}
