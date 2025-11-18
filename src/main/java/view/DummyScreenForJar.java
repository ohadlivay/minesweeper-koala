package main.java.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class DummyScreenForJar extends JFrame {
    public DummyScreenForJar() {
        super("Koala Minesweeper - Dummy Screen");

        // basic window setup
        setSize(400, 300);
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // simple label so we see something
        JLabel label = new JLabel("If you see this, the JAR works 🎉🐨", SwingConstants.CENTER);
        add(label);
    }

    // optional: standalone runner for quick tests from this class
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DummyScreenForJar screen = new DummyScreenForJar();
            screen.setVisible(true);
        });
    }
}

