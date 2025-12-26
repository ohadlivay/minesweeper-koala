package main.java.view.overlays;

import main.java.controller.NavigationController;
import main.java.model.GameSession;
import main.java.view.BackgroundPanel;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import main.java.view.OutlinedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameOverOverlay extends OverlayView {
    private final boolean isWin;
    private final int finalScore;

    public GameOverOverlay(NavigationController nav, boolean isWin, int finalScore) {
        super(nav,true);
        this.isWin = isWin;
        this.finalScore = finalScore;

        //we enable closing the overlay when the user clicks the X button
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                close();
            }
        });

        initUI();
    }


    private void initUI() {
        String bgPath = isWin ? "/win-bg.png" : "/lose-bg.png";
        BackgroundPanel contentPane = new BackgroundPanel(bgPath);
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(30, 40, 30, 40));

        String titleText = isWin ? "YOU WIN!" : "GAME OVER";
        Color titleColor = ColorsInUse.TEXT.get();

        OutlinedLabel titleLabel = new OutlinedLabel(titleText, Color.BLACK, 4f);        titleLabel.setFont(FontsInUse.PIXEL2.getSize(48f));
        titleLabel.setForeground(titleColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        String iconPath = isWin ? "/green-koala.png" : "/red-koala.png";
        JLabel iconLabel = new JLabel();
        try {
            java.net.URL url = getClass().getResource(iconPath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        OutlinedLabel scoreLabel = new OutlinedLabel(" FINAL SCORE: " + finalScore, Color.BLACK, 3f);
        scoreLabel.setFont(FontsInUse.PIXEL.getSize(36f));
        scoreLabel.setForeground(ColorsInUse.TEXT.get());
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(scoreLabel);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);

        //give the players an option to play again
        JButton btnPlayAgain = new JButton(isWin ? "PLAY AGAIN" : "TRY AGAIN");
        btnPlayAgain.setPreferredSize(new Dimension(180, 50));
        btnPlayAgain.setBackground(ColorsInUse.BTN_COLOR.get());
        btnPlayAgain.setForeground(ColorsInUse.TEXT.get());
        btnPlayAgain.setFont(FontsInUse.PIXEL.getSize(24f));
        btnPlayAgain.setFocusPainted(false);
        btnPlayAgain.addActionListener(e -> {
            close();
            //need to add logic to start a new game
        });

        bottomPanel.add(btnPlayAgain);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);
        this.pack();
        this.setLocationRelativeTo(getParent());

    }


}
