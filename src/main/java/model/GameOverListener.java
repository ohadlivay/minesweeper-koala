package main.java.model;

// Listener interface for game over events
public interface GameOverListener {
    // Called when the game is over
    void onGameOver(boolean saved,boolean winOrLose ,int score);
}
