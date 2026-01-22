package main.java.view.overlays;

import main.java.controller.NavigationController;
import main.java.util.SoundManager;
import main.java.view.ComponentAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Abstract class representing a modal overlay dialog in the application.
 * All specific overlays should extend this class.
 */

public abstract class OverlayView extends JDialog {

    protected final NavigationController nav;
    protected ComponentAnimator animator;
    protected SoundManager soundManager;

    public OverlayView(NavigationController nav, boolean isModal) {
        super(nav.getVisFrame(), isModal); // we don't want all overlays to be modals
        this.nav = nav;
        animator = new ComponentAnimator();
        soundManager = SoundManager.getInstance();
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

    ImageIcon loadScaledIcon(String resourceName, int width, int height) {
        try {
            // Remove .png extension if already included
            String resourceBase = resourceName.endsWith(".png") ? resourceName.substring(0, resourceName.length() - 4) : resourceName;
            resourceBase = resourceBase.startsWith("/") ? resourceBase.substring(1) : resourceBase;

            // Try multiple extensions
            String[] exts = {".png", ".jpg", ".jpeg", ".gif"};
            for (String ext : exts) {
                java.net.URL url = getClass().getResource("/" + resourceBase + ext);
                if (url != null) {
                    try {
                        java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                        if (img != null) {
                            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                            return new ImageIcon(scaled);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load icon: " + resourceName);
        }
        return null;
    }
}
