package main.java.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;

public class TblBtnRenderer implements TableCellRenderer {
    private final TblBtnPanel panel;

    public TblBtnRenderer() {
        // Scaling to 20x20 so they sit nicely inside the 40x32 button
        JButton edit = createScaledIconButton("/pixel-pencil.png", ColorsInUse.BTN_COLOR.get(), 30, 30);
        JButton del  = createScaledIconButton("/x-pixel.png", ColorsInUse.BTN_COLOR.get(), 20, 20);

        panel = new TblBtnPanel(edit, del);
    }

    private JButton createScaledIconButton(String resourcePath, Color bg, int width, int height) {
        JButton btn = new JButton();
        URL iconUrl = getClass().getResource(resourcePath);
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            // Scale the underlying image
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        }
        btn.setBackground(bg);
        btn.setFocusable(false);
        btn.setPreferredSize(new Dimension(40, 32));
        return btn;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        panel.updateColors(isSelected, table, table.getBackground());
        return panel;
    }
}