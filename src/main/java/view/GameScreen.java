package main.java.view;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.model.*;
import main.java.view.overlays.OverlayType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
        mainPanel = new BackgroundPanel("/start-bg.jpeg");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        Font font = FontsInUse.PIXEL.getSize(28f);

        feedLabel = new JLabel("Welcome! Click a tile to start.", SwingConstants.CENTER);
        feedLabel.setFont(font);
        feedLabel.setForeground(ColorsInUse.TEXT.get());
        feedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Top panel: only feed in center and info icon at EAST
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        infoIcon = new JButton();
        infoIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
        infoIcon.setToolTipText("How to play");
        infoIcon.setContentAreaFilled(false);
        infoIcon.setBorderPainted(false);
        infoIcon.setFocusPainted(false);
        infoIcon.setOpaque(false);
        infoIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        infoIcon.addActionListener(e ->
                OverlayController.getInstance().showOverlay(OverlayType.INSTRUCTIONS)
        );

        URL iconUrl = getClass().getResource("/info-koala.png");
        if (iconUrl != null) {
            try {
                BufferedImage img = ImageIO.read(iconUrl);
                Image scaled = img.getScaledInstance(105, 70, Image.SCALE_SMOOTH);
                infoIcon.setIcon(new ImageIcon(scaled));
            } catch (IOException ignored) {}
        } else {
            // fallback so you notice missing resource
            infoIcon.setText("i");
            infoIcon.setForeground(Color.WHITE);
        }

        topPanel.add(infoIcon, BorderLayout.EAST);

        // Names panel: appears below the topPanel and above the boards
        JPanel namesPanel = new JPanel(new BorderLayout());
        namesPanel.setOpaque(false);
        namesPanel.setBorder(new EmptyBorder(0, 90, 5, 90));

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

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(player1Label);
        leftPanel.add(player1MinesLeftLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(player2MinesLeftLabel);
        rightPanel.add(player2Label);

        namesPanel.add(leftPanel, BorderLayout.WEST);
        namesPanel.add(feedLabel, BorderLayout.CENTER);
        namesPanel.add(rightPanel, BorderLayout.EAST);

        // North container stacks topPanel (feed + info) and namesPanel (player names) vertically
        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));
        northContainer.setOpaque(false);
        northContainer.add(topPanel);
        northContainer.add(namesPanel);

        mainPanel.add(northContainer, BorderLayout.NORTH);

        // center panel holds the boardlayouts
        centerPanel = new JPanel();
        centerPanel.setBackground(ColorsInUse.BG_COLOR_TRANSPARENT.get());
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // stats and bottom UI stay unchanged...
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        statsPanel.setBackground(ColorsInUse.BG_COLOR_TRANSPARENT.get());
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
        java.net.URL targetUrl = getClass().getResource("/cost-pixel.png");
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
        ImageIcon bg = loadScaledIcon("btn-koala", 80, 70);
        ImageIcon home = loadScaledIcon("home-pixel", 25, 25);

        JButton homeButton = new IconOnImageButton(
                () -> {
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
                },
                "Home",
                new Dimension(80, 70),
                home,
                bg
        );

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
        // special-case: show "This is not your turn" in white
        if (message != null && message.equalsIgnoreCase("This is not your turn!")) {
            feedLabel.setText(message);
            // ColorsInUse.TEXT.get() appears to be the main text color (white on dark bg).
            feedLabel.setForeground(ColorsInUse.TEXT.get());
            // do not run the usual color/animation logic for this message
            return;
        }

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

    private ImageIcon loadScaledIcon(String resourceBase, int width, int height) {
        String[] exts = {".png", ".jpg", ".jpeg", ".gif"};
        for (String ext : exts) {
            URL url = getClass().getResource("/" + resourceBase + ext);
            if (url != null) {
                try {
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }
}
