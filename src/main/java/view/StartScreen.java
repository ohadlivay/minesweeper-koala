package main.java.view;

import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.view.overlays.OverlayType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Represents the main menu/startup screen of the application.
 * Handles the display of the animated logo, menu buttons, and transitions to
 * other screens.
 */
public class StartScreen {
    private JPanel mainPanel;
    private JLabel startScreenLabel;
    private JButton startGameBtn;
    private JButton gameHistoryBtn;
    private JButton mngQuestionsBtn;
    private JButton exitBtn;
    private final NavigationController nav;

    public StartScreen(NavigationController nav) {
        this.nav = nav;
        initUI();
    }

    /**
     * Initializes the user interface components.
     * Sets up the background, top panel, animated logo, and center menu buttons.
     */
    private void initUI() {
        // BackgroundPanel with the resource image so it scales to the panel size
        mainPanel = new BackgroundPanel("/start-bg.jpeg");
        mainPanel.setLayout(new BorderLayout());

        // Top panel: only feed in center and info icon at EAST
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        URL iconUrl = getClass().getResource("/info-koala.png");
        IconOnImageButton infoIcon;

        ImageIcon infoDrawable = null;
        if (iconUrl != null) {
            try {
                BufferedImage img = ImageIO.read(iconUrl);
                Image scaled = img.getScaledInstance(105, 70, Image.SCALE_SMOOTH);
                infoDrawable = new ImageIcon(scaled);
            } catch (IOException ignored) {
            }
        }

        infoIcon = new IconOnImageButton(
                () -> OverlayController.getInstance().showOverlay(OverlayType.INSTRUCTIONS),
                "How to play",
                new Dimension(105, 70),
                infoDrawable,
                null);

        if (infoDrawable == null) {
            infoIcon.setText("i");
            infoIcon.setForeground(Color.WHITE);
        }

        topPanel.add(infoIcon, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Setup for animated GIF + static PNG combination to create a dynamic intro
        // Load GIF at original size to preserve animation
        ImageIcon animationIcon = null;
        URL gifUrl = getClass().getResource("/start-gif1.gif");
        if (gifUrl != null) {
            animationIcon = new ImageIcon(gifUrl);
        }

        if (animationIcon != null) {
            // start with gif at original size
            startScreenLabel = new JLabel(animationIcon);

            // timer to stop the gif and swap with static image scaled to 512x285
            int animationDurationMs = 7000;
            Timer stopAnimationTimer = new Timer(animationDurationMs, e -> {
                ImageIcon staticIcon = loadScaledIcon("logo-static", 768, 428);
                if (staticIcon != null) {
                    startScreenLabel.setIcon(staticIcon);
                }
            });
            stopAnimationTimer.setRepeats(false); // Ensure it only runs once
            stopAnimationTimer.start();
        } else {
            // fallback to static logo scaled to 512x285
            ImageIcon staticIcon = loadScaledIcon("logo-static", 768, 428);
            if (staticIcon != null) {
                startScreenLabel = new JLabel(staticIcon);
            } else {
                startScreenLabel = new JLabel("KOALA MINESWEEPER (Image Missing)");
                startScreenLabel.setForeground(Color.RED);
            }
        }

        startScreenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startScreenLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel centerButtonContainer = new JPanel();
        centerButtonContainer.setLayout(new BoxLayout(centerButtonContainer, BoxLayout.X_AXIS));
        centerButtonContainer.setOpaque(false); // let background show through
        centerButtonContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // create a fixed horizontal gap width between buttons
        int gap = 20;

        // Resource names for the three center buttons (Start, History, Questions)
        String[] centerResources = { "start-koala", "history-koala", "questions-koala" };
        Dimension btnSize = new Dimension(210, 70);

        // 1. Start Game Button
        ImageIcon startIcon = loadScaledIcon(centerResources[0], btnSize.width, btnSize.height);
        startGameBtn = new IconOnImageButton(
                () -> OverlayController.getInstance().showOverlay(OverlayType.SETTINGS),
                null, btnSize, startIcon, null);
        centerButtonContainer.add(startGameBtn);
        centerButtonContainer.add(Box.createRigidArea(new Dimension(gap, 0)));

        // 2. History Button
        ImageIcon historyIcon = loadScaledIcon(centerResources[1], btnSize.width, btnSize.height);
        gameHistoryBtn = new IconOnImageButton(
                () -> nav.goToHistory(),
                null, btnSize, historyIcon, null);
        centerButtonContainer.add(gameHistoryBtn);
        centerButtonContainer.add(Box.createRigidArea(new Dimension(gap, 0)));

        // 3. Questions Button
        ImageIcon questionsIcon = loadScaledIcon(centerResources[2], btnSize.width, btnSize.height);
        mngQuestionsBtn = new IconOnImageButton(
                () -> nav.goToQuestionManager(),
                null, btnSize, questionsIcon, null);
        centerButtonContainer.add(mngQuestionsBtn);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));

        // --- CENTER area ---
        JPanel contentStack = new JPanel();
        contentStack.setOpaque(false);
        contentStack.setLayout(new BoxLayout(contentStack, BoxLayout.Y_AXIS));

        // logo
        startScreenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentStack.add(Box.createVerticalStrut(40)); // tweak this to fine-tune Y
        contentStack.add(startScreenLabel);

        // buttons
        contentStack.add(Box.createVerticalStrut(20));
        centerButtonContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentStack.add(centerButtonContainer);

        // anchor panel
        JPanel centerAnchor = new JPanel(new GridBagLayout());
        centerAnchor.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 1.0;
        c.weighty = 1.0;
        centerAnchor.add(contentStack, c);

        mainPanel.add(centerAnchor, BorderLayout.CENTER);

        // EXIT BUTTON
        Dimension exitBtnSize = new java.awt.Dimension(120, 52);
        ImageIcon exitIcon = loadScaledIcon("exit-koala", exitBtnSize.width, exitBtnSize.height);

        exitBtn = new IconOnImageButton(
                () -> {
                    int option = JOptionPane.showConfirmDialog(
                            mainPanel,
                            "Are you sure you want to exit the application?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                },
                null, exitBtnSize, exitIcon, null);

        // Fallback or setup logic handled by IconOnImageButton, we just feed it the
        // icon.

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false); // let background show through
        bottomPanel.add(exitBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper method to load a resource with common image extensions and scale it.
     * Tries extensions .png, .jpg, .jpeg, .gif in order.
     *
     * @param resourceBase The base name of the resource (without extension).
     * @param width        The desired width.
     * @param height       The desired height.
     * @return A scaled ImageIcon, or null if the resource is not found.
     */
    private ImageIcon loadScaledIcon(String resourceBase, int width, int height) {
        String[] exts = { ".png", ".jpg", ".jpeg", ".gif" };
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
