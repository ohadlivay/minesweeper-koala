package main.java.view;

import main.java.controller.NavigationController;
import main.java.model.Board;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameScreen {
    private final NavigationController nav;

    private JPanel mainPanel;
    private JPanel centerPanel;
    private BoardLayout leftBoard;
    private BoardLayout rightBoard;

    public GameScreen(NavigationController nav) {
        this.nav = nav;
        initUI();
    }

    public void setBoards(Board left, Board right) {
        leftBoard = new BoardLayout(left);
        rightBoard = new BoardLayout(right);

        centerPanel.add(leftBoard);
        centerPanel.add(Box.createHorizontalStrut(50)); // gap
        centerPanel.add(rightBoard);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //top panel holds player names
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

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //center panel holds the boardlayouts
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // bottom panel holds the home button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton homeButton = createHomeButton();
        homeButton.addActionListener(e -> nav.goToHome());
        bottomPanel.add(homeButton, BorderLayout.WEST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createHomeButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(72, 36));
        java.net.URL icon = getClass().getResource("/home.png");
        if (icon != null) {
            button.setIcon(new ImageIcon(icon));
        }

        button.setBackground(new Color(10, 10, 10));
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 80, 100), 2));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);

        return button;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
