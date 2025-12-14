package main.java.model;

public interface ActionMadeListener {
    void onActionMade(String message,boolean positive, int healthChange, int pointsChange);
}
