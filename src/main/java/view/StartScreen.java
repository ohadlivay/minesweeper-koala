package main.java.view;
import main.java.controller.NavigationController;
import javax.swing.*;

public class StartScreen {
    private JPanel mainPanel;
    private JPanel panel1;
    private JLabel startScreenLabel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private final NavigationController nav;

    public StartScreen(NavigationController nav) {
        this.nav = nav;

        button1.addActionListener(e -> nav.goToGame());
        button2.addActionListener(e -> nav.goToHistory());
        button3.addActionListener(e -> nav.goToQuestionManager());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


}
