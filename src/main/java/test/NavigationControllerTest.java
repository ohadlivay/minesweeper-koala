package main.java.test;

import main.java.controller.NavigationController;
import javax.swing.JFrame;

public class NavigationControllerTest {

    public static void runTestNav() throws Exception {
        JFrame frame = new JFrame();
        NavigationController nav = new NavigationController(frame);
        frame.setSize(1000, 700);
        frame.setVisible(true);


        nav.goToGame();
        System.out.println("Going to game...");
        if (frame.getContentPane() == null) {
            throw new Exception("goToGame() did not set a content pane");
        }
        Thread.sleep(1000);

        nav.goToHistory();
        System.out.println("Going to history...");
        if (frame.getContentPane() == null) {
            throw new Exception("goToHistory() did not set a content pane");
        }
        Thread.sleep(1000);

        nav.goToQuestionManager();
        System.out.println("Going to question manager...");
        if (frame.getContentPane() == null) {
            throw new Exception("goToQuestionManager() did not set a content pane");
        }
        Thread.sleep(1000);

        nav.goToHome();
        System.out.println("Going to home...");
        if (frame.getContentPane() == null) {
            throw new Exception("goToHome() did not set a content pane");
        }
        Thread.sleep(1000);

        System.out.println("All tests passed!");
        frame.dispose();

    }

    public static void main(String[] args) throws Exception {
        runTestNav();
    }

}
