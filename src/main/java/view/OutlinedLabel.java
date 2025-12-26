package main.java.view;

//this class is used for creating labels with an outline
import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;

public class OutlinedLabel extends JLabel {
    private Color outlineColor;
    private float strokeWidth;

    public OutlinedLabel(String text, Color outlineColor, float strokeWidth) {
        super(text);
        this.outlineColor = outlineColor;
        this.strokeWidth = strokeWidth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics fm = g2.getFontMetrics(getFont());
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;

        GlyphVector gv = getFont().createGlyphVector(g2.getFontRenderContext(), getText());
        Shape shape = gv.getOutline(x, y);

        g2.setColor(outlineColor);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.draw(shape);

        g2.setColor(getForeground());
        g2.fill(shape);

        g2.dispose();
    }
}
