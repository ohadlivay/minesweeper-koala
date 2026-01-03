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

public class SurpriseOverlay extends OverlayView {

    private final int healthChange;
    private final int pointsChange;

    private OutlinedLabel pointsValueLabel;
    private OutlinedLabel healthValueLabel;
    private JButton closeButton;

    private static final Dimension OVERLAY_SIZE = new Dimension(520, 360);

    public SurpriseOverlay(NavigationController nav, int health, int points) {
        super(nav, false);
        GameSessionController.getInstance().setBlocked(true);

        this.healthChange = health;
        this.pointsChange = points;

        // allow closing via X
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeOverlayImmediately();
            }
        });

        initUI();
        startAnimation();
    }

    private void initUI() {
        BackgroundPanel contentPane = new BackgroundPanel("/surprise-overlay.png");
        contentPane.setLayout(new BorderLayout());
        contentPane.setPreferredSize(OVERLAY_SIZE);
        contentPane.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ===== Title =====
        OutlinedLabel title = new OutlinedLabel("SURPRISE!", Color.BLACK, 4f);
        title.setFont(FontsInUse.PIXEL2.getSize(52f));
        title.setForeground(ColorsInUse.TEXT.get());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(6, 6, 6, 6));
        contentPane.add(title, BorderLayout.NORTH);

        // ===== Center stats panel =====
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(18, 0, 18, 0));

        // Row: Points
        ContrastStrip pointsRow = new ContrastStrip();

        OutlinedLabel pointsText = new OutlinedLabel("POINTS:", Color.BLACK, 3f);
        pointsText.setFont(FontsInUse.PIXEL.getSize(34f));
        pointsText.setForeground(Color.WHITE);

        pointsValueLabel = new OutlinedLabel("0", Color.BLACK, 3f);
        pointsValueLabel.setFont(FontsInUse.PIXEL.getSize(34f));
        pointsValueLabel.setForeground(Color.WHITE);
        pointsValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        pointsRow.add(pointsText, BorderLayout.WEST);
        pointsRow.add(pointsValueLabel, BorderLayout.EAST);


        // Row: Health (icon + value)
        ContrastStrip healthRow = new ContrastStrip();

        JLabel heartLabel = new JLabel();
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            heartLabel.setIcon(new ImageIcon(scaled));
        }

        // left side: icon (looks better than "HEALTH:" on this screen)
        JPanel healthLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        healthLeft.setOpaque(false);
        healthLeft.add(heartLabel);

        healthValueLabel = new OutlinedLabel("0", Color.BLACK, 3f);
        healthValueLabel.setFont(FontsInUse.PIXEL.getSize(34f));
        healthValueLabel.setForeground(ColorsInUse.TEXT.get());
        healthValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        healthRow.add(healthLeft, BorderLayout.WEST);
        healthRow.add(healthValueLabel, BorderLayout.EAST);

        center.add(pointsRow);
        center.add(Box.createVerticalStrut(16));
        center.add(healthRow);

        contentPane.add(center, BorderLayout.CENTER);

        // ===== Bottom button =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        closeButton = new JButton("CLOSE");
        closeButton.setPreferredSize(new Dimension(180, 50));
        closeButton.setBackground(ColorsInUse.BTN_COLOR.get());
        closeButton.setForeground(ColorsInUse.TEXT.get());
        closeButton.setFont(FontsInUse.PIXEL.getSize(26f));
        closeButton.setFocusPainted(false);
        closeButton.setEnabled(false);
        closeButton.addActionListener(e -> closeOverlayImmediately());

        bottom.add(closeButton, BorderLayout.EAST);
        contentPane.add(bottom, BorderLayout.SOUTH);

        // match GameOverOverlay behavior
        this.setContentPane(contentPane);
        this.pack();
        this.setLocationRelativeTo(getParent());
    }

    private void startAnimation() {
        // These will animate label text; they are OutlinedLabel but still JLabel-based so it's fine.
        Timer tPoints = animator.randomNumber(pointsValueLabel, pointsChange);
        Timer tHealth = animator.randomNumber(healthValueLabel, healthChange);

        Timer waiter = new Timer(30, e -> {
            if (!tPoints.isRunning() && !tHealth.isRunning()) {
                ((Timer) e.getSource()).stop();

                pointsValueLabel.setText(formatSigned(pointsChange));
                pointsValueLabel.setForeground(pointsChange >= 0
                        ? ColorsInUse.FEEDBACK_GOOD_COLOR.get()
                        : ColorsInUse.FEEDBACK_BAD_COLOR.get());

                healthValueLabel.setText(formatSigned(healthChange));
                healthValueLabel.setForeground(healthChange >= 0
                        ? ColorsInUse.FEEDBACK_GOOD_COLOR.get()
                        : ColorsInUse.FEEDBACK_BAD_COLOR.get());

                closeButton.setEnabled(true);
                closeOverlay(); // start countdown + auto-close
            }
        });
        waiter.start();
    }

    private void closeOverlay() {
        // start countdown on the button (5..1), then close immediately
        animator.closeCountdown(closeButton, 0, 5);

        Timer timer = new Timer(5000, e -> closeOverlayImmediately());
        timer.setRepeats(false);
        timer.start();
    }

    private void closeOverlayImmediately() {
        GameSessionController.getInstance()
                .setSurpriseToGameScreen(healthChange, pointsChange, pointsChange > 0);
        GameSessionController.getInstance().setBlocked(false);
        close();
    }

    private String formatSigned(int value) {
        return value > 0 ? "+" + value : String.valueOf(value);
    }

    static class ContrastStrip extends JPanel {
        public ContrastStrip() {
            setOpaque(false);
            setLayout(new BorderLayout(8, 0));
            setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 170)); // strong but localized
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.dispose();
            super.paintComponent(g);
        }
    }

}
