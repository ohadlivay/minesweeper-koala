package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;

import javax.swing.*;
import java.awt.event.*;

public class SettingsOverlay extends OverlayView {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonBack;
    private JPanel bottomPanel;
    private JPanel mainPanel;
    private JLabel difficultyLabel;
    private JPanel labelPanel;
    private JPanel difficultyPanel;
    private JPanel nameInputPanel;
    private JPanel startBtnPanel;
    private JLabel easy;
    private JLabel medium;
    private JLabel hard;
    private JTextField player1Name;
    private JTextField player2Name;
    private JPanel nameLabelPanel;
    private JLabel player1Label;
    private JLabel player2Label;
    private GameDifficulty difficulty;
    private final NavigationController nav; //very good


    public SettingsOverlay(NavigationController nav) {
        super(nav);
        this.nav = nav;
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(nav.getVisFrame());

        buttonStart.addActionListener(e -> onOK());
        buttonBack.addActionListener(e -> onCancel());

        // call onCancel() when X is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        easy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                difficulty = GameDifficulty.EASY;
                System.out.println("Selected Difficulty: EASY");
            }
        });

        medium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                difficulty = GameDifficulty.MEDIUM;
                System.out.println("Selected Difficulty: MEDIUM");
            }
        });

        hard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                difficulty = GameDifficulty.HARD;
                System.out.println("Selected Difficulty: HARD");
            }
        });

    }

    // pass the user input, close the overlay and go to the game screen
    private void onOK() {
        String player1 = getPlayer1Name();
        String player2 = getPlayer2Name();
        if (player1.isEmpty() || player2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter names for both players.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (difficulty == null) {
            JOptionPane.showMessageDialog(this, "Please select a difficulty level.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        GameSessionController.getInstance().setupGame(player1, player2, difficulty);
        nav.goToGame();
        close();
    }

    private void onCancel() {
        close();
    }

    // add getters to retrieve user input
    public String getPlayer1Name() {
        return player1Name.getText();
    }

    public String getPlayer2Name() {
        return player2Name.getText();
    }



}
