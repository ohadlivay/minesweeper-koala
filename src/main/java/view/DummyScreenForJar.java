package main.java.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class DummyScreenForJar extends JPanel {
    public DummyScreenForJar() {

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

