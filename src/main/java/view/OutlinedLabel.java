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
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        String raw = getText() == null ? "" : getText();
        // split into lines (support \n). Ensure at least one line.
        String[] lines = raw.split("\n", -1);
        if (lines.length == 0) lines = new String[]{""};

        FontMetrics fm = g2.getFontMetrics(getFont());
        int lineHeight = fm.getHeight();
        int totalHeight = lineHeight * lines.length;

        int startY = (getHeight() - totalHeight) / 2 + fm.getAscent();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineWidth = fm.stringWidth(line);
            int x = (getWidth() - lineWidth) / 2;
            int y = startY + i * lineHeight;

            // Draw outline for this line
            if (outlineColor != null) {
                GlyphVector gv = getFont().createGlyphVector(g2.getFontRenderContext(), line);
                Shape outlineShape = gv.getOutline(x, y);
                g2.setColor(outlineColor);
                g2.draw(outlineShape);
            }

            // Fill text for this line
            GlyphVector fillGv = getFont().createGlyphVector(g2.getFontRenderContext(), line);
            Shape fillShape = fillGv.getOutline(x, y);
            g2.setColor(getForeground());
            g2.fill(fillShape);
        }

        g2.dispose();
    }

    // Allow changing outline color at runtime
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        repaint();
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    // Allow changing stroke width at runtime
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        repaint();
    }

    /**
     * Convenience: set outline color according to difficulty level.
     * level: 0 = easy (green), 1 = medium (yellow-orange), 2 = hard (red)
     */
    public void setDifficultyLevel(int level) {
        Color c = ColorsInUse.getDifficultyOutlineColor(level);
        setOutlineColor(c);
    }

    /**
     * Convenience: set the label to display two lines:
     *   "<level> level"
     *   "--<stats>--"
     * Also sets the outline color to match difficulty 'level'.
     *
     * Example: setLevelAndStats(1, "game stats here") -> "1 level" and "--game stats here--"
     */
    public void setLevelAndStats(int level, String stats) {
        setDifficultyLevel(level);
        if (stats == null || stats.trim().isEmpty()) {
            stats = "game stats here";
        }
        String text = level + " level\n--" + stats + "--";
        setText(text);
        repaint();
    }
}
