package main.java.view;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.Board;
import main.java.model.GameSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameScreen {
    private final NavigationController nav;
    private final GameSession session; // Always holds the current game session

    private JPanel mainPanel;
    private JPanel centerPanel;

    private BoardLayout leftBoard;
    private BoardLayout rightBoard;
    private JLabel player1Label;
    private JLabel player2Label;


    public GameScreen(NavigationController nav, GameSession session) {
        this.nav = nav;
        this.session = session;
        initUI();
        setBoards(session.getLeftBoard(), session.getRightBoard());
        setPlayerNames(session.getLeftPlayerName(),session.getRightPlayerName());
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

    public void setPlayerNames(String leftName, String rightName) {
        player1Label.setText(leftName);
        player2Label.setText(rightName);
    }


    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //top panel holds player names
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        Font font = new Font("Segoe UI Black", Font.BOLD, 14);

        player1Label = new JLabel();
        player1Label.setForeground(Color.WHITE);
        player1Label.setFont(font);

        player2Label = new JLabel();
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

        // TEMP BUTTON FOR TESTING GAME SAVES
        JButton endGameButton = new JButton("End Game");
        endGameButton.setBackground(new Color(10, 10, 10));
        endGameButton.setBorder(BorderFactory.createLineBorder(new Color(70, 80, 100), 2));
        endGameButton.setFocusPainted(false);
        endGameButton.setPreferredSize(new Dimension(72, 36));
        endGameButton.setContentAreaFilled(true);
        endGameButton.setForeground(Color.WHITE);
        bottomPanel.add(endGameButton, BorderLayout.EAST);

        endGameButton.addActionListener(e -> {
            GameSessionController.getInstance().endGame(this.session,this.nav);
        });

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
