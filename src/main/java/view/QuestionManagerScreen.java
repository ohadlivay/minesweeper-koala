package main.java.view;

import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.model.Question;
import main.java.model.SysData;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionManagerScreen extends JPanel {

    private final NavigationController nav;
    private JPanel mainPanel;
    private JTable questionsTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton homeButton;

    //fields required for page navigation
    private List<Question> allQuestions = new ArrayList<>();
    private List<Question> filteredQuestions = new ArrayList<>();
    private int currentPage = 1;
    private final int rowsPerPage = 10;
    private OutlinedLabel pageLabel;
    private JButton btnPrev, btnNext;
    private JTextField searchField;

    private final ComponentAnimator animator;

    public QuestionManagerScreen(NavigationController navigationController) {
        this.nav = navigationController;
        animator = new ComponentAnimator();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        mainPanel = new BackgroundPanel("/start-bg.jpeg");
        mainPanel.setLayout(new BorderLayout(15, 15));
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

        questionsTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            private int lastCol = -1;
            private boolean asc = true;

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = questionsTable.columnAtPoint(e.getPoint());
                if (col == 4) return; // Actions

                // toggle direction if clicking same column
                if (col == lastCol) asc = !asc;
                else { asc = true; lastCol = col; }

                Comparator<Question> cmp = switch (col) {
                    case 0 -> Comparator.comparingInt(Question::getId);
                    case 1 -> Comparator.comparing(q -> safe(q.getQuestionText()), String.CASE_INSENSITIVE_ORDER);
                    case 2 -> Comparator.comparingInt(q -> q.getDifficulty().ordinal());
                    case 3 -> Comparator.comparing(q -> safe(q.getAnswer1()), String.CASE_INSENSITIVE_ORDER);
                    default -> null;
                };

                if (cmp != null) {
                    // Sort both master and filtered lists so order persists when filter changes/cleared
                    Comparator<Question> finalCmp = asc ? cmp : cmp.reversed();
                    allQuestions.sort(finalCmp);
                    filteredQuestions.sort(finalCmp);
                    refreshPage();
                }
            }

            private String safe(String s) { return s == null ? "" : s; }
        });


        //these 2 classes need to be created to handle the buttons in the table (this is the standard way to do it in swing)
        questionsTable.getColumnModel().getColumn(4).setCellRenderer(new TblBtnRenderer());
        questionsTable.getColumnModel().getColumn(4).setCellEditor(new TblBtnEditor(new TableActionListener() {

            // when edit button is clicked, open the overlay with the question data
            @Override
            public void onEdit(int row) {
                int id = (int) tableModel.getValueAt(row, 0);
                Question q = SysData.getInstance().getQuestionByID(id);
                OverlayController.getInstance().showAddEditOverlay(q);
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
            }
        }));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        // Search panel (filters results as the user types)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        OutlinedLabel searchLabel = new OutlinedLabel("Search:", Color.BLACK, 2f);
        searchLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        searchLabel.setForeground(Color.WHITE);

        searchField = new JTextField(15);
        searchField.setFont(FontsInUse.PIXEL.getSize(20f));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterQuestions(); }
            public void removeUpdate(DocumentEvent e) { filterQuestions(); }
            public void changedUpdate(DocumentEvent e) { filterQuestions(); }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(questionsTable);
        scrollPane.getViewport().setBackground(ColorsInUse.BG_COLOR_TRANSPARENT.get());
        scrollPane.setBorder(new LineBorder(new Color(70, 80, 100), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //pages navigation panel
        centerPanel.add(createPagesPanel(), BorderLayout.SOUTH);

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
        // initialize filtered list according to current search text (if any)
        filterQuestions();
    }

    //for jumping to last page after adding a question
    public void jumpToLastPageAndPopulate(List<Question> questions) {
        if (questions == null) {
            this.allQuestions = new ArrayList<>();
        } else {
            this.allQuestions = new ArrayList<>(questions);
        }
        // clear search so newly added item is visible, and reset filtered list
        if (searchField != null) searchField.setText("");
        this.filteredQuestions = new ArrayList<>(allQuestions);

        this.currentPage = (int) Math.ceil((double) filteredQuestions.size() / rowsPerPage);
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
            int maxPage = (int) Math.ceil((double) filteredQuestions.size() / rowsPerPage);
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
            int maxPage = (int) Math.ceil((double) filteredQuestions.size() / rowsPerPage);
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

    // Filter logic: updates filteredQuestions based on searchField text and refreshes page
    private void filterQuestions() {
        String query = (searchField != null ? searchField.getText() : "").toLowerCase();
        if (query.trim().isEmpty()) {
            filteredQuestions = new ArrayList<>(allQuestions);
        } else {
            filteredQuestions = allQuestions.stream()
                    .filter(q -> String.valueOf(q.getId()).contains(query) ||
                            (q.getQuestionText() != null && q.getQuestionText().toLowerCase().contains(query)) ||
                            (q.getDifficulty() != null && q.getDifficulty().toString().toLowerCase().contains(query)) ||
                            (q.getAnswer1() != null && q.getAnswer1().toLowerCase().contains(query)))
                    .collect(Collectors.toList());
        }
        currentPage = 1;
        refreshPage();
    }

    private void refreshPage() {
        tableModel.setRowCount(0);
        // Use filteredQuestions for pagination and display
        if (filteredQuestions == null || filteredQuestions.isEmpty()) {
            pageLabel.setText("PAGE 0 OF 0");
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        int totalQuestions = filteredQuestions.size();
        int maxPage = (int) Math.ceil((double) totalQuestions / rowsPerPage);

        //ensure current page is valid
        if (currentPage > maxPage) currentPage = maxPage;
        if (currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, totalQuestions);

        //add rows for the current page
        for (int i = start; i < end; i++) {
            Question q = filteredQuestions.get(i);
            Object[] rowData = {q.getId(), q.getQuestionText(), q.getDifficulty(), q.getAnswer1(), ""};
            tableModel.addRow(rowData);
        }

        //buttons are only enabled if there is more than one page
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
        ImageIcon bg = loadScaledIcon("btn-koala", 80, 70);
        ImageIcon home = loadScaledIcon("home-pixel", 25, 25);

        JButton homeButton = new IconOnImageButton("Home", new Dimension(80, 70), home, bg);

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

    public JPanel getMainPanel() { return mainPanel; }

}