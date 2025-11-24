package main.java.view.overlays;

import main.java.controller.NavigationController;

import javax.swing.*;
/* An abstract class representing an overlay.
 * It provides helper methods to open and close the overlay.
 * It also allows setting a listener to handle actions when the overlay is closed.
 */
public abstract class OverlayView extends JDialog {

    public OverlayView(NavigationController nav) {
        super(nav.getVisFrame(), true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void open() {
        this.setVisible(true);
    }
}
