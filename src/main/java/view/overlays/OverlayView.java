package main.java.view.overlays;

import main.java.controller.NavigationController;

import javax.swing.*;
/* An abstract class representing an overlay.
 * It provides methods to open and close the overlay.
 * It also allows setting a listener to handle actions when the overlay is closed.
 */
public abstract class OverlayView extends JDialog {
    protected OverlayListener listener;
    protected NavigationController nav;

    public OverlayView(NavigationController nav) {
        super(nav.getVisFrame(), true);
        this.nav = nav;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public OverlayView() {
        this(null);
    }

    public void setListener(OverlayListener listener) {
        this.listener = listener;
    }

    public void open() {
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
        if (listener != null) {
            listener.actionOnClose(this);
        }
    }
}
