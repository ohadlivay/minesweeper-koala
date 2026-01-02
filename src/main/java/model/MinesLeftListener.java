package main.java.model;

// Listener for updating the number of mines left
public interface MinesLeftListener {
    // Called when the number of mines left changes
    void updateMinesLeft(int minesLeft, Board board);
}
