package main.java.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class TblBtnEditor extends AbstractCellEditor implements TableCellEditor {
    private final TblBtnPanel panel;
    private final JTable table;
    private final TableActionListener listener; // or move interface out

    private int editingModelRow = -1;

    public TblBtnEditor(JTable table, TableActionListener listener) {
        this.table = table;
        this.listener = listener;

        JButton edit = new JButton("Edit");
        JButton del  = new JButton("Del");
        edit.setFocusable(false);
        del.setFocusable(false);

        panel = new TblBtnPanel(edit, del);

        edit.addActionListener(e -> {
            fireEditingStopped();
            if (listener != null && editingModelRow >= 0) listener.onEdit(editingModelRow);
        });

        del.addActionListener(e -> {
            fireEditingStopped();
            if (listener != null && editingModelRow >= 0) listener.onDelete(editingModelRow);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // Convert view row -> model row (important if you later add sorting/filtering)
        editingModelRow = table.convertRowIndexToModel(row);
        panel.updateColors(true, table, table.getBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
