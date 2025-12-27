package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;
import main.java.model.QuestionDifficulty;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class SettingsOverlay extends OverlayView {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonBack;
    private JLabel difficultyLabel;
    private JButton btnEasy;
    private JButton btnMedium;
    private JButton btnHard;
    private JTextField player1Name;
    private JTextField player2Name;
    private JLabel player1Label;
    private JLabel player2Label;
    private GameDifficulty selectedDifficulty;

    public SettingsOverlay(NavigationController nav) {
        super(nav, true);
        initUI();

        buttonStart.addActionListener(e -> onOK());
        buttonBack.addActionListener(e -> onCancel());

        // call onCancel() when X is clicked
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        btnEasy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.EASY;
                btnEasy.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),new EmptyBorder(5,5,5,5)));
                System.out.println("Selected Difficulty: EASY");
            }
        });

        btnMedium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.MEDIUM;
                btnMedium.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),new EmptyBorder(5,5,5,5)));
                System.out.println("Selected Difficulty: MEDIUM");
            }
        });

        btnHard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.HARD;
                btnHard.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),new EmptyBorder(5,5,5,5)));
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
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        title.setForeground(ColorsInUse.TEXT.get());
        titlePanel.add(title);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        difficultyPanel.setBackground(ColorsInUse.BG_COLOR.get());

        btnEasy = createKoalaButton("/green-koala-pixel.png", "Easy", "9x9", GameDifficulty.EASY);
        btnMedium = createKoalaButton("/yellow-koala-pixel.png", "Medium", "13x13", GameDifficulty.MEDIUM);
        btnHard = createKoalaButton("/red-koala-pixel.png", "Hard", "16x16", GameDifficulty.HARD);

        difficultyPanel.add(btnEasy);
        difficultyPanel.add(btnMedium);
        difficultyPanel.add(btnHard);
        gbc.gridy = 0;
        centerPanel.add(difficultyPanel, gbc);

        JPanel namesPanel = new JPanel(new GridLayout(1, 2, 60, 0));
        namesPanel.setBackground(ColorsInUse.BG_COLOR.get());
        namesPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        namesPanel.add(createInputGroup("Player 1", player1Name = createTextField()));
        namesPanel.add(createInputGroup("Player 2", player2Name = createTextField()));

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(namesPanel, gbc);

        buttonStart = createButton("START");

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(30, 0, 0, 0);
        centerPanel.add(buttonStart, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());

        buttonBack = createButton("Back");
        buttonBack.addActionListener(e -> onCancel());
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
        if (selectedDifficulty == null) {
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
        GameSessionController.getInstance().setupGame(player1, player2, selectedDifficulty);
        nav.goToGame();
        close();
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



    // HELPER METHODS //

    private void resetSelection() {
        btnEasy.setBorder(new EmptyBorder(7, 7, 7, 7));
        btnMedium.setBorder(new EmptyBorder(7, 7, 7, 7));
        btnHard.setBorder(new EmptyBorder(7, 7, 7, 7));
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

    private JPanel createInputGroup(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(ColorsInUse.BG_COLOR.get());

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setForeground(ColorsInUse.TEXT.get());
        label.setFont(new Font("Segoe UI Black", Font.BOLD, 18));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(150, 30));
        field.setMinimumSize(new Dimension(150, 30));
        field.setMaximumSize(new Dimension(150, 30));
        field.setBackground(ColorsInUse.BG_COLOR.get());
        field.setForeground(ColorsInUse.TEXT.get());
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setMinimumSize(new Dimension(150, 45));
        btn.setMaximumSize(new Dimension(150, 45));
        btn.setBackground(ColorsInUse.BTN_COLOR.get());
        btn.setForeground(ColorsInUse.TEXT.get());
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createBevelBorder(0));
        return btn;
    }

    private JButton createKoalaButton(String resourcePath, String text, String tooltip, GameDifficulty difficulty) {
        JButton btn = new JButton(text);
        btn.setToolTipText(tooltip);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        Dimension size = new Dimension(100, 120);
        btn.setPreferredSize(size);
        btn.setMinimumSize(size);
        btn.setMaximumSize(size);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);
        btn.setFont(FontsInUse.PIXEL.getSize(24f));
        btn.setForeground(ColorsInUse.TEXT.get());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            btn.setText(tooltip);
        }
        btn.addActionListener(e -> {
            this.selectedDifficulty = difficulty;
            updateSelection();
        });
        return btn;
    }

    //visual indication for difficulty selected
    private void updateSelection() {
        Color selectedColor = ColorsInUse.TEXT.get();
        Color unselectedColor = new Color(0,0,0,0);
        btnEasy.setBorder(new LineBorder(selectedDifficulty == GameDifficulty.EASY ? selectedColor : unselectedColor, 3));
        btnMedium.setBorder(new LineBorder(selectedDifficulty == GameDifficulty.MEDIUM ? selectedColor : unselectedColor, 3));
        btnHard.setBorder(new LineBorder(selectedDifficulty == GameDifficulty.HARD ? selectedColor : unselectedColor, 3));
    }







}
