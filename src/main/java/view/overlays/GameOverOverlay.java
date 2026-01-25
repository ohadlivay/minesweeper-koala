package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;
import main.java.util.SoundManager;
import main.java.view.BackgroundPanel;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import main.java.view.IconOnImageButton;
import main.java.view.OutlinedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameOverOverlay extends OverlayView {
    private final boolean isWin;
    private final int finalScore;

    public GameOverOverlay(NavigationController nav, boolean isWin, int finalScore) {
        super(nav, true);
        this.isWin = isWin;
        this.finalScore = finalScore;

        // we enable closing the overlay when the user clicks the X button
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                close();
            }
        });

        initUI();
        playMusic();
    }

    private void initUI() {
        String bgPath = isWin ? "/win-bg-gif.gif" : "/loss-bg-gif.gif";
        BackgroundPanel contentPane = new BackgroundPanel(bgPath);
        contentPane.setLayout(new BorderLayout());
        contentPane.setPreferredSize(new Dimension(400, 446));
        contentPane.setBorder(new EmptyBorder(30, 40, 30, 40));

        String titleText = isWin ? "YOU WIN!" : "GAME OVER";
        Color titleColor = ColorsInUse.TEXT.get();

        OutlinedLabel titleLabel = new OutlinedLabel(titleText, Color.BLACK, 4f);
        titleLabel.setFont(FontsInUse.PIXEL2.getSize(48f));
        titleLabel.setForeground(titleColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        String iconPath = isWin ? "/game-won-koala.png" : "/game-over-koala.png";
        JLabel iconLabel = new JLabel();
        try {
            java.net.URL url = getClass().getResource(iconPath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        OutlinedLabel scoreLabel = new OutlinedLabel("FINAL SCORE: " + finalScore, Color.BLACK, 3f);
        scoreLabel.setFont(FontsInUse.PIXEL.getSize(40f));
        scoreLabel.setForeground(ColorsInUse.TEXT.get());
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(scoreLabel);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(0, 30, 0, 0)); // Align buttons with score text visually

        // give the players an option to play again
        ImageIcon bgIcon = null;
        try {
            java.net.URL bgUrl = getClass().getResource("/btn-koala.png");
            if (bgUrl != null) {
                BufferedImage img = javax.imageio.ImageIO.read(bgUrl);
                Image scaled = img.getScaledInstance(180, 52, Image.SCALE_SMOOTH); // sized for 180x50 approx
                bgIcon = new ImageIcon(scaled);
            }
        } catch (Exception ignored) {
        }

        IconOnImageButton btnPlayAgain = new IconOnImageButton(
                () -> {
                    String player1 = GameSessionController.getInstance().getSession().getLeftPlayerName();
                    String player2 = GameSessionController.getInstance().getSession().getRightPlayerName();
                    GameDifficulty selectedDifficulty = GameSessionController.getInstance().getSession()
                            .getGameDifficulty();

                    // restart a game with the same difficulty and names
                    GameSessionController.getInstance().setupGame(player1, player2, selectedDifficulty);
                    nav.goToGame();
                    close();
                },
                null,
                new Dimension(180, 50),
                null,
                bgIcon);

        // Add label manually since base IconOnImageButton doesn't draw text
        OutlinedLabel playLabel = new OutlinedLabel(isWin ? "PLAY AGAIN" : "TRY AGAIN", Color.BLACK, 2f);
        playLabel.setFont(FontsInUse.PIXEL.getSize(24f));
        playLabel.setForeground(ColorsInUse.TEXT.get());

        btnPlayAgain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(4, 0, 0, 0); // Visual adjustment for centering text on this specific bg
        btnPlayAgain.add(playLabel, gbc);

        // Home button
        ImageIcon homeIcon = null;
        try {
            java.net.URL url = getClass().getResource("/home-pixel.png");
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                homeIcon = new ImageIcon(img);
            }
        } catch (Exception ignored) {
        }

        // Load bg for button
        ImageIcon homeBg = null;
        try {
            java.net.URL bgUrl = getClass().getResource("/btn-koala.png");
            if (bgUrl != null) {
                BufferedImage img = javax.imageio.ImageIO.read(bgUrl);
                Image scaled = img.getScaledInstance(80, 70, Image.SCALE_SMOOTH);
                homeBg = new ImageIcon(scaled);
            }
        } catch (Exception ignored) {
        }

        IconOnImageButton btnHome = new IconOnImageButton(
                () -> {
                    nav.goToHome();
                    close();
                },
                null,
                new Dimension(72, 50),
                homeIcon,
                homeBg);

        bottomPanel.add(btnHome);
        bottomPanel.add(btnPlayAgain);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);
        this.pack();
        this.setLocationRelativeTo(getParent());

    }

    private void playMusic() {
        SoundManager.getInstance().playOnce(isWin ? SoundManager.SoundId.WIN : SoundManager.SoundId.LOSE);
        System.out.println(isWin ? SoundManager.SoundId.WIN.toString() : SoundManager.SoundId.LOSE.toString());
    }

}
