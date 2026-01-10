package main.java.view;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.model.*;
import main.java.view.overlays.OverlayType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class GameScreen extends JPanel implements ActionMadeListener, MinesLeftListener, GameOverListener, SurpriseListener, InputBlockListener {
    private final NavigationController nav;
    private final GameSession session; // Always holds the current game session
    private ComponentAnimator animator;

    private JPanel mainPanel;
    private JPanel centerPanel;

    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel player1MinesLeftLabel;
    private JLabel player2MinesLeftLabel;
    private JLabel healthLabel;
    private JLabel pointsLabel;
    private JLabel feedLabel;

    private JButton infoIcon;

    public GameScreen(NavigationController nav, GameSession session) {
        this.nav = nav;
        this.session = session;
        this.session.setActionMadeListener(this);
        this.session.setGameOverListener(this);
        this.session.getLeftBoard().setMinesLeftListener(this);
        this.session.getRightBoard().setMinesLeftListener(this);
        this.session.setSurpriseListener(this);
        animator = new ComponentAnimator();
        GameSessionController.getInstance().addInputBlockListener(this);
        initUI();
        setBoards(session.getLeftBoard(), session.getRightBoard());
        setPlayerNames(session.getLeftPlayerName(), session.getRightPlayerName());
    }

    public void setBoards(Board left, Board right) {
        Color leftColor = ColorsInUse.getRandomWarmBoardColor().get();
        Color rightColor = ColorsInUse.getRandomColdBoardColor().get();

        BoardLayout leftBoard = new BoardLayout(left, leftColor);
        BoardLayout rightBoard = new BoardLayout(right, rightColor);
        leftBoard.revalidate();
        rightBoard.revalidate();
        leftBoard.repaint();
        rightBoard.repaint();
        centerPanel.add(Box.createHorizontalGlue());
        centerPanel.add(leftBoard);
        centerPanel.add(Box.createHorizontalStrut(50)); // gap
        centerPanel.add(rightBoard);
        centerPanel.add(Box.createHorizontalGlue());
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void setPlayerNames(String leftName, String rightName) {
        player1Label.setText(leftName);
        player2Label.setText(rightName);
    }


    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Font font = FontsInUse.PIXEL.getSize(28f);


        feedLabel = new JLabel("Welcome! Click a tile to start.", SwingConstants.CENTER);
        feedLabel.setFont(font);
        feedLabel.setForeground(ColorsInUse.TEXT.get());
        feedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 20, 5, 20));

        this.infoIcon = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/info.png"))));
        infoIcon.setBorder(new EmptyBorder(0, 0, 5, 0));
        infoIcon.setToolTipText("How to play");
        infoIcon.setHorizontalAlignment(SwingConstants.RIGHT);

        infoIcon.setBackground(ColorsInUse.BG_COLOR.get());
        infoIcon.setFocusPainted(false);
        infoIcon.setContentAreaFilled(false);
        infoIcon.addActionListener(e -> OverlayController.getInstance().showOverlay(OverlayType.INSTRUCTIONS));

        topPanel.add(infoIcon, BorderLayout.NORTH);

        player1Label = new JLabel();
        player1Label.setForeground(ColorsInUse.TEXT.get());
        player1Label.setFont(font);

        player2Label = new JLabel();
        player2Label.setForeground(ColorsInUse.TEXT.get());
        player2Label.setFont(font);

        player1MinesLeftLabel = new JLabel("x" + session.getLeftBoard().getMinesLeft());
        player1MinesLeftLabel.setForeground(ColorsInUse.TEXT.get());
        player1MinesLeftLabel.setFont(font);
        java.net.URL bombUrl = getClass().getResource("/white-outline-mine.png");
        if (bombUrl != null) {
            ImageIcon icon = new ImageIcon(bombUrl);
            Image scaled = icon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
            player1MinesLeftLabel.setIcon(new ImageIcon(scaled));
            player1MinesLeftLabel.setIconTextGap(10);
        }

        player2MinesLeftLabel = new JLabel("x" + session.getRightBoard().getMinesLeft());
        player2MinesLeftLabel.setForeground(ColorsInUse.TEXT.get());
        player2MinesLeftLabel.setFont(font);
        if (bombUrl != null) {
            ImageIcon icon = new ImageIcon(bombUrl);
            Image scaled = icon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
            player2MinesLeftLabel.setIcon(new ImageIcon(scaled));
            player2MinesLeftLabel.setIconTextGap(10);
        }

        // Create panels for left player and right player
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        // Add items into each side panel
        leftPanel.add(player1Label);
        leftPanel.add(player1MinesLeftLabel, BorderLayout.EAST);

        rightPanel.add(player2MinesLeftLabel, BorderLayout.WEST);
        rightPanel.add(player2Label);

        // Add those sub-panels to the topPanel
        topPanel.add(feedLabel, BorderLayout.CENTER);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(20, 90, 10, 90));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //center panel holds the boardlayouts
        centerPanel = new JPanel();
        centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        GameDifficulty currentDifficulty = session.getGameDifficulty();
        String koalaPath = switch (currentDifficulty) {
            case EASY -> "/green-koala-pixel.png";
            case MEDIUM -> "/yellow-koala-pixel.png";
            case HARD -> "/red-koala-pixel.png";
        };

        JLabel difficultyLabel = new JLabel(currentDifficulty.toString());
        difficultyLabel.setForeground(ColorsInUse.TEXT.get());
        difficultyLabel.setFont(font);
        java.net.URL koalaUrl = getClass().getResource(koalaPath);
        if (koalaUrl != null) {
            ImageIcon icon = new ImageIcon(koalaUrl);
            Image scaled = icon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
            difficultyLabel.setIcon(new ImageIcon(scaled));
            difficultyLabel.setIconTextGap(10);
        }
        statsPanel.add(difficultyLabel);

        JLabel costLabel = new JLabel("Activation Cost: " + currentDifficulty.getActivationCost());
        costLabel.setForeground(ColorsInUse.TEXT.get());
        costLabel.setFont(font);
        java.net.URL targetUrl = getClass().getResource("/plus-minus.png");
        if (targetUrl != null) {
            ImageIcon icon = new ImageIcon(targetUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            costLabel.setIcon(new ImageIcon(scaled));
            costLabel.setIconTextGap(10);
        }
        statsPanel.add(costLabel);

        healthLabel = new JLabel("x" + session.getHealthPool());
        healthLabel.setForeground(ColorsInUse.TEXT.get());
        healthLabel.setFont(font);
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            healthLabel.setIcon(new ImageIcon(scaled));
            healthLabel.setIconTextGap(10);
        }
        statsPanel.add(healthLabel);

        java.net.URL scoreBgUrl = getClass().getResource("/labelBorder.png");
        final Image scoreBgImage = (scoreBgUrl != null) ? new ImageIcon(scoreBgUrl).getImage() : null;

        pointsLabel = new OutlinedLabel("Score: " + session.getPoints(), Color.BLACK, 3f) {
            protected void paintComponent(Graphics g) {
                if (scoreBgImage != null) {
                    g.drawImage(scoreBgImage, 0, -15, getWidth(), 70, this);
                }
                super.paintComponent(g);
            }
        };


        pointsLabel.setForeground(ColorsInUse.TEXT.get());
        pointsLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        pointsLabel.setOpaque(false);
        pointsLabel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        statsPanel.add(pointsLabel);


        // bottom panel holds the home button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton homeButton = createHomeButton();
        bottomPanel.add(homeButton, BorderLayout.WEST);

        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setOpaque(false);

        southContainer.add(statsPanel);
        southContainer.add(Box.createVerticalStrut(10));

        southContainer.add(bottomPanel);

        mainPanel.add(southContainer, BorderLayout.SOUTH);
    }

    //make sure to disable the info button when ViewQuestionOverlay is being viewed
    @Override
    public void onInputBlock(boolean isBlocked) {
        if (infoIcon != null) {
            infoIcon.setEnabled(!isBlocked);
        }
    }



    private JButton createHomeButton() {
        JButton homeButton = new JButton();
        homeButton.setPreferredSize(new Dimension(72, 36));
        java.net.URL iconUrl = getClass().getResource("/home-pixel.png");
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            homeButton.setIcon(new ImageIcon(img));
        }

        homeButton.setBackground(ColorsInUse.BTN_COLOR.get());
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(true);
        homeButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Are you sure you want to return to the main menu?",
                    "Confirm Navigation",
                    JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                OverlayController.getInstance().closeCurrentOverlay();
                nav.goToHome();
            }

        });

        return homeButton;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateMinesLeft(int minesLeft, Board board) {
        if (board == session.getLeftBoard()) player1MinesLeftLabel.setText("x" + minesLeft);
        else player2MinesLeftLabel.setText("x" + minesLeft);

    }

    //get the current health pool and points from the session
    private void updateLabels() {
        pointsLabel.setText("Score: " + session.getPoints());
        healthLabel.setText("x" + session.getHealthPool());
    }

    //this method replaces the old onPointsChange and onHealthChange, copmbining them together
    @Override
    public void onActionMade(String message, boolean positive, int healthChange, int pointsChange) {
        feedLabel.setText(message);

        feedLabel.setForeground(positive ? ColorsInUse.FEEDBACK_GOOD_COLOR.get() : ColorsInUse.FEEDBACK_BAD_COLOR.get());
        updateLabels();
        if (pointsChange != 0) {
            String text = (pointsChange > 0 ? "+" : "") + pointsChange;
            Color color = pointsChange > 0 ? ColorsInUse.FEEDBACK_GOOD_COLOR.get() : ColorsInUse.FEEDBACK_BAD_COLOR.get();
            animator.pulseBorder(pointsLabel, 6);
            animator.floatingNumber(pointsLabel, text, color, pointsChange > 0);
        }
        if (healthChange != 0) {
            String text = (healthChange > 0 ? "+" : "") + healthChange;
            Color color = healthChange > 0 ? ColorsInUse.FEEDBACK_GOOD_COLOR.get() : ColorsInUse.FEEDBACK_BAD_COLOR.get();
            animator.floatingNumber(healthLabel, text, color, healthChange > 0);
            if (healthChange < 0) animator.shake(healthLabel);
        }
    }

    //this method shows the end game screen when the game is over
    @Override
    public void onGameOver(boolean saved, boolean winOrLose, int score) {
        if (!saved)
            JOptionPane.showMessageDialog(mainPanel, "Error, could not save the game!", "Game Over", JOptionPane.ERROR_MESSAGE);
        OverlayController.getInstance().showGameOverOverlay(winOrLose, score);
    }

    @Override
    public void revealSurprise(int healthChange, int pointsChange) {
        OverlayController.getInstance().showSurpriseOverlay(healthChange, pointsChange);
    }
}
