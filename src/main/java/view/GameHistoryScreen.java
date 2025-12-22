package main.java.view;

import main.java.controller.NavigationController;
import main.java.model.GameSession;
import main.java.model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;


public class GameHistoryScreen extends JPanel{

    private final NavigationController nav;

    private JPanel mainPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JButton homeButton;
    private JLabel historyLabel;
    private DefaultTableModel tableModel;
    private JTable historyTable;
    private ArrayList<GameSession> allSessions = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private JLabel pageLabel;
    private JButton btnPrev, btnNext;


    public GameHistoryScreen(NavigationController navigationController) {
        nav = navigationController;
        initUI();

    }

    private void initUI() {
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("GAME HISTORY", SwingConstants.CENTER);
        titleLabel.setFont(FontsInUse.PIXEL.getSize(62f));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        //bottom panel holds home button
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton homeButton = createHomeButton();
        homeButton.addActionListener(e -> nav.goToHome());
        bottomPanel.add(homeButton, BorderLayout.WEST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Date", "Player 1", "Player 2", "Score", "Difficulty"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        historyTable = new JTable(tableModel);
        styleTable(historyTable);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(historyTable);
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

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void populateHistoryTable(ArrayList<GameSession> allSessions) {
        if (allSessions == null) {
            this.allSessions = new ArrayList<>();
        } else {
            this.allSessions = new ArrayList<>(allSessions);
        }
        refreshPage();
    }

    private JPanel createPagesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        btnPrev = createStyledButton("<", ColorsInUse.BTN_COLOR.get());
        btnPrev.setPreferredSize(new Dimension(50, 36));
        btnPrev.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) allSessions.size() / rowsPerPage);
            if (currentPage > 1) {
                currentPage--;
            }
            else
            {
                currentPage = Math.max(1, maxPage); // if this is the first page, go to last page (carousel)
            }
            refreshPage();
        });

        pageLabel = new JLabel("Page 1 of 1");
        pageLabel.setFont(FontsInUse.PIXEL.getSize(20f));
        pageLabel.setForeground(ColorsInUse.TEXT.get());

        btnNext = createStyledButton(">", ColorsInUse.BTN_COLOR.get());
        btnNext.setPreferredSize(new Dimension(50, 36));
        btnNext.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) allSessions.size() / rowsPerPage);
            if (currentPage < maxPage) {
                currentPage++;
            }
            else
            {
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
        if (allSessions.isEmpty()) {
            pageLabel.setText("Page 0 of 0");
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        int totalSessions= allSessions.size();
        int maxPage = (int) Math.ceil((double) totalSessions / rowsPerPage);

        //ensure current page is valid
        if (currentPage > maxPage) currentPage = maxPage;
        if (currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, totalSessions);

        //add rows for the current page
        for (int i = start; i < end; i++) {
            GameSession s = allSessions.get(i);
            Object[] rowData = {s.getTimeStamp(), s.getLeftPlayerName(), s.getRightPlayerName(), s.getPoints(), s.getGameDifficulty()};
            tableModel.addRow(rowData);
        }

        //buttons are only enabled if there is only 1 page
        pageLabel.setText("Page " + currentPage + " of " + maxPage);
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
        table.setFont(FontsInUse.PIXEL.getSize(20f));
        table.setShowGrid(true);
        table.setFillsViewportHeight(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(ColorsInUse.ALT_TEXT.get());
        header.setFont(FontsInUse.PIXEL.getSize(20f));
        header.setBorder(new LineBorder(Color.DARK_GRAY));
        header.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(250);
        table.getColumnModel().getColumn(1).setMinWidth(250);
        table.getColumnModel().getColumn(2).setMaxWidth(200);
        table.getColumnModel().getColumn(2).setMinWidth(200);
        table.getColumnModel().getColumn(3).setMaxWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(200);
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

        homeButton.setBackground(new Color(10, 10, 10));
        homeButton.setBorder(BorderFactory.createLineBorder(new Color(70, 80, 100), 2));
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(true);

        return homeButton;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
