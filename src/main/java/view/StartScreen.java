package main.java.view;
import main.java.controller.NavigationController;
import main.java.view.overlays.SettingsOverlay;

import javax.swing.*;

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

        // listener to open the GameSettings overlay
        startGameBtn.addActionListener(e -> {
            SettingsOverlay SO = new SettingsOverlay(nav);
            SO.open();
        });
        gameHistoryBtn.addActionListener(e -> nav.goToHistory());
        mngQuestionsBtn.addActionListener(e -> nav.goToQuestionManager());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


}
