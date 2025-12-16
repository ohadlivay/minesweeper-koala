package main.java;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import main.java.controller.NavigationController;
import main.java.model.BoardGenerator;
import main.java.model.GameDifficulty;
import main.java.util.GameDataCSVManager;
import main.java.util.QuestionCSVManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {

        /*optional FlatLef look and feel
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }*/

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
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }


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
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setSize(1200, 640);
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

            });
        }


}
