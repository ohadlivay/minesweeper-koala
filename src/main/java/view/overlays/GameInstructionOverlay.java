package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.view.BackgroundPanel;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import main.java.view.OutlinedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameInstructionOverlay extends OverlayView {

    public GameInstructionOverlay(NavigationController navigationController) {
        super(navigationController, false);
        GameSessionController.getInstance().setBlocked(true); // blocks board interaction

        initUI();

        // ensure clicking X will also close and unblock
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void initUI() {
        // Use BackgroundPanel as the main content pane
        JPanel contentPane = new BackgroundPanel("/overlay-bg.png");
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(25, 30, 20, 30));

        // --- TITLE SECTION ---
        // Use a thinner, semi-transparent outline and add bottom padding so the stroke doesn't create a visible line
        JLabel titleLabel = new OutlinedLabel("How to Play", new Color(0,0,0,120), 2f);
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setFont(FontsInUse.PIXEL.getSize(42f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(new EmptyBorder(8, 0, 10, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false); // Ensure background shows through
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        contentPane.add(titlePanel, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel mainBody = new JPanel();
        mainBody.setLayout(new BoxLayout(mainBody, BoxLayout.Y_AXIS));
        mainBody.setOpaque(false); // Fix for "white block" issue

        // 1. Cooperative Goal
        mainBody.add(Box.createRigidArea(new Dimension(0, 15)));
        mainBody.add(createHeader("COOPERATIVE MISSION"));
        mainBody.add(createWrappedText("This is a 2-player team effort! Collaborate with your partner to neutralize mines and clear the board. It is not a competition; you win or lose together."));
        mainBody.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. Side-by-Side Panel (Actions & Tiles)
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        splitPanel.setOpaque(false);

        // Column A: Game Actions
        JPanel actionsCol = new JPanel();
        actionsCol.setLayout(new BoxLayout(actionsCol, BoxLayout.Y_AXIS));
        actionsCol.setOpaque(false);
        actionsCol.add(createHeader("ACTIONS"));
        actionsCol.add(createInstructionRow("/click-pixel.png", "Left Click: Reveal tile"));
        actionsCol.add(createInstructionRow("/pixel-flag.png", "Right Click: Flag mine"));
        actionsCol.add(createInstructionRow("/heart.png", "Lives: Shared pool"));
        actionsCol.add(createInstructionRow("/home-pixel.png", "Home: Exit game"));
        splitPanel.add(actionsCol);

        // Column B: Tile Types
        JPanel tilesCol = new JPanel();
        tilesCol.setLayout(new BoxLayout(tilesCol, BoxLayout.Y_AXIS));
        tilesCol.setOpaque(false);
        tilesCol.add(createHeader("TILE TYPES"));
        tilesCol.add(createInstructionRow("/tile-info.png", "Numbers: Nearby mines"));
        tilesCol.add(createInstructionRow("/pixel-question.png", "Q: Answer for rewards"));
        tilesCol.add(createInstructionRow("/gift-pixel.png", "S: Surprise effects"));
        tilesCol.add(createInstructionRow("/white-outline-mine.png", "Mine: Avoid these!"));
        splitPanel.add(tilesCol);

        mainBody.add(splitPanel);
        mainBody.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. Strategy Note
        mainBody.add(createHeader("PRO TIP"));
        mainBody.add(createWrappedText("Special tiles (Q & S) cost points to activate. Manage your score carefully! sometimes revealing standard tiles is the safer path to victory!"));

        // Scroll Pane Configuration
        JScrollPane scroll = new JScrollPane(mainBody);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(new Color(0,0,0,0)); // Extra transparency layer
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(scroll, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton btnClose = new JButton("Close");
        btnClose.setPreferredSize(new Dimension(110, 40));
        btnClose.setBackground(ColorsInUse.BTN_COLOR.get());
        btnClose.setForeground(ColorsInUse.TEXT.get());
        btnClose.setFont(FontsInUse.PIXEL.getSize(20f));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> close());

        bottomPanel.add(btnClose);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);
        this.setPreferredSize(new Dimension(850, 600));
        pack();
    }

    private JLabel createHeader(String text) {
        JLabel header = new JLabel(text);
        header.setForeground(ColorsInUse.TEXT.get());
        header.setFont(FontsInUse.PIXEL.getSize(26f));
        header.setBorder(new EmptyBorder(5, 0, 5, 0));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        return header;
    }

    private JTextArea createWrappedText(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(FontsInUse.PIXEL.getSize(20f));
        textArea.setForeground(ColorsInUse.TEXT.get());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0,0,0,0)); // Ensure transparency
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Corrected: Uses Insets instead of EmptyBorder
        textArea.setMargin(new Insets(0, 10, 0, 10));
        return textArea;
    }

    private JPanel createInstructionRow(String resourcePath, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(400, 45));

        JLabel iconLabel = new JLabel();
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(ColorsInUse.TEXT.get());
        textLabel.setFont(FontsInUse.PIXEL.getSize(18f));

        row.add(iconLabel);
        row.add(textLabel);
        return row;
    }

    @Override
    public void close() {
        GameSessionController.getInstance().setBlocked(false);
        super.close();
    }
}