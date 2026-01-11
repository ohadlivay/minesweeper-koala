package main.java;


import main.java.controller.NavigationController;
import main.java.model.BoardGenerator;
import main.java.model.GameDifficulty;
import main.java.util.GameDataCSVManager;
import main.java.util.QuestionCSVManager;
import main.java.util.SoundManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {

        //DON'T TOUCH THIS CODE - IT SETS THE WHOLE UI THEME
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    javax.swing.UIManager.getLookAndFeelDefaults().put("Button.contentMargins", new java.awt.Insets(5, 10, 5, 10));
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        //ohad's tests:
        BoardGenerator testBoard = new BoardGenerator(GameDifficulty.HARD);
        System.out.println(testBoard.runClassTests());
        //end of ohad's tests

        launchSystem();
    }

        public static void launchSystem() {
            SwingUtilities.invokeLater(() -> {

                JFrame frame = new JFrame("Koala Minesweeper");
                java.net.URL iconURL = Main.class.getResource("/green-koala-pixel.png");
                if (iconURL != null) {
                    ImageIcon icon = new ImageIcon(iconURL);
                    frame.setIconImage(icon.getImage());
                } else {
                    System.err.println("Could not find icon resource: /green-koala-pixel.png");
                }
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setSize(1200, 720);
                frame.setResizable(false);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        int option = JOptionPane.showConfirmDialog(
                                frame,
                                "Are you sure you want to exit?",
                                "Exit Confirmation",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (option == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });

                try {
                    NavigationController nav = NavigationController.getInstance(frame);
                    nav.goToHome();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    GameDataCSVManager.readGameDataListFromCSV("GameHistory.csv");
                    QuestionCSVManager.readQuestionsFromCSV("Questions.csv");
                } catch (Exception e) {
                    System.err.println("Error launching system: " + e.getMessage());
                }
                try {
                    SoundManager.getInstance().preload();
                } catch (Exception e) {
                    System.err.println("Error preloading sounds: " + e.getMessage());
                }
            });
        }


}
