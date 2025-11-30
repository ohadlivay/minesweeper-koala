package main.java.view.overlays;

import main.java.controller.NavigationController;

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

    }

    /* close the overlay and go to the game screen
        doesn't pass user input for now
     */
    // tali please send the difficulty level and player names here.
    // 29/11/25 8am ohad
    private void onOK() {
        nav.goToGame();
        close();
    }

    private void onCancel() {
        close();
    }

    /* add getters to retrieve user input
    public String getPlayer1Name() {
        return player1Name.getText();
    }

    public String getPlayer2Name() {
        return player2Name.getText();
    }

     */

}
