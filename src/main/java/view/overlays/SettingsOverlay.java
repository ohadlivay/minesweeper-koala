package main.java.view.overlays;

import main.java.controller.NavigationController;

import javax.swing.*;
import java.awt.event.*;

public class SettingsOverlay extends OverlayView {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
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

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when X is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

    }

    // close the overlay and go to the game screen
    private void onOK() {
        nav.goToGame();
        dispose();
    }

    private void onCancel() {
        setVisible(false);
    }

    // add getters to retrieve user input
    public String getPlayer1Name() {
        return player1Name.getText();
    }

    public String getPlayer2Name() {
        return player2Name.getText();
    }

}
