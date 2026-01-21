package main.java.view;

import main.java.controller.NavigationController;
import main.java.model.GameData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GameHistoryScreen extends JPanel{

    private final NavigationController nav;

    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JButton homeButton;
    private DefaultTableModel tableModel;
    private JTable historyTable;
    private List<GameData> allSessions = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private OutlinedLabel pageLabel;
    private JButton btnPrev, btnNext;


    public GameHistoryScreen(NavigationController navigationController) {
        nav = navigationController;
        initUI();

    }

    private void initUI() {
        setLayout(new BorderLayout());

        mainPanel = new BackgroundPanel("/start-bg.jpeg");
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        OutlinedLabel titleLabel = new OutlinedLabel("GAME HISTORY", Color.BLACK, 6f);
        titleLabel.setFont(FontsInUse.PIXEL.getSize(62f));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        //bottom panel holds home button
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        homeButton = createHomeButton();
        homeButton.addActionListener(e -> nav.goToHome());
        bottomPanel.add(homeButton, BorderLayout.WEST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Date", "Player 1", "Player 2", "Score", "Difficulty","Result"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        historyTable = new JTable(tableModel);
        styleTable(historyTable);

        historyTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            private int lastCol = -1;
            private boolean asc = true;

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = historyTable.columnAtPoint(e.getPoint());

                // toggle direction if clicking same column
                if (col == lastCol) asc = !asc;
                else { asc = true; lastCol = col; }

                Comparator<GameData> cmp = switch (col) {
                    case 0, 1 -> Comparator.comparingLong(GameData::getTimestampMillis);
                    case 2 -> Comparator.comparing(g -> safe(g.getLeftPlayerName()),
                            String.CASE_INSENSITIVE_ORDER);
                    case 3 -> Comparator.comparing(g -> safe(g.getRightPlayerName()),
                            String.CASE_INSENSITIVE_ORDER);
                    case 4 -> Comparator.comparingInt(GameData::getPoints);
                    case 5 -> Comparator.comparingInt(g -> g.getGameDifficulty().ordinal());
                    case 6 -> Comparator.comparing(GameData::isWin);
                    default -> null;
                };


                if (cmp != null) {
                    allSessions.sort(asc ? cmp : cmp.reversed());
                    refreshPage();
                }
            }

            private String safe(String s) { return s == null ? "" : s; }
        });

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

        JButton deleteBtn = createStyledButton("DELETE ALL", new Color(180, 50, 50));
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete all history?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                main.java.controller.HistoryController.getInstance().clearAllHistory();
            }
        });
        bottomPanel.add(deleteBtn, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void populateHistoryTable(List<GameData> games) {
        if (games == null) {
            this.allSessions = new ArrayList<>();
        } else {
            this.allSessions = new ArrayList<>(games);
        }
        this.currentPage = 1;
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

        pageLabel = new OutlinedLabel("PAGE 1 OF 1", Color.BLACK, 4f);
        pageLabel.setFont(FontsInUse.PIXEL.getSize(28f));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        tableModel.setRowCount(0);
        if (allSessions.isEmpty()) {
            pageLabel.setText("PAGE 0 OF 0");
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
            GameData s = allSessions.get(i);
            String formattedDate = s.getTimeStamp().format(formatter);
            String result = s.isWin() ? "WIN" : "LOSE";

            Object[] rowData = {
                    i + 1,
                    formattedDate,
                    s.getLeftPlayerName(),
                    s.getRightPlayerName(),
                    s.getPoints(),
                    s.getGameDifficulty(),
                    result
            };
            tableModel.addRow(rowData);
        }

        //buttons are only enabled if there is only 1 page
        pageLabel.setText("PAGE " + currentPage + " OF " + maxPage);
        boolean canScroll = maxPage > 1;
        btnPrev.setEnabled(canScroll);
        btnNext.setEnabled(canScroll);
    }

    private void styleTable(JTable table) {
        table.setBackground(ColorsInUse.BG_COLOR.get());
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
        btn.setFont(FontsInUse.PIXEL.getSize(24f));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
        ImageIcon bg = loadScaledIcon("btn-koala", 80, 70);
        ImageIcon home = loadScaledIcon("home-pixel", 25, 25);

        homeButton = new IconOnImageButton("Home", new Dimension(80, 70), home, bg);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return homeButton;
    }

    private ImageIcon loadScaledIcon(String resourceBase, int width, int height) {
        String[] exts = {".png", ".jpg", ".jpeg", ".gif"};
        for (String ext : exts) {
            URL url = getClass().getResource("/" + resourceBase + ext);
            if (url != null) {
                try {
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
