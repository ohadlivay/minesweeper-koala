package main.java.controller;

import javax.swing.JFrame;

import main.java.view.*;

public class NavigationController {
    private static NavigationController instance;
    private final JFrame visFrame;

    private NavigationController(JFrame frame) {
        this.visFrame = frame;
    }

    public static NavigationController getInstance(JFrame frame) {
        if (instance == null) {
            instance = new NavigationController(frame);
        }
        else if (instance.getVisFrame() != frame) {

        }
        return instance;
    }

    public static NavigationController getInstance() {
        return instance;
    }

    //***Methods to navigate between screens***//

    public void goToHome() {
        StartScreen startScreen = new StartScreen(this);
        visFrame.setContentPane(startScreen.getMainPanel());
        refresh();
    }

    public void goToGame() {
        GameSessionController gsc = GameSessionController.getInstance();
        GameScreen gameScreen = gsc.startNewGame(this);
        visFrame.setContentPane(gameScreen.getMainPanel());
        refresh();
    }


    public void goToHistory() {
        GameHistoryScreen gameHistoryScreen = new GameHistoryScreen(this);
        visFrame.setContentPane(gameHistoryScreen.getMainPanel());
        refresh();
    }

    public void goToQuestionManager() {
        QuestionManagerScreen questionManager = new QuestionManagerScreen(this);
        visFrame.setContentPane(questionManager.getMainPanel());
        refresh();
    }

    public JFrame getVisFrame() {
        return visFrame;
    }

    //***Helper Methods***//

    private void refresh() {
        visFrame.revalidate();
        visFrame.repaint();
    }


}
