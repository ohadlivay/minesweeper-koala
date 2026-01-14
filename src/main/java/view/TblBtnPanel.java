package main.java.view;

import javax.swing.*;
import java.awt.*;

/**
 * the panel that contaisn the edit and delete buttons
 * it is used by both TblBtnRenderer and TblBtnEditor, therefore made into a class
 */

public class TblBtnPanel extends JPanel {
    public final JButton btnEdit;
    public final JButton btnDelete;

    public TblBtnPanel(JButton edit, JButton del) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setOpaque(true);

        this.btnEdit = edit;
        this.btnDelete = del;

        add(btnEdit);
        add(btnDelete);
    }

    //updates the panel bg to match the table state (if a row is selected)
    public void updateColors(boolean isSelected, JTable table, Color normalBg) {
        setBackground(isSelected ? table.getSelectionBackground() : normalBg);
    }

}
