package main.java.view;

import main.java.controller.NavigationController;

import javax.swing.*;

public class GameScreen extends JPanel {
    private JPanel gamePanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel board1Placeholder;
    private JPanel board2Placeholder;
    private JLabel player1Label;
    private JLabel player2Label;
    private JButton exitBtn;

    public GameScreen(NavigationController navigationController) {

    }

    public JPanel getGamePanel() {
        return gamePanel;
    }
}
