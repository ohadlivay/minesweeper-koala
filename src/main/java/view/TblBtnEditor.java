package main.java.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.net.URL;

/**
 * handles interaction with the actions column in the Questions table
 * when the user clicks one of the buttons, this class takes over to process the button events
 */
public class TblBtnEditor extends AbstractCellEditor implements TableCellEditor {
    private final TblBtnPanel panel;
    private int editingModelRow = -1;

    public TblBtnEditor(TableActionListener listener) {

        JButton edit = createScaledIconButton("/pixel-pencil.png", ColorsInUse.BTN_COLOR.get(), 30, 30);
        JButton del  = createScaledIconButton("/x-pixel.png", ColorsInUse.BTN_COLOR.get(), 20, 20);

        panel = new TblBtnPanel(edit, del);

        edit.addActionListener(e -> {
            fireEditingStopped();
            if (editingModelRow >= 0) {
                listener.onEdit(editingModelRow);
            }
        });

        del.addActionListener(e -> {
            fireEditingStopped();
            if (editingModelRow >= 0) {
                listener.onDelete(editingModelRow);
            }
        });
    }

    private JButton createScaledIconButton(String resourcePath, Color bg, int width, int height) {
        JButton btn = new JButton();
        URL iconUrl = getClass().getResource(resourcePath);
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        }
        btn.setBackground(bg);
        btn.setFocusable(false);
        btn.setPreferredSize(new Dimension(40, 32));
        btn.setBorderPainted(false);
        return btn;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        editingModelRow = table.convertRowIndexToModel(row);
        panel.updateColors(true, table, table.getBackground());
        return panel;
    }

    //this is required by TableCellEditor. returns the value of the cell after editing
    //since these are just buttons, we return an empty string
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}