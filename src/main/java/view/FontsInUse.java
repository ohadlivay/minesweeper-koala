package main.java.view;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public enum FontsInUse {
    PIXEL("/fonts/ByteBounce.ttf"),
    PIXEL2("/fonts/BoldPixels.ttf");
    private Font baseFont;


    FontsInUse(String resourcePath) {
        try (InputStream is = FontsInUse.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Font resource not found: " + resourcePath);
                this.baseFont = new Font("SansSerif", Font.PLAIN, 12);
            } else {
                this.baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                //create a new font out of the custom ttf file
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(this.baseFont);
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Error loading font " + resourcePath + ": " + e.getMessage());
            this.baseFont = new Font("SansSerif", Font.PLAIN, 12);
        }
    }

    //Method to get the font at a specific size
    public Font getSize(float size) {
        return baseFont.deriveFont(size);
    }


}
