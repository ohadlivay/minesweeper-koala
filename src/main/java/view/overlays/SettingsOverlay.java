package main.java.view.overlays;

import javax.swing.*;
import java.awt.event.*;

public class SettingsOverlay extends JDialog {
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
    private JTextField textField1;
    private JTextField textField2;
    private JPanel nameLabelPanel;
    private JLabel player1Label;
    private JLabel player2Label;

    public SettingsOverlay() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SettingsOverlay dialog = new SettingsOverlay();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
