package main.java.view;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class GameScreen extends JPanel implements PointsListener, HealthListener, MinesLeftListener {
    private final NavigationController nav;
    private final GameSession session; // Always holds the current game session

    private JPanel mainPanel;
    private JPanel centerPanel;

    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel player1MinesLeftLabel;
    private JLabel player2MinesLeftLabel;
    private JLabel healthLabel;
    private JLabel pointsLabel;

    private Color componentColor;


    public GameScreen(NavigationController nav, GameSession session) {
        this.nav = nav;
        this.session = session;
        this.session.setPointsListener(this);
        this.session.setHealthListener(this);
        this.session.getLeftBoard().setMinesLeftListener(this);
        this.session.getRightBoard().setMinesLeftListener(this);

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
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        Font font = new Font("Segoe UI Black", Font.BOLD, 16);

        player1Label = new JLabel();
        player1Label.setForeground(ColorsInUse.TEXT.get());
        player1Label.setFont(font);

        player2Label = new JLabel();
        player2Label.setForeground(ColorsInUse.TEXT.get());
        player2Label.setFont(font);

        player1MinesLeftLabel = new JLabel("x" + session.getLeftBoard().getMinesLeft());
        player1MinesLeftLabel.setForeground(ColorsInUse.TEXT.get());
        player1MinesLeftLabel.setFont(font);
        java.net.URL bombUrl = getClass().getResource("/bomb.png");
        if (bombUrl != null) { ImageIcon icon = new ImageIcon(bombUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            player1MinesLeftLabel.setIcon(new ImageIcon(scaled));
            player1MinesLeftLabel.setIconTextGap(10);
        }

        player2MinesLeftLabel = new JLabel("x" + session.getRightBoard().getMinesLeft());
        player2MinesLeftLabel.setForeground(ColorsInUse.TEXT.get());
        player2MinesLeftLabel.setFont(font);
        if (bombUrl != null) { ImageIcon icon = new ImageIcon(bombUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            player2MinesLeftLabel.setIcon(new ImageIcon(scaled));
            player2MinesLeftLabel.setIconTextGap(10);
        }

        // Create panels for left player and right player
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        // Add items into each side panel
        leftPanel.add(player1Label);
        leftPanel.add(player1MinesLeftLabel, BorderLayout.EAST);

        rightPanel.add(player2MinesLeftLabel, BorderLayout.WEST);
        rightPanel.add(player2Label);

        // Add those sub-panels to the topPanel
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        topPanel.setBorder(new EmptyBorder(20, 90, 10, 90));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //center panel holds the boardlayouts
        centerPanel = new JPanel();
        centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        healthLabel = new JLabel("x" + session.getHealthPool());
        healthLabel.setForeground(ColorsInUse.TEXT.get());
        healthLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        java.net.URL heartUrl = getClass().getResource("/heart.png");
        if (heartUrl != null) {
            ImageIcon icon = new ImageIcon(heartUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            healthLabel.setIcon(new ImageIcon(scaled));
            healthLabel.setIconTextGap(10);
        }
        statsPanel.add(healthLabel);

        pointsLabel = new JLabel("Score: " + session.getPoints());
        pointsLabel.setForeground(ColorsInUse.TEXT.get());
        pointsLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        pointsLabel.setOpaque(true);
        pointsLabel.setBackground(ColorsInUse.POINTS.get());
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
        endGameButton.setBackground(ColorsInUse.BTN_COLOR.get());
        endGameButton.setFocusPainted(false);
        endGameButton.setPreferredSize(new Dimension(72, 36));
        endGameButton.setContentAreaFilled(true);
        endGameButton.setForeground(ColorsInUse.TEXT.get());
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

        button.setBackground(ColorsInUse.BTN_COLOR.get());
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
        System.out.println("Points updated to: " + newPoints);
        pointsLabel.setText("Score: " + newPoints);
    }

    public void updateMinesLeft(int minesLeft, Board board) {
        if(board == session.getLeftBoard()) player1MinesLeftLabel.setText("x" + minesLeft);
        else player2MinesLeftLabel.setText("x" + minesLeft);

    }

    @Override
    public void onHealthChanged(int newHealth) {
        healthLabel.setText("x" + newHealth);
    }



}
