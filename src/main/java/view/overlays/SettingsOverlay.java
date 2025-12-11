package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;
import main.java.view.ColorsInUse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SettingsOverlay extends OverlayView {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonBack;
    private JLabel difficultyLabel;
    private JLabel easy;
    private JLabel medium;
    private JLabel hard;
    private JTextField player1Name;
    private JTextField player2Name;
    private JLabel player1Label;
    private JLabel player2Label;
    private GameDifficulty difficulty;

    public SettingsOverlay(NavigationController nav) {
        super(nav);
        initUI();

        buttonStart.addActionListener(e -> onOK());
        buttonBack.addActionListener(e -> onCancel());

        // call onCancel() when X is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        easy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                difficulty = GameDifficulty.EASY;
                easy.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),new EmptyBorder(5,5,5,5)));
                System.out.println("Selected Difficulty: EASY");
            }
        });

        medium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                difficulty = GameDifficulty.MEDIUM;
                medium.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),new EmptyBorder(5,5,5,5)));
                System.out.println("Selected Difficulty: MEDIUM");
            }
        });

        hard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                difficulty = GameDifficulty.HARD;
                hard.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),new EmptyBorder(5,5,5,5)));
                System.out.println("Selected Difficulty: HARD");
            }
        });

    }

    private void initUI() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(ColorsInUse.BG_COLOR.get());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(700, 500));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ColorsInUse.BG_COLOR.get());
        JLabel title = new JLabel("Choose Difficulty:");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(ColorsInUse.TEXT.get());
        titlePanel.add(title);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        difficultyPanel.setBackground(ColorsInUse.BG_COLOR.get());

        easy = new JLabel("Easy", SwingConstants.CENTER);
        easy.setPreferredSize(new Dimension(80, 80));
        easy.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        easy.setForeground(ColorsInUse.TEXT.get());
        easy.setBorder(new EmptyBorder(5, 5, 5, 5));

        medium = new JLabel("Medium", SwingConstants.CENTER);
        medium.setPreferredSize(new Dimension(80, 80));
        medium.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        medium.setForeground(ColorsInUse.TEXT.get());
        medium.setBorder(new EmptyBorder(5, 5, 5, 5));

        hard = new JLabel("Hard", SwingConstants.CENTER);
        hard.setPreferredSize(new Dimension(80, 80));
        hard.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        hard.setForeground(ColorsInUse.TEXT.get());
        hard.setBorder(new EmptyBorder(5, 5, 5, 5));

        difficultyPanel.add(easy);
        difficultyPanel.add(medium);
        difficultyPanel.add(hard);
        gbc.gridy = 0;
        centerPanel.add(difficultyPanel, gbc);

        JPanel namesPanel = new JPanel(new GridLayout(1, 2, 60, 0)); // 1 Row, 2 Cols, 60px gap
        namesPanel.setBackground(ColorsInUse.BG_COLOR.get());
        namesPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        player1Name = new JTextField();
        player1Name.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        player1Name.setForeground(ColorsInUse.TEXT.get());
        player1Name.setPreferredSize(new Dimension(100, 35));
        player1Name.setBackground(ColorsInUse.INPUT_FIELD.get());
        player1Name.setCaretColor(ColorsInUse.TEXT.get());
        player1Name.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        player2Name = new JTextField();
        player2Name.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        player2Name.setForeground(ColorsInUse.TEXT.get());
        player2Name.setPreferredSize(new Dimension(100, 35));
        player2Name.setBackground(ColorsInUse.INPUT_FIELD.get());
        player2Name.setCaretColor(ColorsInUse.TEXT.get());
        player2Name.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        namesPanel.add(player1Name);
        namesPanel.add(player2Name);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(namesPanel, gbc);

        buttonStart = new JButton("START");
        buttonStart.setPreferredSize(new Dimension(150, 45));
        buttonStart.setBackground(ColorsInUse.BTN_COLOR.get());
        buttonStart.setForeground(ColorsInUse.TEXT.get());
        buttonStart.setFocusPainted(false);
        buttonStart.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        buttonStart.setBorder(BorderFactory.createBevelBorder(0));
        buttonStart.addActionListener(e->onOK());

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(30, 0, 0, 0); // Margin top
        centerPanel.add(buttonStart, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());

        buttonBack = new JButton("Back");
        buttonBack.addActionListener(e -> onCancel());
        buttonBack.setPreferredSize(new Dimension(150, 45));
        buttonBack.setBackground(ColorsInUse.BTN_COLOR.get());
        buttonBack.setForeground(ColorsInUse.TEXT.get());
        buttonBack.setFocusPainted(false);
        buttonBack.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
        buttonBack.setBorder(BorderFactory.createBevelBorder(0));
        buttonBack.addActionListener(e->onOK());
        bottomPanel.add(buttonBack);

        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);

    }

    // pass the user input, close the overlay and go to the game screen
    private void onOK() {
        String player1 = getPlayer1Name();
        String player2 = getPlayer2Name();

        // check for difficulty
        if (difficulty == null) {
            JOptionPane.showMessageDialog(null, "Please select a difficulty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // do not proceed if difficulty is not selected
        }

        //check for player names
        if (player1.trim().isEmpty() || player2.trim().isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this, nameWarning(player1, player2), "Names not chosen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.NO_OPTION) {
                return; // do not proceed if user selects NO
            }
            if (player1.trim().isEmpty()) {
                player1 = "Player 1";
            }
            if (player2.trim().isEmpty()) {
                player2 = "Player 2";
            }
        }
        GameSessionController.getInstance().setupGame(player1, player2, difficulty);
        nav.goToGame();
        close();
    }

    private void resetSelection() {
        easy.setBorder(null);
        medium.setBorder(null);
        hard.setBorder(null);
    }

    private String nameWarning (String player1, String player2) {
        if (player1.trim().isEmpty() && player2.trim().isEmpty()) {
            return "Names for both players not chosen\n Continue with default names?\n - Player 1\n - Player 2";
        } else if (player1.trim().isEmpty()) {
            return "Name for player 1 not chosen\nContinue with default name?\n - Player 1";
        } else {
            return "Name for player 2 not chosen\n Continue with default name?\n - Player 2";
        }

    }

    private void onCancel() {
        close();
    }

    // add getters to retrieve user input
    public String getPlayer1Name() {
        return player1Name.getText();
    }

    public String getPlayer2Name() {
        return player2Name.getText();
    }



}
