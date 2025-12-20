package main.java.view.overlays;

import main.java.controller.NavigationController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Abstract class representing a modal overlay dialog in the application.
 * All specific overlays should extend this class.
 */

public abstract class OverlayView extends JDialog {

    protected final NavigationController nav;

    public OverlayView(NavigationController nav, boolean isModal) {
        super(nav.getVisFrame(), isModal); // we don't want all overlays to be modals
        this.nav = nav;
        initBaseUI();
    }

    private void initBaseUI() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //by default, you can't close the overlay by clicking X
            }
        });
    }

    public void open() {
        this.pack(); // Adjusts size to fit content
        this.setLocationRelativeTo(nav.getVisFrame()); // Center relative to main frame
        this.setVisible(true);
    }

    public void close() {
        this.dispose();
    }
}
