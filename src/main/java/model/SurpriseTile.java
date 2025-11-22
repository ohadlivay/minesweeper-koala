package main.java.model;

//SurpriseTile class that extends the abstract SpecialTile class
public class SurpriseTile extends SpecialTile
{
    // Constructors

    public SurpriseTile(GameDifficulty gameDifficulty)
    {
        super(gameDifficulty);
        if (super.getAdjacentMines()>0)
            throw new IllegalArgumentException("Surprise tile cannot have adjacent mines");
    }

    // Tests the SurpriseTile class
    @Override
    public boolean runClassTests()
    {
        try {
            // ensure parent tests pass
            if (!super.runClassTests()) return false;

            // construct with a normal difficulty and verify adjacent mines == 0
            SurpriseTile s1 = new SurpriseTile(GameDifficulty.EASY);
            if (s1.getAdjacentMines() != 0) return false;

            boolean threw = false;
            SurpriseTile s2 = null;
            try {
                s2 = new SurpriseTile(null);
            } catch (IllegalArgumentException | NullPointerException ex) {
                threw = true;
            }
            if (!threw) {
                return s2.getAdjacentMines() == 0;
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
