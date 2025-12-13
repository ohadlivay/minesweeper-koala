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
    private JLabel feedLabel;

    private int lastHealth;
    private int lastPoints;
    private final Timer uiUpdateTimer;
    private int pendingPointChange = 0;
    private int pendingHealthChange = 0;

    private Color componentColor;


    public GameScreen(NavigationController nav, GameSession session) {
        this.nav = nav;
        this.session = session;
        this.session.setPointsListener(this);
        this.session.setHealthListener(this);
        this.session.getLeftBoard().setMinesLeftListener(this);
        this.session.getRightBoard().setMinesLeftListener(this);
        this.lastHealth = session.getHealthPool();
        this.lastPoints = session.getPoints();

        //collect updates for 150ms
        this.uiUpdateTimer = new Timer(150, e -> processPendingUpdates());
        this.uiUpdateTimer.setRepeats(false);

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

        feedLabel = new JLabel("Welcome! Click a tile to start.", SwingConstants.CENTER);
        feedLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
        feedLabel.setForeground(ColorsInUse.TEXT.get());
        feedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

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
        topPanel.add(feedLabel, BorderLayout.CENTER);
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
        int diff = newPoints - lastPoints;
        if(diff != 0){
            //accumulate change and restart the timer
            pendingPointChange += diff;
            uiUpdateTimer.restart();
        }
        lastPoints = newPoints;
        pointsLabel.setText("Score: " + newPoints);
    }

    public void updateMinesLeft(int minesLeft, Board board) {
        if(board == session.getLeftBoard()) player1MinesLeftLabel.setText("x" + minesLeft);
        else player2MinesLeftLabel.setText("x" + minesLeft);

    }

    @Override
    public void onHealthChanged(int newHealth) {
        int diff = newHealth - lastHealth;
        if(diff != 0){
            //accumulate change and restart the timer
            pendingHealthChange += diff;
            uiUpdateTimer.restart();
        }
        lastHealth = newHealth;
        healthLabel.setText("x" + newHealth);
    }

    //method used to display points/health feedback only after accumulating all the changes after the click
    //this method is also responsible for setting the feed label messages based on the source of the click
    private void processPendingUpdates() {
        if (pendingPointChange == 0 && pendingHealthChange == 0) {
            return;
        }

        String msg = "";
        Color msgColor = ColorsInUse.TEXT.get();
        Color goodColor = new Color(46, 204, 113);
        Color badColor = new Color(231, 76, 60);

        //handle health + points (can only be triggered by surprise/question tiles)
        // ****** can change surprise/question messages to be more specific after we implement GameEventListener ********
        if (pendingPointChange != 0 && pendingHealthChange != 0) {
            boolean goodOutcome = pendingPointChange > 0;

            //update feed label text
            if (goodOutcome) {
                msg = "Special Tile Bonus! (+" + pendingPointChange + " pts, +" + pendingHealthChange + " HP)";
                msgColor = goodColor;
            } else {
                msg = "Special Tile Penalty... (" + pendingPointChange + " pts, " + pendingHealthChange + " HP)";
                msgColor = badColor;
            }
        }
            //health only (can only happen from mine reveal)
        else if (pendingHealthChange < 0 ){
            msg = "BOOM! You hit a mine!";
            msgColor = badColor;
        }
        //only points change
        else if (pendingPointChange !=0) {
            if (pendingPointChange == 1) {
                msg = "Safe move! (+1)";
                msgColor = goodColor;
            }
            else if (pendingPointChange == -3) {
                msg = "Incorrect flag! (-3)";
                msgColor = badColor;
            }
            else if (pendingPointChange > 1) {
                msg = "Great! Cleared " + pendingPointChange + " tiles.";
                msgColor = goodColor;
            }
            else {
                // Negative points (other than -3) usually means activation cost or penalty
                msg = "Points lost (" + pendingPointChange + ")";
                msgColor = badColor;
            }
        }
        //update the feed label msg
        feedLabel.setText(msg);
        feedLabel.setForeground(msgColor);

        //trigger floating numbers animation
        if (pendingPointChange != 0) {
            boolean isPositive = pendingPointChange > 0;
            String text = (isPositive ? "+" : "") + pendingPointChange;
            Color color = isPositive ? Color.GREEN : Color.RED;
            floatingNumber(pointsLabel, text, color, isPositive);
        }

        if (pendingHealthChange != 0) {
            boolean isPositive = pendingHealthChange > 0;
            String text = (isPositive ? "+" : "") + pendingHealthChange;
            Color color = isPositive ? Color.GREEN : Color.RED;
            floatingNumber(healthLabel, text, color, isPositive);
        }

        //reset counters
        pendingPointChange = 0;
        pendingHealthChange = 0;
    }

    //animation for immediate points/health feedback
    private void floatingNumber(JComponent target, String text, Color color, boolean isUp) {
        JRootPane rootPane = SwingUtilities.getRootPane(target);
        if (rootPane == null) return;

        JLabel floatLabel = new JLabel(text);
        floatLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 20));
        floatLabel.setForeground(color);

        Point screenPos = target.getLocationOnScreen();
        Point rootPos = rootPane.getLocationOnScreen();
        int x = screenPos.x - rootPos.x + (target.getWidth() / 2) - 15;
        int y = screenPos.y - rootPos.y;

        floatLabel.setBounds(x, y, 100, 30);

        JLayeredPane layeredPane = rootPane.getLayeredPane();
        layeredPane.add(floatLabel, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();

        //animation timer
        int distance = 50;
        int step;
        if(!isUp) {
            step = 2;
        } else {
            step = -2;
        }
        Timer timer = new Timer(40, null);
        timer.addActionListener(e -> {
            Point p = floatLabel.getLocation();
            floatLabel.setLocation(p.x, p.y + step);

            //define when to stop based on the direction
            boolean finished;
            if (isUp) {
                finished = (p.y < y - distance);
            }
            else {
                finished = (p.y > y + distance);
            }

            if (finished) {
                layeredPane.remove(floatLabel);
                layeredPane.repaint();
                timer.stop();
            }

        });
        timer.start();
    }



}
