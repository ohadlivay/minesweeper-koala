package main.java.model;

//Tile class for the minesweeper game
public class Tile {

    //Indicators of whether the tile is flagged or revealed
    private boolean isFlagged;
    private boolean isRevealed;

    //Indicator of whether the tile has been activated
    private boolean isActivated;

    private RevealListener revealListener;
    private FlagListener flagListener;

    private Board parentBoard;

    //Constructors

    public Tile()
    {
        this.isFlagged = false;
        this.isRevealed = false;
        this.isActivated = false;
        this.parentBoard = null;

    }

    //Getters and setters for the tile class
    public boolean isFlagged()
    {
        return isFlagged;
    }

    public boolean isRevealed()
    {
        return isRevealed;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    //Methods for the tile class


//    //Reveals the tile if it is not revealed already
//    public void reveal()
//    {
//        if (!isRevealed&&!isFlagged)
//        {
//            setIsRevealed(true);
//            activate();
//        }
//
//        else
//            throw new IllegalMoveException("reveal");
//    }
    protected void forceReveal()
    {
        setIsRevealed(true);
    }
/*
    //Flags the tile if it is not flagged already
    public void flag()
    {
        if (! (isFlagged || isRevealed) )
        {
            this.setIsFlagged(true);
            activate();
        }
        else
            throw new IllegalMoveException("flag");
    }

    //Unflags the tile if it is flagged already
    public void unflag()
    {
        if (isFlagged&&!isRevealed)
        {
            setIsFlagged(false);
        }

        else
            throw new IllegalMoveException("unflag");
    }

 */

    //Activates the tile if it is not activated already
    private void activate()
    {
        isActivated = true;
    }

    @Override
    public String toString(){
        return "T";
    }

    public Board getParentBoard()
    {
        return this.parentBoard;
    }

    public void setParentBoard(Board parentBoard)
    {
        this.parentBoard = parentBoard;
    }

    public boolean getIsRevealed(){
        return this.isRevealed;
    }
    public boolean setIsRevealed(boolean isRevealed){
        activate();
        this.isRevealed = isRevealed;
        if(revealListener != null) {
            revealListener.updateRevealed();
        }
        return true;
    }


    public boolean getIsFlagged(){
        return this.isFlagged;
    }

    public void setIsFlagged(boolean isFlagged){
        activate();
        this.isFlagged = isFlagged;

        System.out.println("Updating flagged tile view");
        if(flagListener != null) {
            flagListener.updateFlagged(isFlagged);
        }
    }

    public void setFlagListener(FlagListener flagListener) {
        this.flagListener = flagListener;
    }

    public void setRevealListener(RevealListener revealListener) {
        this.revealListener = revealListener;
    }
}
