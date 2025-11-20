package main.java.model;
// Question tile that extends the abstract SpecialTile class
public class QuestionTile extends SpecialTile
{
    // Constructors


    public QuestionTile(double x, double y, boolean isFlagged, boolean isRevealed, Difficulty difficulty) {
        super(x, y, isFlagged, isRevealed, difficulty);
    }

    public QuestionTile(double x, double y, Difficulty difficulty) {
        super(x, y, difficulty);
    }

    public QuestionTile(boolean isFlagged, boolean isRevealed, Difficulty difficulty) {
        super(isFlagged, isRevealed, difficulty);
    }

    public QuestionTile(Difficulty difficulty) {
        super(difficulty);
    }
}
