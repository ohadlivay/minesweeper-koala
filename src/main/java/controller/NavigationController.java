package main.java.controller;

import javax.swing.JFrame;

import main.java.view.*;

public class NavigationController {
    private final JFrame visFrame;

    public NavigationController(JFrame frame) {
        this.visFrame = frame;
    }

    //***Methods to navigate between screens***//

    public void goToHome() {
        visFrame.setContentPane(new HomeScreen(this));
        refresh();
    }

    public void goToGame() {
        visFrame.setContentPane(new GameScreen(this));
        refresh();
    }

    public void goToHistory() {
        visFrame.setContentPane(new GameHistoryScreen(this));
        refresh();
    }

    public void goToQuestionManager() {
        visFrame.setContentPane(new QuestionManagerScreen(this));
        refresh();
    }

    //***Helper Methods***//

    private void refresh() {
        visFrame.revalidate();
        visFrame.repaint();
    }


}
