package main.java.model;

// Listener interface for input block events
public interface InputBlockListener {
    // Called when the input block state changes
    void onInputBlock(boolean isBlocked);
}
