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
    private JPanel contentPane;
    private JButton btnClose;
    private JTextArea txtInstructions;
    private JLabel titleLabel;

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
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(ColorsInUse.BG_COLOR.get());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(600, 300));

        // Title
        titleLabel = new JLabel("How to Play");
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setFont(FontsInUse.PIXEL.getSize(28f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ColorsInUse.BG_COLOR.get());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        contentPane.add(titlePanel, BorderLayout.NORTH);

        // Instructions area (placeholders)
        txtInstructions = new JTextArea();
        txtInstructions.setEditable(false);
        txtInstructions.setOpaque(false);
        txtInstructions.setForeground(ColorsInUse.TEXT.get());
        txtInstructions.setFont(FontsInUse.PIXEL.getSize(22f));
        txtInstructions.setLineWrap(true);
        txtInstructions.setWrapStyleWord(true);
        txtInstructions.setText(
                "1) Left click to reveal a tile.\n" +
                "2) Right click to place or remove a flag.\n" +
                "3) Answer question tiles to gain bonus points.\n" +
                "4) Avoid mines - revealing one ends the game.\n" +
                "5) Use the pause/menu to change settings or view history.\n\n" +
                "(Replace these lines with more detailed instructions as needed.)"
        );

        JScrollPane scroll = new JScrollPane(txtInstructions);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(ColorsInUse.BG_COLOR.get());
        contentPane.add(scroll, BorderLayout.CENTER);

        // Bottom panel with Close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnClose = new JButton("Close");
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

    @Override
    public void close() {
        // unblock the game session when overlay is closed
        GameSessionController.getInstance().setBlocked(false);
        super.close();
    }
}