package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;

import javax.swing.*;
import java.awt.*;
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

    public SettingsOverlay(NavigationController nav) {
        super(nav);
        setContentPane(contentPane);

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
                resetSelection();
                difficulty = GameDifficulty.EASY;
                easy.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                System.out.println("Selected Difficulty: EASY");
            }
        });

        medium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                difficulty = GameDifficulty.MEDIUM;
                medium.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                System.out.println("Selected Difficulty: MEDIUM");
            }
        });

        hard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                difficulty = GameDifficulty.HARD;
                hard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                System.out.println("Selected Difficulty: HARD");
            }
        });

    }

    // pass the user input, close the overlay and go to the game screen
    private void onOK() {
        String player1 = getPlayer1Name();
        String player2 = getPlayer2Name();

        // check for difficulty
        if (difficulty == null) {
            JOptionPane.showMessageDialog(null, "Please select a difficulty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // do not proceed if difficulty is not selected
        }

        //check for player names
        if (player1.trim().isEmpty() || player2.trim().isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this, nameWarning(player1, player2), "Names not chosen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.NO_OPTION) {
                return; // do not proceed if user selects NO
            }
            if (player1.trim().isEmpty()) {
                player1 = "Player 1";
            }
            if (player2.trim().isEmpty()) {
                player2 = "Player 2";
            }
        }
        GameSessionController.getInstance().setupGame(player1, player2, difficulty);
        nav.goToGame();
        close();
    }

    private void resetSelection() {
        easy.setBorder(null);
        medium.setBorder(null);
        hard.setBorder(null);
    }

    private String nameWarning (String player1, String player2) {
        if (player1.trim().isEmpty() && player2.trim().isEmpty()) {
            return "Names for both players not chosen\n Continue with default names?\n - Player 1\n - Player 2";
        } else if (player1.trim().isEmpty()) {
            return "Name for player 1 not chosen\nContinue with default name?\n - Player 1";
        } else {
            return "Name for player 2 not chosen\n Continue with default name?\n - Player 2";
        }

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
