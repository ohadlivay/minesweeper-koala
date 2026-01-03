package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;

import javax.swing.*;
import java.awt.*;

public class SurpriseOverlay extends OverlayView{

    private int healthChange;
    private int pointsChange;

    private JPanel contentPane;
    private JLabel titleLabel;
    private JLabel healthLabel;
    private JLabel pointsLabel;

    public SurpriseOverlay(NavigationController nav, int health, int points) {
        super(nav, false);
        GameSessionController.getInstance().setBlocked(true);
        this.healthChange = health;
        this.pointsChange = points;
        this.healthLabel = new JLabel();
        this.pointsLabel = new JLabel();

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));

        Font font = FontsInUse.PIXEL.getSize(18f);

        // ===== Title =====
        titleLabel = new JLabel("Surprise!", SwingConstants.CENTER);
        titleLabel.setFont(font);
        add(titleLabel, BorderLayout.NORTH);

        // ===== Content =====
        contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- Row 0 : Points ----
        JLabel pointsText = new JLabel("Points:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        pointsText.setFont(font);
        contentPane.add(pointsText, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        pointsLabel.setFont(font);
        contentPane.add(pointsLabel, gbc);

        // ---- Row 1 : Health ----
        JLabel heartLabel = new JLabel();
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            heartLabel.setIcon(new ImageIcon(scaled));
            heartLabel.setIconTextGap(10);
        }
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(heartLabel, gbc);

        healthLabel.setFont(font);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(healthLabel, gbc);

        Timer tPoints = animator.randomNumber(pointsLabel, pointsChange);
        Timer tHealth = animator.randomNumber(healthLabel, healthChange);

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

                GameSessionController.getInstance().setBlocked(false);
            }
        });
        waiter.start();


        setPreferredSize(new Dimension(260, 160));
        setMinimumSize(new Dimension(260, 160));
        setMaximumSize(new Dimension(260, 160));


        add(contentPane, BorderLayout.CENTER);
    }

}
