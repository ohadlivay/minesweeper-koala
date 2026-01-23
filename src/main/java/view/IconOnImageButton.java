package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconOnImageButton extends JButton {
    protected boolean isHovered = false;
    protected boolean isSelectedState = false;
    private ImageIcon bg;
    private Icon fg;

    private static Image createLighterImage(Image img) {
        if (img == null)
            return null;
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        // Use RescaleOp to brighten
        java.awt.image.RescaleOp op = new java.awt.image.RescaleOp(1.4f, 0, null);
        return op.filter(bi, null);
    }

    public void setSelectedState(boolean selected) {
        this.isSelectedState = selected;
        repaint();
    }

    public boolean isSelectedState() {
        return isSelectedState;
    }

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

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
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

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
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

    // Factory method to create a koala difficulty button with wood background,
    // koala icon, and text
    public static IconOnImageButton createKoalaButton(
            ImageIcon woodBg,
            ImageIcon koalaIcon,
            String text,
            String tooltip,
            Dimension size,
            Runnable onClick) {

        // Pre-calculate normal and lighter images for hover/selection effect
        Image bgNormal = (woodBg != null) ? woodBg.getImage() : null;
        Image bgLight = createLighterImage(bgNormal);

        Image koalaNormal = (koalaIcon != null) ? koalaIcon.getImage() : null;
        Image koalaLight = createLighterImage(koalaNormal);

        IconOnImageButton b = new IconOnImageButton(onClick, tooltip, size, null, null) {
            @Override
            protected void paintComponent(Graphics g) {
                // Do NOT call super.paintComponent(g) because we want full custom painting here
                // and the super class paints its own bg/fg which are null here.

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

                boolean active = isHovered || isSelectedState;
                Image currentBg = active ? bgLight : bgNormal;
                Image currentKoala = active ? koalaLight : koalaNormal;

                // bg
                if (currentBg != null) {
                    g2d.drawImage(currentBg, 0, 0, getWidth(), getHeight(), this);
                }

                // koala - center horizontally and position in upper portion
                if (currentKoala != null) {
                    // Start with original size
                    int koalaWidth = currentKoala.getWidth(null);
                    int koalaHeight = currentKoala.getHeight(null);

                    double scale = Math.min(1.0, (double) getWidth() / 130.0);
                    int drawW = (int) (koalaWidth * scale);
                    int drawH = (int) (koalaHeight * scale);

                    int koalaX = (getWidth() - drawW) / 2;
                    int koalaY = (int) (22 * scale); // Position lower on button

                    g2d.drawImage(currentKoala, koalaX, koalaY, drawW, drawH, this);
                }

                // text - position at bottom
                switch (text) {
                    case "Easy" -> g2d.setColor(ColorsInUse.DIFFICULTY_EASY_OUTLINE);
                    case "Medium" -> g2d.setColor(ColorsInUse.DIFFICULTY_MEDIUM_OUTLINE);
                    case "Hard" -> g2d.setColor(ColorsInUse.DIFFICULTY_HARD_OUTLINE);
                    default -> g2d.setFont(new Font("Arial", Font.BOLD, 14));
                }

                int fontSize = (int) (16 * (Math.min(1.0, (double) getWidth() / 130.0)));
                g2d.setFont(new Font("Arial", Font.BOLD, Math.max(10, fontSize)));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = getHeight() - (int) (20 * (Math.min(1.0, (double) getHeight() / 130.0)));
                g2d.drawString(text, textX, textY);

                g2d.dispose();
            }
        };

        // IMPORTANT: allow border painting
        b.setBorderPainted(false); // We don't want the default border logic if we are doing custom visual states

        // default padding (unselected state)
        b.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        return b;
    }

}
