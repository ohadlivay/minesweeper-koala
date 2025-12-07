package main.java.view;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.Board;
import main.java.model.GameSession;
import main.java.model.MinesLeftListener;
import main.java.model.PointsListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class GameScreen implements PointsListener, MinesLeftListener {
    private final NavigationController nav;
    private final GameSession session; // Always holds the current game session

    private JPanel mainPanel;
    private JPanel centerPanel;

    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel livesLabel;
    private JLabel pointsLabel;


    public GameScreen(NavigationController nav, GameSession session) {
        this.nav = nav;
        this.session = session;
        initUI();
        setBoards(session.getLeftBoard(), session.getRightBoard());
        setPlayerNames(session.getLeftPlayerName(),session.getRightPlayerName());
    }

    public void setBoards(Board left, Board right) {
        BoardLayout leftBoard = new BoardLayout(left);
        BoardLayout rightBoard = new BoardLayout(right);
        centerPanel.add(Box.createHorizontalGlue());
        centerPanel.add(leftBoard);
        centerPanel.add(Box.createHorizontalStrut(50)); // gap
        centerPanel.add(rightBoard);
        centerPanel.add(Box.createHorizontalGlue());
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void setPlayerNames(String leftName, String rightName) {
        player1Label.setText(leftName);
        player2Label.setText(rightName);
    }


    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 20, 20));
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
        centerPanel.setBackground(new Color(20, 20, 20));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        livesLabel = new JLabel("x" + session.getHealthPool());
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            livesLabel.setIcon(new ImageIcon(scaled));
            livesLabel.setIconTextGap(10);
        }
        statsPanel.add(livesLabel);

        pointsLabel = new JLabel("Score: " + session.getPoints());
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        pointsLabel.setOpaque(true);
        pointsLabel.setBackground(new Color(88, 124, 196));
        pointsLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        statsPanel.add(pointsLabel);

        // bottom panel holds the home button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton homeButton = createHomeButton();
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

        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setOpaque(false);
        southContainer.add(statsPanel);
        southContainer.add(Box.createVerticalStrut(10));
        southContainer.add(bottomPanel);

        mainPanel.add(southContainer, BorderLayout.SOUTH);

        endGameButton.addActionListener(e -> {
            try {
                GameSessionController.getInstance().endGame(this.session,this.nav);
                JOptionPane saved = new JOptionPane();
                endGameButton.setEnabled(false);
                saved.showMessageDialog(mainPanel, "Game Over! Game data saved.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane endGame = new JOptionPane();
                endGame.showMessageDialog(mainPanel, "Error saving game data!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

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
        button.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    mainPanel,
                    "Are you sure you want to return to the main menu?",
                    "Confirm Navigation",
                    JOptionPane.YES_NO_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                nav.goToHome();
        }
        });

        return button;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void onPointsChanged(int newPoints) {
        //When Points are visible, implement this method
    }

    @Override
    public void updateMinesLeft(int minesLeft) {
        //When mines left is visible, implement this method
    }
}
