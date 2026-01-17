package main.java.model;

import main.java.test.Testable;

/**
 * Tile represents a single square on the Minesweeper board.
 * It holds the state of the tile (flagged, revealed, activated) and its relationships
 * to the board and UI listeners.
 * 
 * Implements Testable interface for unit testing support.
 */
public class Tile implements Testable {

    // ==================================================================================
    // FIELDS
    // ==================================================================================

    // Indicators of whether the tile is flagged or revealed
    private boolean isFlagged;
    private boolean isRevealed;

    // Indicator of whether the tile has been activated (clicked or interacted with)
    private boolean isActivated;

    // Parent board reference
    private Board parentBoard;

    // UI Listeners
    private RevealListener revealListener;
    private FlagListener flagListener;

    // ==================================================================================
    // CONSTRUCTOR
    // ==================================================================================

    /**
     * Creates a new, empty Tile.
     * Initializes state to unflagged, unrevealed, and unactivated.
     */
    public Tile() {
        this.isFlagged = false;
        this.isRevealed = false;
        this.isActivated = false;
        this.parentBoard = null;
    }

    // ==================================================================================
    // ACCESSORS (GETTERS & SETTERS)
    // ==================================================================================

    public Board getParentBoard() {
        return this.parentBoard;
    }

    public void setParentBoard(Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    // Duplicate getter for legacy support
    public boolean getIsFlagged() {
        return this.isFlagged;
    }

    /**
     * Sets the flagged state of the tile.
     * Activates the tile and notifies listeners.
     * 
     * @param isFlagged true to flag, false to unflag
     */
    public void setIsFlagged(boolean isFlagged) {
        activate();
        this.isFlagged = isFlagged;

        System.out.println("Updating flagged tile view");
        if (flagListener != null) {
            flagListener.updateFlagged(isFlagged);
        }
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    // Duplicate getter for legacy support
    public boolean getIsRevealed() {
        return this.isRevealed;
    }

    /**
     * Sets the revealed state of the tile.
     * Activates the tile and notifies listeners.
     * 
     * @param isRevealed true to reveal
     * @return true (always)
     */
    public boolean setIsRevealed(boolean isRevealed) {
        activate();
        this.isRevealed = isRevealed;
        if (revealListener != null) {
            revealListener.updateRevealed();
        }
        return true;
    }

    public boolean isActivated() {
        return isActivated;
    }

    // ==================================================================================
    // LISTENER MANAGEMENT
    // ==================================================================================

    public void setFlagListener(FlagListener flagListener) {
        this.flagListener = flagListener;
    }

    public void setRevealListener(RevealListener revealListener) {
        this.revealListener = revealListener;
    }

    // ==================================================================================
    // GAME LOGIC & BEHAVIOR
    // ==================================================================================

    /**
     * Activates the tile if it is not activated already.
     * This marks the tile as having been interacted with.
     */
    private void activate() {
        isActivated = true;
    }

    /**
     * Forces the tile to be revealed without standard interaction checks.
     */
    protected void forceReveal() {
        setIsRevealed(true);
    }

    /**
     * Strategy: Should the cascade STOP spreading after reaching this tile?
     * Default logic: false (allows propagation unless overridden).
     * 
     * @return true if expansion should stop here.
     */
    public boolean stopsExpansion() {
        return false;
    }

    /**
     * Strategy: Is this tile allowed to be revealed in a cascade?
     * Default: false (hidden/mines are not revealable by standard cascade).
     * 
     * @return true if this tile can be auto-revealed.
     */
    public boolean isRevealable() {
        return false;
    }

    // ==================================================================================
    // OVERRIDES (TOSTRING & TESTABLE)
    // ==================================================================================

    @Override
    public String toString() {
        return "T";
    }

    // Tests the tile class
    @Override
    public boolean runClassTests() {
        return true;
    }
}
