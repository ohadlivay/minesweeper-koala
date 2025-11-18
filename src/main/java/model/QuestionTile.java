package main.java.model;
// Question tile that extends the abstract SpecialTile class
public class QuestionTile extends SpecialTile
{
    // Constructors

    public QuestionTile(double x, double y, boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(x, y, isFlagged, isRevealed, activationCost);
    }

    public QuestionTile(double x, double y, boolean isFlagged, boolean isRevealed)
    {
        super(x, y, isFlagged, isRevealed);
    }

    public QuestionTile(double x, double y, int activationCost)
    {
        super(x, y, activationCost);
    }

    public QuestionTile(double x, double y)
    {
        super(x, y);
    }


    public QuestionTile(boolean isFlagged, boolean isRevealed, int activationCost)
    {
        super(isFlagged, isRevealed, activationCost);
    }

    public QuestionTile(boolean isFlagged, boolean isRevealed)
    {
        super(isFlagged, isRevealed);
    }

    public QuestionTile(int activationCost)
    {
        super(activationCost);
    }

    public QuestionTile()
    {
        super();
    }

}
