package main.java.view;

import javax.swing.*;
import java.awt.*;

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

    public void updateColors(boolean isSelected, JTable table, Color normalBg) {
        setBackground(isSelected ? table.getSelectionBackground() : normalBg);
    }

}
