package main.java.view;

import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.model.Question;
import main.java.model.SysData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionManagerScreen extends JPanel {

    private final NavigationController nav;
    private TableActionListener tableActionListener;
    private JPanel mainPanel;
    private JTable questionsTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton homeButton;

    //fields required for page navigation
    private List<Question> allQuestions = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private OutlinedLabel pageLabel;
    private JButton btnPrev, btnNext;

    private final ComponentAnimator animator;

    public QuestionManagerScreen(NavigationController navigationController) {
        this.nav = navigationController;
        animator = new ComponentAnimator();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        OutlinedLabel titleLabel = new OutlinedLabel("QUESTION MANAGER", Color.BLACK, 6f);
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
                int id = (int) tableModel.getValueAt(row, 0);
                Question q = SysData.getInstance().getQuestionByID(id);
                if (q != null) {
                    OverlayController.getInstance().showDeleteQuestionOverlay(q);
                } else {
                    System.err.println("Could not find question with ID: " + id);
                }
                if (tableActionListener != null) {
                    tableActionListener.onDelete(row);
                }
            }
        }));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(questionsTable);
        scrollPane.getViewport().setBackground(ColorsInUse.BG_COLOR.get());
        scrollPane.setBorder(new LineBorder(new Color(70, 80, 100), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //pages navigation panel
        JPanel pagesPanel = createPagesPanel();
        centerPanel.add(pagesPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        homeButton = createHomeButton();
        homeButton.addActionListener(e -> nav.goToHome());
        bottomPanel.add(homeButton, BorderLayout.WEST);
        btnAdd = createStyledButton("", ColorsInUse.BTN_COLOR.get());
        btnAdd.setPreferredSize(new Dimension(70, 40));

        java.net.URL addIconUrl = getClass().getResource("/plus-pixel.png");
        if (addIconUrl != null) {
            ImageIcon icon = new ImageIcon(addIconUrl);
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT);
            if(btnAdd != null)
                btnAdd.setIcon(new ImageIcon(img));

        }

        btnAdd.addActionListener(e -> OverlayController.getInstance().showAddEditOverlay(null));
        bottomPanel.add(btnAdd, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    //store all questions and refresh the table to show the first page
    public void populateTable(List<Question> questions) {
        if (questions == null) {
            this.allQuestions = new ArrayList<>();
        } else {
            this.allQuestions = new ArrayList<>(questions);
        }
        //make sure the current page isnt out of bounds after changes (like delete)
        int maxPage = (int) Math.ceil((double) allQuestions.size() / rowsPerPage);
        if (currentPage > maxPage) {
            currentPage = Math.max(1, maxPage);
        }
        refreshPage();
    }

    //for jumping to last page after adding a question
    public void jumpToLastPageAndPopulate(List<Question> questions) {
        if (questions == null) {
            this.allQuestions = new ArrayList<>();
        } else {
            this.allQuestions = new ArrayList<>(questions);
        }
        this.currentPage = (int) Math.ceil((double) allQuestions.size() / rowsPerPage);
        if (this.currentPage < 1) {
            this.currentPage = 1;
        }
        refreshPage();

        animator.flashForeground(questionsTable, ColorsInUse.CONFIRM.get(), ColorsInUse.TEXT.get());
    }

    private JPanel createPagesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        btnPrev = createStyledButton("<", ColorsInUse.BTN_COLOR.get());
        btnPrev.setPreferredSize(new Dimension(50, 36));
        btnPrev.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) allQuestions.size() / rowsPerPage);
            if (currentPage > 1) {
                currentPage--;
            } else {
                currentPage = Math.max(1, maxPage); // if this is the first page, go to last page (carousel)
            }
            refreshPage();
        });

        pageLabel = new OutlinedLabel("PAGE 1 OF 1", Color.BLACK, 4f);
        pageLabel.setFont(FontsInUse.PIXEL.getSize(28f));
        pageLabel.setForeground(ColorsInUse.TEXT.get());

        btnNext = createStyledButton(">", ColorsInUse.BTN_COLOR.get());
        btnNext.setPreferredSize(new Dimension(50, 36));
        btnNext.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) allQuestions.size() / rowsPerPage);
            if (currentPage < maxPage) {
                currentPage++;
            } else {
                currentPage = 1; //if this is the last page, go back to first page (carousel)
            }
            refreshPage();
        });
        panel.add(btnPrev);
        panel.add(pageLabel);
        panel.add(btnNext);

        return panel;
    }

    private void refreshPage() {
        tableModel.setRowCount(0);
        if (allQuestions.isEmpty()) {
            pageLabel.setText("PAGE 0 OF 0");
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        int totalQuestions = allQuestions.size();
        int maxPage = (int) Math.ceil((double) totalQuestions / rowsPerPage);

        //ensure current page is valid
        if (currentPage > maxPage) currentPage = maxPage;
        if (currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, totalQuestions);

        //add rows for the current page
        for (int i = start; i < end; i++) {
            Question q = allQuestions.get(i);
            Object[] rowData = {q.getId(), q.getQuestionText(), q.getDifficulty(), q.getAnswer1(), ""};
            tableModel.addRow(rowData);
        }

        //buttons are only enabled if there is only 1 page
        pageLabel.setText("PAGE " + currentPage + " OF " + maxPage);
        boolean canScroll = maxPage > 1;
        btnPrev.setEnabled(canScroll);
        btnNext.setEnabled(canScroll);
    }

    private void styleTable(JTable table) {
        table.setBackground(ColorsInUse.BTN_COLOR.get());
        table.setForeground(ColorsInUse.TEXT.get());
        table.setSelectionBackground(ColorsInUse.BOARD_ACTIVE_BORDER2.get());
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(Color.DARK_GRAY);
        table.setRowHeight(45);
        table.setFont(FontsInUse.PIXEL.getSize(22f));
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
        table.getColumnModel().getColumn(4).setMaxWidth(160);
        table.setFocusable(false);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(ColorsInUse.TEXT.get());
        btn.setFont(FontsInUse.PIXEL.getSize(24f));
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

        java.net.URL iconUrl = getClass().getResource("/home-pixel.png");
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            homeButton.setIcon(new ImageIcon(img));
        }

        homeButton.setBackground(ColorsInUse.BTN_COLOR.get());
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(true);

        return homeButton;
    }


    // --- getters ---

    public JPanel getMainPanel() { return mainPanel; }

}