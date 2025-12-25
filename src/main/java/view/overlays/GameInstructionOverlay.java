package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameInstructionOverlay extends OverlayView {

    public GameInstructionOverlay(NavigationController navigationController) {
        super(navigationController, false);
        GameSessionController.getInstance().setBlocked(true); //blocks board interaction

        initUI();

        // ensure clicking X will also close and unblock
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(ColorsInUse.BG_COLOR.get());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(700, 420));

        // Title
        JLabel titleLabel = new JLabel("How to Play");
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setFont(FontsInUse.PIXEL.getSize(28f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ColorsInUse.BG_COLOR.get());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        contentPane.add(titlePanel, BorderLayout.NORTH);

        // Instructions area with icons (each row is a JLabel + icon)
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBackground(ColorsInUse.BG_COLOR.get());
        instructionsPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        // Add instruction rows using resource icons (png files in resources)
        instructionsPanel.add(createInstructionRow("/left-click.png", "Left click to reveal a tile."));
        instructionsPanel.add(createInstructionRow("/red-flag.png", "Right click to place or remove a flag."));
        instructionsPanel.add(createInstructionRow("/bomb.png", "Avoid mines! revealing one ends the game. Flagging a mine reveals it."));
        instructionsPanel.add(createInstructionRow("/question.png", "Answer question tiles to gain bonus points."));
        instructionsPanel.add(createInstructionRow("/surprise.png", "Activate surprise ang get a reward or penalty."));
        // we could write how many points for this specific game if we pass on here the session
        instructionsPanel.add(createInstructionRow("/plus-minus.png", "Activation of special costs points."));
        instructionsPanel.add(createInstructionRow("/home.png", "Use the Home button to quit game and return to main menu."));
        instructionsPanel.add(Box.createVerticalGlue());
        instructionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scroll = new JScrollPane(instructionsPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(ColorsInUse.BG_COLOR.get());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(scroll, BorderLayout.CENTER);

        // Bottom panel with Close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton btnClose = new JButton("Close");
        btnClose.setPreferredSize(new Dimension(110, 40));
        btnClose.setBackground(ColorsInUse.BTN_COLOR.get());
        btnClose.setForeground(ColorsInUse.TEXT.get());
        btnClose.setFont(FontsInUse.PIXEL.getSize(16f));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> close());

        bottomPanel.add(btnClose);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);
    }

    // helper to create a row with an icon and text
    private JPanel createInstructionRow(String resourcePath, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        row.setBackground(ColorsInUse.BG_COLOR.get());
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        // load and scale icon
        JLabel iconLabel = new JLabel();
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {
        }

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(ColorsInUse.TEXT.get());
        textLabel.setFont(FontsInUse.PIXEL.getSize(22f));

        row.add(iconLabel);
        row.add(textLabel);
        return row;
    }

    @Override
    public void close() {
        // unblock the game session when overlay is closed
        GameSessionController.getInstance().setBlocked(false);
        super.close();
    }
}