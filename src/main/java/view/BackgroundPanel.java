package main.java.view;

import javax.swing.*;
import java.awt.*;

// this class is for setting background images to screens
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String resourcePath) {
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                this.backgroundImage = new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draws the image to fill the entire panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
