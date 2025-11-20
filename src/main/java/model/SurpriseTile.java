package main.java.model;

//SurpriseTile class that extends the abstract SpecialTile class
public class SurpriseTile extends SpecialTile
{
    // Constructors


    public SurpriseTile(double x, double y, boolean isFlagged, boolean isRevealed, Difficulty difficulty) {
        super(x, y, isFlagged, isRevealed, difficulty);
    }

    public SurpriseTile(double x, double y, Difficulty difficulty) {
        super(x, y, difficulty);
    }

    public SurpriseTile(boolean isFlagged, boolean isRevealed, Difficulty difficulty) {
        super(isFlagged, isRevealed, difficulty);
    }

    public SurpriseTile(Difficulty difficulty) {
        super(difficulty);
    }
}
