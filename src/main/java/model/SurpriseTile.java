package main.java.model;

//SurpriseTile class that extends the abstract SpecialTile class
public class SurpriseTile extends SpecialTile
{
    // Constructors

    public SurpriseTile()
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
        return "S";
    }
}
