package main.java.view;

import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.controller.QuestionManagerController;
import main.java.model.Question;
import main.java.model.SysData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class QuestionManagerScreen extends JPanel {

    private final NavigationController nav;
    private TableActionListener tableActionListener;


    private JPanel mainPanel;
    private JTable questionsTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton homeButton;

    public QuestionManagerScreen(NavigationController navigationController) {
        this.nav = navigationController;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("QUESTION MANAGER", SwingConstants.CENTER);
        titleLabel.setFont(FontsInUse.PIXEL.getSize(62f));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Question", "Difficulty", "Correct Answer", "Actions"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // only this column is editable (for buttons)
            }
        };

        questionsTable = new JTable(tableModel);
        styleTable(questionsTable);

        //these 2 classes need to be created to handle the buttons in the table (this is the standard way to do it in swing)
        questionsTable.getColumnModel().getColumn(4).setCellRenderer(new TblBtnRenderer());
        questionsTable.getColumnModel().getColumn(4).setCellEditor(new TblBtnEditor(questionsTable, new TableActionListener() {

            // when edit button is clicked, open the overlay with the question data
            @Override
            public void onEdit(int row) {
                int id = (int) tableModel.getValueAt(row, 0);
                Question q = SysData.getInstance().getQuestionByID(id);
                OverlayController.getInstance().showAddEditOverlay(q);
                if (tableActionListener != null) {
                    tableActionListener.onEdit(row);
                }
            }

            @Override
            public void onDelete(int row) {
                if (tableActionListener != null) {
                    tableActionListener.onDelete(row);
                }
            }
        }));

        JScrollPane scrollPane = new JScrollPane(questionsTable);
        scrollPane.getViewport().setBackground(ColorsInUse.BG_COLOR.get());
        scrollPane.setBorder(new LineBorder(new Color(70, 80, 100), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        homeButton = createHomeButton();
        homeButton.addActionListener(e -> nav.goToHome());
        bottomPanel.add(homeButton, BorderLayout.WEST);
        btnAdd = createStyledButton("Add New Question", ColorsInUse.BTN_COLOR.get());
        btnAdd.setPreferredSize(new Dimension(200, 36));
        btnAdd.addActionListener(e -> {
            OverlayController.getInstance().showAddEditOverlay(null);
        });
        bottomPanel.add(btnAdd, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    //fetch questions from SysData with the help of the controller
    public void populateTable(List<Question> questions) {
        tableModel.setRowCount(0);
        if (questions != null) {
            for (Question q : questions) {
                Object[] rowData = {q.getId(), q.getQuestionText(), q.getDifficulty(), q.getAnswer1(), ""};
                tableModel.addRow(rowData);
                // hey tali should you save the questions at this point? or some PK?
                // so when user deletes, my controller can know which question he meant to delete
                // for now, use QuestionManagerController's userDeletedQuestion(Question)
            }
        }
    }

    private void styleTable(JTable table) {
        table.setBackground(ColorsInUse.BTN_COLOR.get());
        table.setForeground(ColorsInUse.TEXT.get());
        table.setSelectionBackground(ColorsInUse.BOARD_ACTIVE_BORDER2.get());
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(Color.DARK_GRAY);
        table.setRowHeight(45);
        table.setFont(FontsInUse.PIXEL.getSize(20f));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(ColorsInUse.ALT_TEXT.get());
        header.setFont(FontsInUse.PIXEL.getSize(20f));
        header.setBorder(new LineBorder(Color.DARK_GRAY));
        header.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(4).setMinWidth(160);
        table.getColumnModel().getColumn(4).setMaxWidth(180);
        table.setFocusable(false);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(ColorsInUse.TEXT.get());
        btn.setFont(FontsInUse.PIXEL.getSize(20f));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(bg);
            }
        });
        return btn;
    }

    private JButton createHomeButton() {
        homeButton = new JButton();
        homeButton.setPreferredSize(new Dimension(72, 36));
        java.net.URL icon = getClass().getResource("/home.png");
        if (icon != null) {
            homeButton.setIcon(new ImageIcon(icon));
        }

        homeButton.setBackground(ColorsInUse.BTN_COLOR.get());
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(true);

        return homeButton;
    }



    // --- getters ---

    public JPanel getMainPanel() { return mainPanel; }
    public JTable getQuestionsTable() { return questionsTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getBtnAdd() { return btnAdd; }


}