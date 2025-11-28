package main.java.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameScreen extends JFrame {

    public GameScreen() {
        setTitle("Koala Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 640);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.BLACK);
        root.setBorder(new EmptyBorder(10, 10, 10, 10)); // outer margin
        setContentPane(root);

        //top panel holds the player names
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        Font font = new Font("Segoe UI Black", Font.BOLD, 14);

        JLabel player1Label = new JLabel("Player 1");
        player1Label.setForeground(Color.WHITE);
        player1Label.setFont(font);

        JLabel player2Label = new JLabel("Player 2");
        player2Label.setForeground(Color.WHITE);
        player2Label.setFont(font);

        topPanel.add(player1Label, BorderLayout.WEST);
        topPanel.add(player2Label, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(20, 5, 5, 5)); // spacing under labels

        root.add(topPanel, BorderLayout.NORTH);

        //center panel holds the boards
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        JPanel leftBoard = createBoardPanel();
        JPanel rightBoard = createBoardPanel();

        centerPanel.add(leftBoard);
        centerPanel.add(Box.createHorizontalStrut(50)); // middle gap (the “bar”)
        centerPanel.add(rightBoard);

        root.add(centerPanel, BorderLayout.CENTER);

        //bottom panel holds the home button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton homeButton = createHomeButton();
        bottomPanel.add(homeButton, BorderLayout.WEST);

        root.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBoardPanel() {
        JPanel board = new JPanel();
        board.setBackground(Color.WHITE);
        board.setPreferredSize(new Dimension(520, 430));
        board.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        return board;
    }

    private JButton createHomeButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(72, 36));
        java.net.URL icon = getClass().getResource("/home.png");
        button.setIcon(new ImageIcon(icon));

        button.setBackground(new Color(10, 10, 10));
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 80, 100), 2));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);

        return button;
    }

}
