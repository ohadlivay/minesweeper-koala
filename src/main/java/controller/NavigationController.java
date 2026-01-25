package main.java.controller;

import main.java.util.SoundManager;
import main.java.view.GameHistoryScreen;
import main.java.view.GameScreen;
import main.java.view.QuestionManagerScreen;
import main.java.view.StartScreen;

import javax.swing.*;

public class NavigationController {
    private static NavigationController instance;
    private final JFrame visFrame;

    private NavigationController(JFrame frame) {
        this.visFrame = frame;
    }

    public static NavigationController getInstance(JFrame frame) {
        if (instance == null) {
            instance = new NavigationController(frame);
            OverlayController.getInstance(instance);
        }
        return instance;
    }

    public static NavigationController getInstance() {
        return instance;
    }

    // ***Methods to navigate between screens***//

    public void goToHome() {
        SoundManager.getInstance().playBackgroundMusic("/sounds/76 Load Game.wav");
        StartScreen startScreen = new StartScreen(this);
        visFrame.setContentPane(startScreen.getMainPanel());
        refresh();
    }

    public void goToGame() {
        SoundManager.getInstance().playBackgroundMusic("/sounds/62 Mines (Star Lumpy).wav");
        GameSessionController gsc = GameSessionController.getInstance();
        GameScreen gameScreen = gsc.startNewGame(this);
        visFrame.setContentPane(gameScreen.getMainPanel());
        refresh();
    }

    public void goToHistory() {
        HistoryController hsc = HistoryController.getInstance();
        GameHistoryScreen historyScreen = new GameHistoryScreen(this);
        hsc.setView(historyScreen);
        hsc.refreshGamesList();
        visFrame.setContentPane(historyScreen.getMainPanel());
        refresh();
    }

    public void goToQuestionManager() {
        QuestionManagerScreen questionManager = new QuestionManagerScreen(this);
        QuestionManagerController qmc = QuestionManagerController.getInstance();
        qmc.setView(questionManager);
        qmc.refreshQuestionList();
        visFrame.setContentPane(questionManager.getMainPanel());
        refresh();
    }

    public JFrame getVisFrame() {
        return visFrame;
    }

    // ***Helper Methods***//

    private void refresh() {
        visFrame.revalidate();
        visFrame.repaint();
    }

}
