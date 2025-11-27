package main.java;

import main.java.controller.NavigationController;
import main.java.model.BoardGenerator;
import main.java.model.GameDifficulty;
import main.java.test.Testable;

import javax.swing.*;

public class Main implements Testable {
    public static void main(String[] args) {
        /* tali's test:
         * This is a test for the Main class to ensure that the GUI components can be created and displayed without errors.
         * It creates a JFrame and uses the NavigationController to navigate to the home screen.
         * If the GUI components are created and displayed successfully, the test returns true; otherwise, it returns false.
         */
        Main mainTester = new Main();
        System.out.println("Main startup test: " + mainTester.runClassTests());
        //end of tali's test

        //ohad's tests:
        // (this will be replaced with smarter tests)
        BoardGenerator testBoard = new BoardGenerator(GameDifficulty.HARD);
        System.out.println(testBoard.runClassTests());
        //end of ohad's tests



        launchSystem();
    }

        public static void launchSystem() {
//            DummyScreenForJar ds4j = new DummyScreenForJar();
//            ds4j.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Koala Minesweeper");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 640);   // temp size!
                frame.setResizable(false);

                NavigationController nav = NavigationController.getInstance(frame);
                nav.goToHome();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);


            });
        }

    @Override
    public boolean runClassTests() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                JFrame testFrame = new JFrame("Koala Minesweeper - TEST");
                testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                testFrame.setSize(1200, 640);
                testFrame.setResizable(false);

                NavigationController navTest = new NavigationController(testFrame);
                navTest.goToHome();
                testFrame.dispose();
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
