package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.util.SoundManager;
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
    private boolean isClosed = false;

    private OutlinedLabel healthValueLabel;
    private OutlinedLabel pointsValueLabel;
    private JButton closeButton;

    private JPanel leftCardPanel;
    private JPanel rightCardPanel;

    private Timer closeTimer;

    private static final Dimension OVERLAY_SIZE = new Dimension(520, 520);

    private static final String BG = "/surprise-bg.png";
    private static final String SELECTED_CARD = "/surprise-card-selected.png";
    private static final String UNSELECTED_CARD = "/surprise-card.png";
    private static final String GOOD_CARD = "/good-surprise-card.png";
    private static final String BAD_CARD = "/bad-surprise-card.png";

    public SurpriseOverlay(NavigationController nav, int health, int points) {
        super(nav, false);
        GameSessionController.getInstance().setBlocked(true);

        this.healthChange = health;
        this.pointsChange = points;
        this.closeButton = new JButton("CLOSE");
        this.isClosed = false;

        initUI();
        startAnimation();
    }

    private void initUI() {
        BackgroundPanel contentPane = new BackgroundPanel(BG);
        contentPane.setLayout(new BorderLayout());
        contentPane.setPreferredSize(OVERLAY_SIZE);
        contentPane.setBorder(new EmptyBorder(18, 18, 18, 18));

        // ===== TOP: title centered inside the top bar area =====
        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(22, 10, 0, 10));

        OutlinedLabel title = new OutlinedLabel("SURPRISE!", Color.BLACK, 4f);
        title.setFont(FontsInUse.PIXEL2.getSize(44f));
        title.setForeground(ColorsInUse.TEXT.get());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(title);

        contentPane.add(top, BorderLayout.NORTH);

        // ===== CENTER: cards + stats bar + button (all centered) =====
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // --- Row 0: cards ---
        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 0));
        cardsRow.setOpaque(false);

        leftCardPanel  = (JPanel) createCardTile(UNSELECTED_CARD);
        rightCardPanel = (JPanel) createCardTile(UNSELECTED_CARD);

        cardsRow.add(leftCardPanel);
        cardsRow.add(rightCardPanel);

        gbc.gridy = 0;
        gbc.insets = new Insets(40, 0, 0, 0); // push cards down under title bar
        center.add(cardsRow, gbc);

        // --- Row 1: stats inside the bottom bar area ---
        JPanel statsBar = new JPanel(new GridLayout(1, 2));
        statsBar.setOpaque(false);
        statsBar.setPreferredSize(new Dimension(460, 60));
        statsBar.setBorder(new EmptyBorder(0, 10, 0, 10)); // padding inside bar

        // LEFT HALF: heart + "x"
        JPanel leftHalf = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        leftHalf.setOpaque(false);

        JLabel heart = new JLabel();
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
            heart.setIcon(new ImageIcon(scaled));
        }

        healthValueLabel = new OutlinedLabel("0", Color.BLACK, 3f);
        healthValueLabel.setFont(FontsInUse.PIXEL.getSize(40f));
        healthValueLabel.setForeground(ColorsInUse.TEXT.get());

        leftHalf.add(heart);
        leftHalf.add(healthValueLabel);

        // RIGHT HALF: "POINTS:"
        JPanel rightHalf = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        rightHalf.setOpaque(false);

        pointsValueLabel = new OutlinedLabel("POINTS: 0", Color.BLACK, 3f);
        pointsValueLabel.setFont(FontsInUse.PIXEL.getSize(40f));
        pointsValueLabel.setForeground(ColorsInUse.TEXT.get());

        rightHalf.add(pointsValueLabel);

        leftHalf.setBorder(new EmptyBorder(0, 0, 0, 15));
        rightHalf.setBorder(new EmptyBorder(10, 25, 0, 0));

        statsBar.add(leftHalf);
        statsBar.add(rightHalf);

        // add to center with your existing gbc
        gbc.gridy = 1;
        gbc.insets = new Insets(80, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        center.add(statsBar, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;


        // --- Row 2: button small + centered ---
        closeButton.setText("close");
        closeButton.setPreferredSize(new Dimension(190, 54));
        closeButton.setBackground(ColorsInUse.BTN_COLOR.get());
        closeButton.setForeground(ColorsInUse.TEXT.get());
        closeButton.setFont(FontsInUse.PIXEL.getSize(22f));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> closeOverlayImmediately());

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        center.add(closeButton, gbc);

        contentPane.add(center, BorderLayout.CENTER);

        this.setContentPane(contentPane);
        this.pack();
        this.setLocationRelativeTo(getParent());
    }


    private JComponent createCardTile(String imagePath) {
        JPanel tile = new JPanel(new GridBagLayout());
        tile.setOpaque(false);
        tile.setPreferredSize(new Dimension(170, 170));

        JLabel card = new JLabel();
        card.setHorizontalAlignment(SwingConstants.CENTER);

        tile.add(card);

        setTileIcon(tile, imagePath);
        return tile;
    }


    private void setTileIcon(JPanel tile, String imagePath) {
        JLabel card = (JLabel) tile.getComponent(0);

        java.net.URL url = getClass().getResource(imagePath);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
            card.setIcon(new ImageIcon(scaled));
            card.setText(null);
        } else {
            card.setIcon(null);
            card.setText("CARD");
            card.setFont(FontsInUse.PIXEL.getSize(16f));
            card.setForeground(Color.BLACK);
        }
    }

    private void startAnimation() {
        int targetIndex = (int) (Math.random() * 2); // 0 = left, 1 = right
        boolean isGood = pointsChange > 0;

        JPanel[] cardPanels = new JPanel[]{leftCardPanel, rightCardPanel};

        final int loops = 6;
        final int totalSteps = loops * 2 + targetIndex;

        final int[] step = {0};
        final int[] current = {0};

        final int minDelay = 40;
        final int maxDelay = 240;

        setTileIcon(leftCardPanel, UNSELECTED_CARD);
        setTileIcon(rightCardPanel, UNSELECTED_CARD);

        Timer t = new Timer(70, e -> {
            // unselect current
            setTileIcon(cardPanels[current[0]], UNSELECTED_CARD);

            // move selection
            current[0] = (current[0] + 1) % 2;
            setTileIcon(cardPanels[current[0]], SELECTED_CARD);

            step[0]++;

            SoundManager.getInstance().playOnce(SoundManager.SoundId.SELECTION);

            // recompute progress every tick (0..1)
            double progress = Math.min(1.0, (double) step[0] / totalSteps);

            // ease-out slowdown (gets slower and slower)
            int delay = (int) (minDelay + (maxDelay - minDelay) * progress * progress);
            ((Timer) e.getSource()).setDelay(delay);

            // stop condition
            if (step[0] >= totalSteps && current[0] == targetIndex) {
                ((Timer) e.getSource()).stop();

                // reveal the TARGET card result; keep the other unselected
                setTileIcon(cardPanels[1- targetIndex], isGood ? GOOD_CARD : BAD_CARD);
                setTileIcon(cardPanels[targetIndex], UNSELECTED_CARD);

                // stats
                healthValueLabel.setText(String.valueOf(healthChange));
                pointsValueLabel.setText("POINTS: " + pointsChange);

                healthValueLabel.setForeground(
                        (healthChange >= 0 ? ColorsInUse.FEEDBACK_GOOD_COLOR : ColorsInUse.FEEDBACK_BAD_COLOR).get()
                );
                pointsValueLabel.setForeground(
                        pointsChange >= 0 ? ColorsInUse.FEEDBACK_GOOD_COLOR.get() : ColorsInUse.FEEDBACK_BAD_COLOR.get()
                );

                //this is maybe the reason for the double floating numbers
                if(!isClosed) closeOverlay();
            }
        });

        t.start();
    }

    private void closeOverlay() {
        //this is maybe the reason for the double floating numbers
        if(isClosed) return;
        closeTimer = new Timer(7000, e -> closeOverlayImmediately());
        closeTimer.setRepeats(false);
        closeTimer.start();
        animator.closeCountdown(closeButton, 0, 7);
        isClosed = true;
    }

    private void closeOverlayImmediately() {
        GameSessionController.getInstance()
                .setSurpriseToGameScreen(healthChange, pointsChange, pointsChange > 0);
        GameSessionController.getInstance().setBlocked(false);
        isClosed = true;
        close();
    }
}
