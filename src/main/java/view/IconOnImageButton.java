package main.java.view;

import javax.swing.*;
import java.awt.*;

public class IconOnImageButton extends JButton {
    private final ImageIcon bg;
    private final Icon fg;

    public IconOnImageButton(Runnable onClick, String tooltip, Dimension size, Icon fg, Icon bg) {
        this.bg = (bg instanceof ImageIcon ii) ? ii : null;
        this.fg = fg;

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setBorder(BorderFactory.createEmptyBorder());
        setMargin(new Insets(0, 0, 0, 0));

        setToolTipText(tooltip);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addActionListener(e -> onClick.run());
    }

    public IconOnImageButton(String tooltip, Dimension size, Icon fg, Icon bg) {
        this.bg = (bg instanceof ImageIcon ii) ? ii : null;
        this.fg = fg;

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setBorder(BorderFactory.createEmptyBorder());
        setMargin(new Insets(0, 0, 0, 0));

        setToolTipText(tooltip);
        setBorderPainted(true);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // safe since contentAreaFilled=false + opaque=false

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (bg != null) {
            g2.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        if (fg != null) {
            int x = (getWidth() - fg.getIconWidth()) / 2;
            int y = (getHeight() - fg.getIconHeight()) / 2 + 3;
            fg.paintIcon(this, g2, x, y);
        }

        g2.dispose();
    }

    // Factory method to create a koala difficulty button with wood background, koala icon, and text
    public static IconOnImageButton createKoalaButton(
            ImageIcon woodBg,
            ImageIcon koalaIcon,
            String text,
            String tooltip,
            Dimension size,
            Runnable onClick
    ) {
        IconOnImageButton b = new IconOnImageButton(onClick, tooltip, size, null, null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

                // bg
                if (woodBg != null) {
                    g2d.drawImage(woodBg.getImage(), 0, 0, getWidth(), getHeight(), this);
                }

                // koala - center horizontally and position in upper portion
                if (koalaIcon != null) {
                    int koalaWidth = koalaIcon.getIconWidth();
                    int koalaHeight = koalaIcon.getIconHeight();
                    int koalaX = (getWidth() - koalaWidth) / 2;
                    int koalaY = 22; // Position lower on button
                    g2d.drawImage(koalaIcon.getImage(), koalaX, koalaY, koalaWidth, koalaHeight, this);
                }

                // text - position at bottom
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = getHeight() - 20;
                g2d.drawString(text, textX, textY);

                // IMPORTANT: paint border last so it’s on top
                javax.swing.border.Border border = getBorder();
                if (border != null) {
                    border.paintBorder(this, g2d, 0, 0, getWidth(), getHeight());
                }

                g2d.dispose();
            }
        };

        // IMPORTANT: allow border painting
        b.setBorderPainted(true);

        // default padding (unselected state)
        b.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        return b;
    }

}

