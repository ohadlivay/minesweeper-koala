package main.java.view;

import javax.swing.*;
import java.awt.*;

class IconOnImageButton extends JButton {
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
        setBorderPainted(false);
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

}

