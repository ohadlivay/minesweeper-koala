package main.java.model;

// Interface for updating the flagged state of a tile
public interface FlagListener {
    // Called when the flagged state of a tile is updated
    void updateFlagged(boolean flagged);
}
