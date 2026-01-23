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
        startGameBtn.addActionListener(e -> OverlayController.getInstance().showOverlay(OverlayType.SETTINGS));
        gameHistoryBtn.addActionListener(e -> nav.goToHistory());
        mngQuestionsBtn.addActionListener(e -> nav.goToQuestionManager());
        exitBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Are you sure you want to exit the application?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
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

        JButton infoIcon = new JButton();
        infoIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
        infoIcon.setToolTipText("How to play");
        infoIcon.setContentAreaFilled(false);
        infoIcon.setBorderPainted(false);
        infoIcon.setFocusPainted(false);
        infoIcon.setOpaque(false);
        infoIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        infoIcon.addActionListener(e -> OverlayController.getInstance().showOverlay(OverlayType.INSTRUCTIONS));

        URL iconUrl = getClass().getResource("/info-koala.png");
        if (iconUrl != null) {
            try {
                BufferedImage img = ImageIO.read(iconUrl);
                Image scaled = img.getScaledInstance(105, 70, Image.SCALE_SMOOTH);
                infoIcon.setIcon(new ImageIcon(scaled));
            } catch (IOException ignored) {
            }
        } else {
            // fallback so you notice missing resource
            infoIcon.setText("i");
            infoIcon.setForeground(Color.WHITE);
        }

        topPanel.add(infoIcon, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Setup for animated GIF + static PNG combination to create a dynamic intro
        // Load GIF at original size to preserve animation
        ImageIcon animationIcon = null;
        URL gifUrl = getClass().getResource("/logo-gif.gif");
        if (gifUrl != null) {
            animationIcon = new ImageIcon(gifUrl);
        }

        if (animationIcon != null) {
            // start with gif at original size
            startScreenLabel = new JLabel(animationIcon);

            // timer to stop the gif and swap with static image scaled to 512x285
            int animationDurationMs = 3000;
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

        startGameBtn = new JButton();
        gameHistoryBtn = new JButton();
        mngQuestionsBtn = new JButton();

        java.awt.Font btnFont = FontsInUse.PIXEL.getSize(32f);

        // helper to add label centered and make it fill the button
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton[] centerButtons = {startGameBtn, gameHistoryBtn, mngQuestionsBtn};

        URL borderURL = getClass().getResource("/labelBorder.png");
        BufferedImage borderImg = null;
        if (borderURL != null) {
            try {
                borderImg = ImageIO.read(borderURL);
            } catch (IOException ignored) {
            }
        }

        JPanel centerButtonContainer = new JPanel();
        centerButtonContainer.setLayout(new BoxLayout(centerButtonContainer, BoxLayout.X_AXIS));
        centerButtonContainer.setOpaque(false); // let background show through
        centerButtonContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // create a fixed horizontal gap width between buttons
        int gap = 20;

        // Resource names for the three center buttons (Start, History, Questions)
        String[] centerResources = {"start-koala", "history-koala", "questions-koala"};
        Dimension btnSize = new Dimension(210, 70);
        for (int i = 0; i < centerButtons.length; i++) {
            JButton btn = centerButtons[i];

            // remove internal text/labels: keep layout so icon is centered
            btn.setLayout(new GridBagLayout());

            btn.setFocusable(false);
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setMinimumSize(btnSize);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Try to load corresponding koala image and scale it to the button size
            ImageIcon koalaIcon = loadScaledIcon(centerResources[i], btnSize.width, btnSize.height);
            if (koalaIcon != null) {
                btn.setIcon(koalaIcon);
                btn.setHorizontalTextPosition(SwingConstants.CENTER);
                btn.setVerticalTextPosition(SwingConstants.CENTER);
            } else if (borderImg != null) {
                Image scaled = borderImg.getScaledInstance(btnSize.width, btnSize.height, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaled));
                btn.setHorizontalTextPosition(SwingConstants.CENTER);
                btn.setVerticalTextPosition(SwingConstants.CENTER);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
            } else {
                btn.setBackground(ColorsInUse.BTN_COLOR.get());
            }

            centerButtonContainer.add(btn);

            if (i < centerButtons.length - 1) {
                centerButtonContainer.add(Box.createRigidArea(new Dimension(gap, 0)));
            }
        }

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

        exitBtn = new JButton();
        exitBtn.setFocusable(false);
        Dimension exitBtnSize = new java.awt.Dimension(120, 52);
        exitBtn.setPreferredSize(exitBtnSize);
        exitBtn.setMaximumSize(exitBtnSize);
        exitBtn.setMinimumSize(exitBtnSize);
        exitBtn.setBorderPainted(false);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // try to load exit koala and scale to exit button size
        ImageIcon exitIcon = loadScaledIcon("exit-koala", exitBtnSize.width, exitBtnSize.height);
        if (exitIcon != null) {
            exitBtn.setIcon(exitIcon);
            exitBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            exitBtn.setVerticalTextPosition(SwingConstants.CENTER);
            exitBtn.setBorderPainted(false);
            exitBtn.setContentAreaFilled(false);
            exitBtn.setOpaque(false);
        } else if (borderImg != null) {
            Image scaled = borderImg.getScaledInstance(exitBtnSize.width, exitBtnSize.height, Image.SCALE_SMOOTH);
            exitBtn.setIcon(new ImageIcon(scaled));
            exitBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            exitBtn.setVerticalTextPosition(SwingConstants.CENTER);
            exitBtn.setBorderPainted(false);
            exitBtn.setContentAreaFilled(false);
            exitBtn.setOpaque(false);
        } else {
            exitBtn.setBackground(ColorsInUse.BTN_COLOR.get());
        }

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

