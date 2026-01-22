package main.java.view;

import main.java.controller.NavigationController;
import main.java.model.GameData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class GameHistoryScreen extends JPanel{

    private final NavigationController nav;

    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JButton homeButton;
    private DefaultTableModel tableModel;
    private JTable historyTable;
    private List<GameData> allSessions = new ArrayList<>();

    // Add: filtered list and search field
    private List<GameData> filteredSessions = new ArrayList<>();
    private JTextField nameField;
    private JComboBox difficultyBox;
    private JComboBox winLossBox;
    private JButton fromDateButton;
    private JButton toDateButton;
    private LocalDate fromDate;
    private LocalDate toDate;

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
        mainPanel.setBackground(ColorsInUse.TABLE_BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        OutlinedLabel titleLabel = new OutlinedLabel("GAME HISTORY", Color.BLACK, 6f);
        titleLabel.setFont(FontsInUse.PIXEL.getSize(62f));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel controlsPanel = createControlsPanel();
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        String[] columnNames = {"ID", "Date", "Player 1", "Player 2", "Score", "Difficulty", "Result"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(tableModel);
        styleTable(historyTable);
        setupTableHeaderListener();

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(ColorsInUse.TABLE_BG_COLOR.get());
        scrollPane.setBorder(new LineBorder(new Color(70, 80, 100), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(createPagesPanel(), BorderLayout.SOUTH);
        return centerPanel;
    }

    private void setupTableHeaderListener() {
        historyTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            private int lastCol = -1;
            private boolean asc = true;

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = historyTable.columnAtPoint(e.getPoint());
                if (col == lastCol) asc = !asc;
                else { asc = true; lastCol = col; }

                Comparator<GameData> cmp = switch (col) {
                    case 0, 1 -> Comparator.comparingLong(GameData::getTimestampMillis);
                    case 2 -> Comparator.comparing(g -> safe(g.getLeftPlayerName()), String.CASE_INSENSITIVE_ORDER);
                    case 3 -> Comparator.comparing(g -> safe(g.getRightPlayerName()), String.CASE_INSENSITIVE_ORDER);
                    case 4 -> Comparator.comparingInt(GameData::getPoints);
                    case 5 -> Comparator.comparingInt(g -> g.getGameDifficulty().ordinal());
                    case 6 -> Comparator.comparing(GameData::isWin);
                    default -> null;
                };

                if (cmp != null) {
                    Comparator<GameData> finalCmp = asc ? cmp : cmp.reversed();
                    allSessions.sort(finalCmp);
                    // also sort filtered view to keep UI consistent
                    filteredSessions.sort(finalCmp);
                    refreshPage();
                }
            }

            private String safe(String s) { return s == null ? "" : s; }
        });
    }

    private JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setOpaque(false);
        controlsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(ColorsInUse.BG_COLOR.get());
        scrollPane.setBorder(new LineBorder(new Color(70, 80, 100), 1));

        // ADD: Search Panel (placed above table)
        JPanel filterPanel = createFilterPanel();

        controlsPanel.add(filterPanel, BorderLayout.NORTH);
        controlsPanel.add(scrollPane, BorderLayout.CENTER);

        homeButton = createIconButton("btn-square", "home-pixel", "Home");
        homeButton.addActionListener(e -> nav.goToHome());
        controlsPanel.add(homeButton, BorderLayout.WEST);

        JButton deleteBtn = createDeleteAllButton();
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete all history?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                main.java.controller.HistoryController.getInstance().clearAllHistory();
            }
        });
        controlsPanel.add(deleteBtn, BorderLayout.EAST);

        return controlsPanel;
    }

    public void populateHistoryTable(List<GameData> games) {
        if (games == null) {
            this.allSessions = new ArrayList<>();
        } else {
            this.allSessions = new ArrayList<>(games);
        }
        // initialize filtered list to show all by default
        this.filteredSessions = new ArrayList<>(this.allSessions);
        currentPage = 1;
        refreshPage();
    }

    private JPanel createPagesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        btnPrev = createStyledButton("<", ColorsInUse.BTN_COLOR.get());
        btnPrev.setPreferredSize(new Dimension(50, 36));
        btnPrev.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) filteredSessions.size() / rowsPerPage);
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
            int maxPage = (int) Math.ceil((double) filteredSessions.size() / rowsPerPage);
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

    private JPanel createFilterPanel() {
        JPanel toReturn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toReturn.setOpaque(false);
        OutlinedLabel searchLabel = new OutlinedLabel("Player Name:", Color.BLACK, 2f);
        searchLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        searchLabel.setForeground(ColorsInUse.TEXT.get());

        nameField = new JTextField(15);
        nameField.setFont(FontsInUse.PIXEL.getSize(20f));
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterSessions(); }
            public void removeUpdate(DocumentEvent e) { filterSessions(); }
            public void changedUpdate(DocumentEvent e) { filterSessions(); }
        });

        toReturn.add(searchLabel);
        toReturn.add(nameField);
        toReturn.add(Box.createRigidArea(new Dimension(10, 0)));

        OutlinedLabel difficultyLabel = new OutlinedLabel("Difficulty:", Color.BLACK, 2f);
        difficultyLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        difficultyLabel.setForeground(ColorsInUse.TEXT.get());
        difficultyBox = new JComboBox<>(new String[]{"All", "Easy", "Medium", "Hard"});
        difficultyBox.setFont(FontsInUse.PIXEL.getSize(20f));
        difficultyBox.addActionListener(e -> filterSessions());

        toReturn.add(difficultyLabel);
        toReturn.add(difficultyBox);
        toReturn.add(Box.createRigidArea(new Dimension(10, 0)));

        OutlinedLabel winLossLabel = new OutlinedLabel("Win/Loss:", Color.BLACK, 2f);
        winLossLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        winLossLabel.setForeground(ColorsInUse.TEXT.get());
        winLossBox = new JComboBox<>(new String[]{"All", "Win", "Loss"});
        winLossBox.setFont(FontsInUse.PIXEL.getSize(20f));
        winLossBox.addActionListener(e -> filterSessions());

        toReturn.add(winLossLabel);
        toReturn.add(winLossBox);
        toReturn.add(Box.createRigidArea(new Dimension(10, 0)));

        // Date Range Filter
        OutlinedLabel dateRangeLabel = new OutlinedLabel("Date Range:", Color.BLACK, 2f);
        dateRangeLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        dateRangeLabel.setForeground(ColorsInUse.TEXT.get());

        fromDate = LocalDate.now().minusMonths(1);
        toDate = LocalDate.now();

        fromDateButton = createDateButton(fromDate, true);
        toDateButton = createDateButton(toDate, false);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setOpaque(false);
        JLabel fromLabel = new OutlinedLabel("From:", Color.BLACK, 2f);
        fromLabel.setFont(FontsInUse.PIXEL.getSize(20f));
        fromLabel.setForeground(ColorsInUse.TEXT.get());
        datePanel.add(fromLabel);
        datePanel.add(fromDateButton);
        JLabel toLabel = new OutlinedLabel("To:", Color.BLACK, 2f);
        toLabel.setFont(FontsInUse.PIXEL.getSize(20f));
        toLabel.setForeground(ColorsInUse.TEXT.get());
        datePanel.add(toLabel);
        datePanel.add(toDateButton);

        toReturn.add(dateRangeLabel);
        toReturn.add(datePanel);
        toReturn.add(Box.createRigidArea(new Dimension(10, 0)));

        return toReturn;
    }

    private JButton createDateButton(LocalDate initialDate, boolean isFromDate) {
        JButton btn = new JButton(initialDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        btn.setFont(FontsInUse.PIXEL.getSize(16f));
        btn.setPreferredSize(new Dimension(120, 30));
        btn.setBackground(ColorsInUse.BTN_COLOR.get());
        btn.setForeground(ColorsInUse.TEXT.get());
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            LocalDate selectedDate = showDatePicker(isFromDate ? fromDate : toDate);
            if (selectedDate != null) {
                if (isFromDate) {
                    fromDate = selectedDate;
                    fromDateButton.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    toDate = selectedDate;
                    toDateButton.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                filterSessions();
            }
        });

        return btn;
    }

    private LocalDate showDatePicker(LocalDate initialDate) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(initialDate.getMonthValue(), 1, 12, 1));
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(initialDate.getYear(), 2000, 2100, 1));
        JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(initialDate.getDayOfMonth(), 1, 31, 1));

        // Fix year spinner to not use comma formatting
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "0000");
        yearSpinner.setEditor(yearEditor);

        // Update day spinner max when month/year changes
        var updateDayMax = new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                int year = (int) yearSpinner.getValue();
                int month = (int) monthSpinner.getValue();
                int maxDay = LocalDate.of(year, month, 1).lengthOfMonth();
                int currentDay = (int) daySpinner.getValue();
                daySpinner.setModel(new SpinnerNumberModel(Math.min(currentDay, maxDay), 1, maxDay, 1));
            }
        };
        monthSpinner.addChangeListener(updateDayMax);
        yearSpinner.addChangeListener(updateDayMax);

        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        spinnerPanel.add(new JLabel("Day:"));
        spinnerPanel.add(daySpinner);
        spinnerPanel.add(new JLabel("Month:"));
        spinnerPanel.add(monthSpinner);
        spinnerPanel.add(new JLabel("Year:"));
        spinnerPanel.add(yearSpinner);

        panel.add(spinnerPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, "Select Date", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                return LocalDate.of((int) yearSpinner.getValue(), (int) monthSpinner.getValue(), (int) daySpinner.getValue());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    private void filterSessions() {
        String searchedName = (nameField == null ? "" : nameField.getText()).toLowerCase().trim();
        String selectedDifficulty = (difficultyBox != null) ? (String) difficultyBox.getSelectedItem() : "All";
        String selectedWinLoss = (winLossBox != null) ? (String) winLossBox.getSelectedItem() : "All";

        filteredSessions = allSessions.stream()
                .filter(g -> {
                    // Name filter
                    boolean matchesQuery = searchedName.isEmpty() ||
                            (g.getLeftPlayerName() != null && g.getLeftPlayerName().toLowerCase().contains(searchedName)) ||
                            (g.getRightPlayerName() != null && g.getRightPlayerName().toLowerCase().contains(searchedName));

                    // Difficulty filter ("All" = show all)
                    boolean matchesDifficulty = selectedDifficulty.equals("All") ||
                            g.getGameDifficulty().toString().equalsIgnoreCase(selectedDifficulty);

                    // Win/Loss filter ("All" = show all)
                    boolean matchesWinLoss = selectedWinLoss.equals("All") ||
                            (selectedWinLoss.equalsIgnoreCase("Win") && g.isWin()) ||
                            (selectedWinLoss.equalsIgnoreCase("Loss") && !g.isWin());

                    // Date range filter
                    LocalDate gameDate = g.getTimeStamp().toLocalDate();
                    boolean matchesDateRange = (fromDate == null || !gameDate.isBefore(fromDate)) &&
                            (toDate == null || !gameDate.isAfter(toDate));

                    return matchesQuery && matchesDifficulty && matchesWinLoss && matchesDateRange;
                })
                .collect(Collectors.toList());

        currentPage = 1;
        refreshPage();
    }

    // UPDATE: refreshPage to use filteredSessions
    private void refreshPage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        tableModel.setRowCount(0);
        if (filteredSessions == null || filteredSessions.isEmpty()) {
            pageLabel.setText("PAGE 0 OF 0");
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        int totalSessions= filteredSessions.size();
        int maxPage = (int) Math.ceil((double) totalSessions / rowsPerPage);

        //ensure current page is valid
        if (currentPage > maxPage) currentPage = maxPage;
        if (currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, totalSessions);

        //add rows for the current page
        for (int i = start; i < end; i++) {
            GameData s = filteredSessions.get(i);
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
        table.setBackground(ColorsInUse.TABLE_BG_COLOR.get());
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

    private JButton createIconButton(String resourcePathBtn, String resourcePathIcon, String tooltip) {
        ImageIcon bg = loadScaledIcon(resourcePathBtn, 80, 70);
        ImageIcon icon = loadScaledIcon(resourcePathIcon, 25, 25);

        JButton iconOnImageButton = new IconOnImageButton(tooltip, new Dimension(80, 70), icon, bg);

        return iconOnImageButton;
    }

    private JButton createDeleteAllButton() {
        ImageIcon bgIcon = loadScaledIcon("btn-koala", 150, 70);
        JButton deleteBtn = new JButton(bgIcon);
        deleteBtn.setPreferredSize(new Dimension(150, 70));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setOpaque(false);
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color bgColor = ColorsInUse.DIFFICULTY_HARD_OUTLINE;

        OutlinedLabel label = new OutlinedLabel("DELETE ALL", Color.WHITE, 2f);
        label.setFont(FontsInUse.PIXEL.getSize(26f));
        label.setForeground(bgColor); // Red text color


        // need this ugly thing just for the slight downwards text offset
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(8, 0, 0, 0); // Top padding for downward offset

        deleteBtn.setLayout(gbl);
        gbl.setConstraints(label, gbc);
        deleteBtn.add(label);

        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (deleteBtn.isEnabled()) {
                    label.setForeground(new Color(255, 100, 100));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (deleteBtn.isEnabled()) {
                    label.setForeground(bgColor);
                }
            }
        });

        return deleteBtn;
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
