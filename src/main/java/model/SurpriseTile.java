package main.java.model;

//SurpriseTile class that extends the abstract SpecialTile class
public class SurpriseTile extends SpecialTile
{
    // Constructors

    public SurpriseTile(GameDifficulty gameDifficulty)
    {
        super(gameDifficulty);
    }

    @Override
    public void initiate()
    {
        setUsed();
    }


}
