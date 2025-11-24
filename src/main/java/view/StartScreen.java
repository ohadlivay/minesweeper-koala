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

        // set listener to send user input to controller
        startGameBtn.addActionListener(e -> {
                    SettingsOverlay SO = new SettingsOverlay(nav);
                    SO.setListener(oview -> {
                        SettingsOverlay overlay = (SettingsOverlay) oview;

                        String player1 = overlay.getPlayer1Name().trim();
                        String player2 = overlay.getPlayer2Name().trim();
                        //need to send difficulty
                    });

                    SO.open();

                });
        gameHistoryBtn.addActionListener(e -> nav.goToHistory());
        mngQuestionsBtn.addActionListener(e -> nav.goToQuestionManager());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


}
