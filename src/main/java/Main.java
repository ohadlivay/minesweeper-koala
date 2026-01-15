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

/**
 * Entry point for the Koala Minesweeper application.
 * Handles the initialization of the UI, resources, and core controllers.
 */
public class Main {
    public static void main(String[] args) {

        // Set the application UI theme to Nimbus for a consistent look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    javax.swing.UIManager.getLookAndFeelDefaults().put("Button.contentMargins",
                            new java.awt.Insets(5, 10, 5, 10));
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            // If Nimbus is not available, fall back to default and log the error.
            // In a production app, we might want to handle this more gracefully or just
            // ignore it.
            throw new RuntimeException(e);
        }

        BoardGenerator testBoard = new BoardGenerator(GameDifficulty.HARD);
        System.out.println(testBoard.runClassTests());

        launchSystem();
    }

    /**
     * Initializes the main application window and starts the system.
     * Sets up the main frame, navigation controller, loads data, and preloads
     * sounds.
     */
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
            // Ensure the application doesn't close immediately on click, so we can show a
            // confirmation dialog
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(1200, 725);
            frame.setResizable(false);

            // Add a confirmation dialog when the user tries to close the window
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int option = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to exit?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            });

            try {
                // Initialize NavigationController and set the initial view
                NavigationController nav = NavigationController.getInstance(frame);
                nav.goToHome();

                frame.setLocationRelativeTo(null); // Center the window
                frame.setVisible(true);

                // Load game data and questions from CSV files
                GameDataCSVManager.readGameDataListFromCSV("GameHistory.csv");
                QuestionCSVManager.readQuestionsFromCSV("Questions.csv");
            } catch (Exception e) {
                System.err.println("Error launching system: " + e.getMessage());
            }

            // Preload sound assets to avoid lag during playback
            try {
                SoundManager.getInstance().preload();
            } catch (Exception e) {
                System.err.println("Error preloading sounds: " + e.getMessage());
            }
        });
    }

}
