package main.java.view;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public enum FontsInUse {
    PIXEL("/fonts/pixel-font.ttf"),
    PIXEL2("/fonts/slkscreb.ttf");
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
            e.printStackTrace();
            this.baseFont = new Font("SansSerif", Font.PLAIN, 12);
        }
    }

    //Method to get the font at a specific size
    public Font getSize(float size) {
        return baseFont.deriveFont(size);
    }

    //Method to get the font with a specific style (BOLD, PLAIN) and size
    public Font getStyle(int style, float size) {
        return baseFont.deriveFont(style, size);
    }


}
