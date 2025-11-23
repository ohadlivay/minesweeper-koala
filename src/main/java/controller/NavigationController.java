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
        StartScreen startScreen = new StartScreen(this);
        visFrame.setContentPane(startScreen.getMainPanel());
        refresh();
    }

    public void goToGame() {
        GameScreen gameScreen = new GameScreen(this);
        visFrame.setContentPane(gameScreen.getGamePanel());
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
