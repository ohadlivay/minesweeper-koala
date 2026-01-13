package main.java.view;
import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.view.overlays.OverlayType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
                    JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void initUI() {
        // Use BackgroundPanel with the resource image so it scales to the panel size
        mainPanel = new BackgroundPanel("/start-bg.jpeg");
        mainPanel.setLayout(new BorderLayout());

        //setup for gif+static png combination
        java.net.URL animURL = getClass().getResource("/start-gif.gif");
        java.net.URL staticURL = getClass().getResource("/start-static.png");

        if (animURL != null) {
            //start with gif
            ImageIcon animation = new ImageIcon(animURL);
            startScreenLabel = new JLabel(animation);

            //timer to stop the gif and swap with static image
            int animationDurationMs = 3000;
            Timer stopAnimationTimer = new Timer(animationDurationMs, e -> {
                if (staticURL != null) {
                    startScreenLabel.setIcon(new ImageIcon(staticURL));
                }
            });
            stopAnimationTimer.setRepeats(false); // Ensure it only runs once
            stopAnimationTimer.start();
        } else {
            //fallback
            if (staticURL != null) {
                startScreenLabel = new JLabel(new ImageIcon(staticURL));
            } else {
                startScreenLabel = new JLabel("KOALA MINESWEEPER (Image Missing)");
                startScreenLabel.setForeground(Color.RED);
            }
        }

        startScreenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startScreenLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
        mainPanel.add(startScreenLabel, BorderLayout.NORTH);

        startGameBtn = new JButton();
        gameHistoryBtn = new JButton();
        mngQuestionsBtn = new JButton();

        java.awt.Font btnFont = FontsInUse.PIXEL.getSize(32f);

        // create outlined labels and style them
        OutlinedLabel startLbl = new OutlinedLabel("START GAME", ColorsInUse.BTN_COLOR.get(), 3f);
        startLbl.setFont(btnFont);
        startLbl.setForeground(ColorsInUse.TEXT.get());
        startLbl.setOpaque(false);

        OutlinedLabel historyLbl = new OutlinedLabel("GAME HISTORY", ColorsInUse.BTN_COLOR.get(), 3f);
        historyLbl.setFont(btnFont);
        historyLbl.setForeground(ColorsInUse.TEXT.get());
        historyLbl.setOpaque(false);

        OutlinedLabel manageLbl = new OutlinedLabel("MANAGE QUESTIONS", ColorsInUse.BTN_COLOR.get(), 3f);
        manageLbl.setFont(btnFont);
        manageLbl.setForeground(ColorsInUse.TEXT.get());
        manageLbl.setOpaque(false);

        // helper to add label centered and make it fill the button
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 1.0;
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

        OutlinedLabel[] lbls = {startLbl, historyLbl, manageLbl};
        Dimension btnSize = new Dimension(270, 270);
        for (int i = 0; i < centerButtons.length; i++) {
            JButton btn = centerButtons[i];
            OutlinedLabel lbl = lbls[i];

            btn.setLayout(new GridBagLayout());
            btn.add(lbl, gbc);

            btn.setFocusable(false);
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setMinimumSize(btnSize);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);

            if (borderImg != null) {
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

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerButtonContainer);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        OutlinedLabel exitLbl = new OutlinedLabel("EXIT", ColorsInUse.BTN_COLOR.get(), 3f);
        exitLbl.setFont(btnFont);
        exitLbl.setForeground(ColorsInUse.TEXT.get());
        exitLbl.setOpaque(false);

        exitBtn = new JButton();
        exitBtn.add(exitLbl, gbc);
        exitBtn.setFocusable(false);
        Dimension exitBtnSize = new java.awt.Dimension(100, 100);
        exitBtn.setPreferredSize(exitBtnSize);
        exitBtn.setMaximumSize(exitBtnSize);
        exitBtn.setMinimumSize(exitBtnSize);
        exitBtn.setBorderPainted(false);
        exitBtn.setContentAreaFilled(false);

        if (borderImg != null) {
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

    public JPanel getMainPanel() {
        return mainPanel;
    }


}
