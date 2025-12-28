package main.java.view;
import main.java.controller.NavigationController;
import main.java.controller.OverlayController;
import main.java.view.overlays.OverlayType;
import main.java.view.overlays.SettingsOverlay;

import javax.swing.*;
import java.awt.*;

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
        startGameBtn.addActionListener(e -> {
            OverlayController.getInstance().showOverlay(OverlayType.SETTINGS);
        });
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
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());

        startScreenLabel = new JLabel("KOALA MINESWEEPER");
        startScreenLabel.setForeground(java.awt.Color.WHITE);
        startScreenLabel.setFont(FontsInUse.PIXEL2.getSize(60f));
        startScreenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startScreenLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        mainPanel.add(startScreenLabel, BorderLayout.NORTH);

        startGameBtn = new JButton("START GAME");
        gameHistoryBtn = new JButton("GAME HISTORY");
        mngQuestionsBtn = new JButton("MANAGE QUESTIONS");

        java.awt.Font btnFont = FontsInUse.PIXEL.getSize(32f);
        JButton[] centerButtons = {startGameBtn, gameHistoryBtn, mngQuestionsBtn};

        JPanel centerButtonContainer = new JPanel();
        centerButtonContainer.setLayout(new BoxLayout(centerButtonContainer, BoxLayout.Y_AXIS));
        centerButtonContainer.setBackground(ColorsInUse.BG_COLOR.get());

        for (JButton btn : centerButtons) {
            btn.setFont(btnFont);
            btn.setFocusable(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new java.awt.Dimension(350, 60));
            btn.setPreferredSize(new java.awt.Dimension(350, 60));

            centerButtonContainer.add(btn);
            centerButtonContainer.add(Box.createRigidArea(new java.awt.Dimension(0, 20)));
        }

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(ColorsInUse.BG_COLOR.get());
        centerWrapper.add(centerButtonContainer);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        exitBtn = new JButton("Exit");
        exitBtn.setFont(btnFont);
        exitBtn.setFocusable(false);
        exitBtn.setPreferredSize(new java.awt.Dimension(120, 45));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());
        bottomPanel.add(exitBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


}
