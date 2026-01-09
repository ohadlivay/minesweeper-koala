package main.java.model;

// Abstract class for special tiles
public abstract class SpecialTile extends NumberTile
{
    // Indicates whether the tile has been initiated or not
    protected boolean isUsed;
    private SpecialTileActivationListener listener;


    // Constructors

    public SpecialTile()
    {
        super();
        this.isUsed = false;
    }

    // Getters and setters
    public boolean isUsed()
    {
        return isUsed;
    }

    protected void setUsed()
    {
        isUsed = true;
        if(this.listener != null){
            listener.onSpecialTileActivated();
        }
    }


    public void setSpecialTileActivationListener(SpecialTileActivationListener specialTileActivationListener) {
        listener = specialTileActivationListener;
    }


}
