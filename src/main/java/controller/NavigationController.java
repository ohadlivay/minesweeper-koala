package main.java.controller;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import main.java.view.*;

public class NavigationController {
    private final JFrame VisFrame;

    public NavigationController(JFrame frame) {
        this.VisFrame = frame;
    }

    //***Methods to navigate between screens***//

    public void goToHome() {
        VisFrame.setContentPane(new HomeScreen(this));
        refresh();
    }

    public void goToGame() {
        VisFrame.setContentPane(new GameScreen(this));
        refresh();
    }

    public void goToHistory() {
        VisFrame.setContentPane(new GameHistoryScreen(this));
        refresh();
    }

    public void goToQuestionManager() {
        VisFrame.setContentPane(new QuestionManagerScreen(this));
        refresh();
    }

    //***Methods to open Overlays***//

    //***Other Methods***//

    private void refresh() {
        VisFrame.revalidate();
        VisFrame.repaint();
    }


}
