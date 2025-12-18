package main.java.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TblBtnRenderer implements TableCellRenderer {
    private final TblBtnPanel panel;

    public TblBtnRenderer() {
        JButton edit = new JButton("Edit");
        JButton del  = new JButton("Del");
        edit.setFocusable(false);
        del.setFocusable(false);

        panel = new TblBtnPanel(edit, del);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        panel.updateColors(isSelected, table, table.getBackground());
        return panel;
    }

}
