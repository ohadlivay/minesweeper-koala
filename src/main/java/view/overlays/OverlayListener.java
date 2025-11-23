package main.java.view.overlays;

/**
 * This interface will be used by overlays to notify the controller that it was closed.
 * This is important for separating the overlay logic from the controller logic.
 */
public interface OverlayListener {
    void actionOnClose(OverlayView overlay);
}
