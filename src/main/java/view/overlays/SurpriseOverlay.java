package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;

import javax.swing.*;
import java.awt.*;

public class SurpriseOverlay extends OverlayView {

    private final int healthChange;
    private final int pointsChange;

    private JPanel contentPane;
    private JLabel titleLabel;
    private JLabel healthLabel;
    private JLabel pointsLabel;
    private JButton closeButton;

    public SurpriseOverlay(NavigationController nav, int health, int points) {
        super(nav, false);
        GameSessionController.getInstance().setBlocked(true);

        this.healthChange = health;
        this.pointsChange = points;

        this.healthLabel = new JLabel();
        this.pointsLabel = new JLabel();
        this.closeButton = new JButton("Close");

        initUI();
        startAnimation();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));

        Font font = FontsInUse.PIXEL.getSize(18f);

        titleLabel = new JLabel("Surprise!", SwingConstants.CENTER);
        titleLabel.setFont(font);
        add(titleLabel, BorderLayout.NORTH);

        contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel pointsText = new JLabel("Points:");
        pointsText.setFont(font);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(pointsText, gbc);

        pointsLabel.setFont(font);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(pointsLabel, gbc);

        JLabel heartLabel = new JLabel();
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            heartLabel.setIcon(new ImageIcon(scaled));
            heartLabel.setIconTextGap(10);
        }
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(heartLabel, gbc);

        healthLabel.setFont(font);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(healthLabel, gbc);

        closeButton.setFont(font);
        closeButton.addActionListener(e -> closeOverlay());
        closeButton.setBackground(ColorsInUse.BTN_COLOR.get());
        closeButton.setForeground(ColorsInUse.TEXT.get());
        closeButton.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(closeButton, gbc);

        setPreferredSize(new Dimension(260, 260));
        setMinimumSize(new Dimension(260, 260));
        setMaximumSize(new Dimension(260, 260));

        add(contentPane, BorderLayout.CENTER);
    }

    private void startAnimation() {
        Timer tPoints = animator.randomNumber(pointsLabel, pointsChange);
        Timer tHealth  = animator.randomNumber(healthLabel, healthChange);

        Timer waiter = new Timer(30, e -> {
            if (!tPoints.isRunning() && !tHealth.isRunning()) {
                ((Timer) e.getSource()).stop();

                pointsLabel.setText(String.valueOf(pointsChange));
                pointsLabel.setForeground(pointsChange >= 0
                        ? ColorsInUse.FEEDBACK_GOOD_COLOR.get()
                        : ColorsInUse.FEEDBACK_BAD_COLOR.get());

                healthLabel.setText(String.valueOf(healthChange));
                healthLabel.setForeground(healthChange >= 0
                        ? ColorsInUse.FEEDBACK_GOOD_COLOR.get()
                        : ColorsInUse.FEEDBACK_BAD_COLOR.get());

                closeButton.setEnabled(true);
                // auto-close after showing final values for 5 seconds
                closeOverlay();
            }
        });
        waiter.start();
    }

    private void closeOverlay() {
        Timer timer = new Timer(5000, e -> {
            GameSessionController.getInstance()
                    .setSurpriseToGameScreen(healthChange, pointsChange, pointsChange > 0);
            // change button text to close + countdown

            close();
        });

        animator.closeCountdown(closeButton, 0, 5);
        timer.setRepeats(false);
        timer.start();
    }
}
