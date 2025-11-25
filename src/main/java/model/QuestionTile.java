package main.java.model;
// Question tile that extends the abstract SpecialTile class
public class QuestionTile extends SpecialTile
{
    // Constructors

    public QuestionTile()
    {
        super();
    }

    @Override
    public void initiate(GameDifficulty gameDifficulty)
    {
        setUsed();
    }

    @Override
    public String toString() {
        return "Q";
    }
}
