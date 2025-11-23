package main.java;

import main.java.controller.NavigationController;
import main.java.model.Board;
import main.java.model.BoardGenerator;
import main.java.model.GameDifficulty;
import main.java.test.SmokeTest;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //ohad's tests:
        // (this will be replaced with smarter tests)
        BoardGenerator testBoard = new BoardGenerator(GameDifficulty.MEDIUM);
        System.out.println(testBoard.runClassTests());
        //end of ohad's tests
        try {
            SmokeTest.runAllTests(args);
        } catch (Exception e) {
            System.out.println("Tests failed:");
            e.printStackTrace();
            return;
        }
        launchSystem();
    }

        public static void launchSystem() {
//            DummyScreenForJar ds4j = new DummyScreenForJar();
//            ds4j.setVisible(true);

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Koala Minesweeper");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 640);   // temp size!

                NavigationController nav = new NavigationController(frame);
                nav.goToHome();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);


            });
        }
    }
