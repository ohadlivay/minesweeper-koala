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
        instructionsPanel.add(createInstructionRow("/click-pixel.png", "Left click to Uncover a tile to find numbers or special items."));
        instructionsPanel.add(createInstructionRow("/pixel-flag.png", "Right click to place or remove a flag."));
        instructionsPanel.add(createInstructionRow("/white-outline-mine.png", "Avoid mines! revealing one deducts life. the game ends if health hits zero or all mines are revealed."));
        instructionsPanel.add(createInstructionRow("/heart.png", "Health pool indicates how many turns you have left.")); //improve this
        instructionsPanel.add(createInstructionRow("/tile-info.png", "A tile's number tells you exactly how many mines are in the 8 surrounding squares."));
        instructionsPanel.add(createInstructionRow("/pixel-question.png", "Answer question tiles to gain a reward."));
        instructionsPanel.add(createInstructionRow("/gift-pixel.png", "Activate surprise and get a reward or penalty."));
        instructionsPanel.add(createInstructionRow("/cost-pixel.png", "Activation of special tiles (Q/S) costs points, so manage your score wisely."));
        instructionsPanel.add(createInstructionRow("/target-pixel.png", "Maximize your score by revealing tiles and neutralizing mines while protecting your health."));
        instructionsPanel.add(createInstructionRow("/home-pixel.png", "Exit the current session and return to the main menu at any time."));
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
        btnClose.setFont(FontsInUse.PIXEL.getSize(20f));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> close());

        bottomPanel.add(btnClose);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);

        pack();
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