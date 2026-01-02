package main.java.model;

// Listener interface for actions made in the game
public interface ActionMadeListener {
    // Called when an action is made
    void onActionMade(String message,boolean positive, int healthChange, int pointsChange);
}
