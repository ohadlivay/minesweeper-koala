package main.java;

import main.java.controller.NavigationController;
import main.java.model.BoardGenerator;
import main.java.model.GameDifficulty;
import main.java.test.Testable;
import main.java.util.GameDataCSVManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //ohad's tests:
        // (this will be replaced with smarter tests)
        BoardGenerator testBoard = new BoardGenerator(GameDifficulty.HARD);
        System.out.println(testBoard.runClassTests());
        //end of ohad's tests

        launchSystem();
    }

        public static void launchSystem() {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Koala Minesweeper");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 640);   // temp size!
                frame.setResizable(false);

                try {
                    NavigationController nav = NavigationController.getInstance(frame);
                    nav.goToHome();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    GameDataCSVManager.readGameDataListFromCSV("GameHistory.csv");
                } catch (Exception e) {
                    System.err.println("Error launching system: " + e.getMessage());
                }



            });
        }


}
