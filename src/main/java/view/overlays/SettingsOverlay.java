package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;
import main.java.model.Question;
import main.java.util.SoundManager;
import main.java.view.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SettingsOverlay extends OverlayView {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonBack;
    private JButton btnEasy;
    private JButton btnMedium;
    private JButton btnHard;
    private JTextField player1Name;
    private JTextField player2Name;
    private OutlinedLabel gameInfoTitle;
    private OutlinedLabel gameInfoStats;
    private GameDifficulty selectedDifficulty;
    private ColorsInUse player1Color;
    private ColorsInUse player2Color;
    private JPanel player1ColorPanel;
    private JPanel player2ColorPanel;
    private List<JButton> player1ColorButtons;
    private List<JButton> player2ColorButtons;

    private final int PlAYER_TEXT_LENGTH = 15;

    // Placeholder text used for name fields
    private static final String NAME_PLACEHOLDER = "Enter name here...";

    public SettingsOverlay(NavigationController nav) {
        super(nav, true);
        initUI();

        // Initialize default colors
        this.player1Color = ColorsInUse.CRIMSON;
        this.player2Color = ColorsInUse.SUNSET_ORANGE;

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
                btnEasy.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorsInUse.BOARD_ACTIVE_BORDER.get(), 2), new EmptyBorder(5, 5, 5, 5)));
                System.out.println("Selected Difficulty: EASY");
            }
        });

        btnMedium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.MEDIUM;
                btnMedium.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorsInUse.BOARD_ACTIVE_BORDER.get(), 2), new EmptyBorder(5, 5, 5, 5)));
                System.out.println("Selected Difficulty: MEDIUM");
            }
        });

        btnHard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.HARD;
                btnHard.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorsInUse.BOARD_ACTIVE_BORDER.get(), 2), new EmptyBorder(5, 5, 5, 5)));
                System.out.println("Selected Difficulty: HARD");
            }
        });
    }

    public SettingsOverlay(NavigationController nav, String player1, String player2, GameDifficulty difficulty) {
        this(nav);
        player1Name.setText(player1);
        player2Name.setText(player2);
        if (difficulty != null) {
            this.selectedDifficulty = difficulty;
            updateSelection();
        } else {
            resetSelection();
        }
    }

    public SettingsOverlay(NavigationController nav, String player1, String player2, GameDifficulty difficulty, ColorsInUse color1, ColorsInUse color2) {
        this(nav, player1, player2, difficulty);
        if (color1 != null) {
            this.player1Color = color1;
        }
        if (color2 != null) {
            this.player2Color = color2;
        }
    }

    private void initUI() {
        contentPane = new BackgroundPanel("/wood-bg.png");
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(700, 650));

        JPanel titlePanel = new JPanel();
        //titlePanel.setBackground(ColorsInUse.BG_COLOR.get());
        titlePanel.setOpaque(false);
        OutlinedLabel title = new OutlinedLabel("CHOOSE DIFFICULTY:", Color.BLACK, 6f);
        title.setFont(FontsInUse.PIXEL.getSize(52f));
        title.setForeground(ColorsInUse.TEXT.get());
        titlePanel.add(title);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        //centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        //difficultyPanel.setBackground(ColorsInUse.BG_COLOR.get());
        difficultyPanel.setOpaque(false);

        //create tooltip text
        String easyTip = "<html>" +
                "<b><font color='#027315'>EASY DIFFICULTY</font></b><br/>" +
                "<hr/>" +
                "<font color='#0cc42b'>Grid:</font> " + GameDifficulty.EASY.getRows() + "x" + GameDifficulty.EASY.getCols() + "<br/>" +
                "<font color='#0cc42b'>Mines:</font> " + GameDifficulty.EASY.getMineCount() + "<br/>" +
                "<font color='#0cc42b'>Activation Cost:</font> " + GameDifficulty.EASY.getActivationCost() + " pts" +
                "</html>";

        String medTip = "<html>" +
                "<b><font color='#c78800'>MEDIUM DIFFICULTY</font></b><br/>" +
                "<hr/>" +
                "<font color='#c78800'>Grid:</font> " + GameDifficulty.MEDIUM.getRows() + "x" + GameDifficulty.MEDIUM.getCols() + "<br/>" +
                "<font color='#c78800'>Mines:</font> " + GameDifficulty.MEDIUM.getMineCount() + "<br/>" +
                "<font color='#c78800'>Activation Cost:</font> " + GameDifficulty.MEDIUM.getActivationCost() + " pts" +
                "</html>";

        String hardTip = "<html>" +
                "<b><font color='#FF6B6B'>HARD DIFFICULTY</font></b><br/>" +
                "<hr/>" +
                "<font color='#FF6B6B'>Grid:</font> " + GameDifficulty.HARD.getRows() + "x" + GameDifficulty.HARD.getCols() + "<br/>" +
                "<font color='#FF6B6B'>Mines:</font> " + GameDifficulty.HARD.getMineCount() + "<br/>" +
                "<font color='#FF6B6B'>Activation Cost:</font> " + GameDifficulty.HARD.getActivationCost() + " pts" +
                "</html>";

        btnEasy = createKoalaButton("/green-koala-pixel.png", "Easy", easyTip, GameDifficulty.EASY);
        btnMedium = createKoalaButton("/yellow-koala-pixel.png", "Medium", medTip, GameDifficulty.MEDIUM);
        btnHard = createKoalaButton("/red-koala-pixel.png", "Hard", hardTip, GameDifficulty.HARD);



        difficultyPanel.add(btnEasy);
        difficultyPanel.add(btnMedium);
        difficultyPanel.add(btnHard);
        gbc.gridy = 0;
        centerPanel.add(difficultyPanel, gbc);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 15, 10, 15);
        centerPanel.add(difficultyPanel, gbc);

        // --- game info ---
        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setOpaque(false);
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.Y_AXIS));

        gameInfoTitle = new OutlinedLabel("", Color.BLACK, 4f);
        gameInfoTitle.setForeground(ColorsInUse.TEXT.get());
        gameInfoTitle.setFont(FontsInUse.PIXEL.getSize(28f));
        gameInfoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        gameInfoStats = new OutlinedLabel("", Color.BLACK, 4f);
        gameInfoStats.setForeground(ColorsInUse.TEXT.get());
        gameInfoStats.setFont(FontsInUse.PIXEL.getSize(28f));
        gameInfoStats.setAlignmentX(Component.CENTER_ALIGNMENT);

        gameInfoPanel.add(gameInfoTitle);
        gameInfoPanel.add(Box.createVerticalStrut(4)); // spacing between lines
        gameInfoPanel.add(gameInfoStats);

        gbc.gridy++;
        gbc.insets = new Insets(10, 15, 3, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        gameInfoPanel.setPreferredSize(new Dimension(560, 70));
        gameInfoPanel.setMinimumSize(new Dimension(560, 70));

        centerPanel.add(gameInfoPanel, gbc);

        // --- names panel ---
        JPanel namesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        //namesPanel.setBackground(ColorsInUse.BG_COLOR.get());
        namesPanel.setOpaque(false);
        namesPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        namesPanel.add(createInputGroup("Player 1", player1Name = createTextField()));
        namesPanel.add(createInputGroup("Player 2", player2Name = createTextField()));

        gbc.gridy++;
        gbc.insets = new Insets(5, 50, 5, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        centerPanel.add(namesPanel, gbc);

        // --- color selection panel ---
        JPanel colorPanel = new JPanel(new GridLayout(1, 2, 20, 0));
      //  colorPanel.setBackground(ColorsInUse.BG_COLOR.get());
        colorPanel.setOpaque(false);
        colorPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        player1ColorPanel = createColorSelectionGroup("Player 1 Board Color", 1);
        player2ColorPanel = createColorSelectionGroup("Player 2 Board Color", 2);

        colorPanel.add(player1ColorPanel);
        colorPanel.add(player2ColorPanel);

        gbc.gridy++;
        gbc.insets = new Insets(5, 50, 5, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        centerPanel.add(colorPanel, gbc);

        // --- bottom buttons: back on the left, start centered ---

        buttonStart = createButton("START");
        buttonStart.setPreferredSize(new Dimension(180, 50));

        buttonBack = createTransparentIconButton("/back-pixel-2.png", 60, 50);
        buttonBack.addActionListener(e -> onCancel());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(700, 70));

        // a little padding so the back button isn't glued to the edge
        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        backWrap.setOpaque(false);
        backWrap.add(buttonBack);

        // keep START visually centered
        JPanel startWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        startWrap.setOpaque(false);
        startWrap.add(buttonStart);

        // RIGHT SPACER — SAME WIDTH AS BACK
        JPanel rightSpacer = new JPanel();
        rightSpacer.setOpaque(false);
        rightSpacer.setPreferredSize(backWrap.getPreferredSize());

        bottomPanel.add(backWrap, BorderLayout.WEST);
        bottomPanel.add(startWrap, BorderLayout.CENTER);
        bottomPanel.add(rightSpacer, BorderLayout.EAST);



        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);


    }

    // pass the user input, close the overlay and go to the game screen
    private void onOK() {
        String player1 = getPlayer1Name().equals(NAME_PLACEHOLDER) ? "" : getPlayer1Name();
        String player2 = getPlayer2Name().equals(NAME_PLACEHOLDER) ? "" : getPlayer2Name();

        // check for difficulty
        if (selectedDifficulty == null) {
            JOptionPane.showMessageDialog(null, "Please select a difficulty.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // do not proceed if difficulty is not selected
        }

        if (player1.trim().length() > PlAYER_TEXT_LENGTH) {
            String mess = "Name for player 1 too long\n Maximum length is " + PlAYER_TEXT_LENGTH + " characters.";
            JOptionPane.showMessageDialog(this, mess, "Name too long", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (player2.trim().length() > PlAYER_TEXT_LENGTH) {
            String mess = "Name for player 2 too long\n Maximum length is " + PlAYER_TEXT_LENGTH + " characters.";
            JOptionPane.showMessageDialog(this, mess, "Name too long", JOptionPane.WARNING_MESSAGE);
            return;
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
        try {
            // Check to see if the system has enough questions to start a game
            GameSessionController.getInstance().setupGame(player1, player2, selectedDifficulty, player1Color, player2Color);
        } catch (IllegalStateException e) {
            // e.getMessage() contains the detailed "Current: X, Required: Y..." string we built
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
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

    public ColorsInUse getPlayer1Color() {
        return player1Color;
    }

    public ColorsInUse getPlayer2Color() {
        return player2Color;
    }


    // HELPER METHODS //

    private void resetSelection() {
        btnEasy.setBorder(new EmptyBorder(7, 7, 7, 7));
        btnMedium.setBorder(new EmptyBorder(7, 7, 7, 7));
        btnHard.setBorder(new EmptyBorder(7, 7, 7, 7));
    }

    private String nameWarning(String player1, String player2) {
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
        //panel.setBackground(ColorsInUse.BG_COLOR.get());
        panel.setOpaque(false);

        JLabel label = new OutlinedLabel(labelText, Color.BLACK, 4f);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(ColorsInUse.TEXT.get());
        label.setFont(FontsInUse.PIXEL.getSize(28f));

        textField.setPreferredSize(new Dimension(150, 30));
        textField.setMinimumSize(new Dimension(150, 30));
        textField.setMaximumSize(new Dimension(150, 30));

        JLabel bottomLabel = new OutlinedLabel("Max length: " + PlAYER_TEXT_LENGTH, Color.BLACK, 4f);
        bottomLabel.setFont(FontsInUse.PIXEL.getSize(20f));
        bottomLabel.setText(" ");
        bottomLabel.setForeground(new Color(0, 0, 0, 0));
        bottomLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(bottomLabel, BorderLayout.SOUTH);

        textField.addKeyListener(answerKeyListener(textField, label, bottomLabel));
        return panel;
    }

    FocusAdapter placeholderListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (!(e.getComponent() instanceof JTextComponent tc)) return;
            Object ph = tc.getClientProperty("placeholder");
            if (ph instanceof String placeholder && tc.getText().equals(placeholder)) {
                tc.setText("");
                tc.setForeground(ColorsInUse.TEXT.get());
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!(e.getComponent() instanceof JTextComponent tc)) return;
            if (tc.getText().trim().isEmpty()) {
                Object ph = tc.getClientProperty("placeholder");
                if (ph instanceof String placeholder) {
                    tc.setText(placeholder);
                    tc.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
                }
            }
        }
    };

    private JPanel createColorSelectionGroup(String labelText, int playerNumber) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        //panel.setBackground(ColorsInUse.BG_COLOR.get());
        panel.setOpaque(false);

        JLabel label = new OutlinedLabel(labelText, Color.BLACK, 4f);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(ColorsInUse.TEXT.get());
        label.setFont(FontsInUse.PIXEL.getSize(28f));

        JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 4, 8, 8));
        //colorButtonsPanel.setBackground(ColorsInUse.BG_COLOR.get());
        colorButtonsPanel.setOpaque(false);
        colorButtonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ColorsInUse[] warmColors = ColorsInUse.getBoardColors();
        ColorsInUse selectedColor = (playerNumber == 1) ? player1Color : player2Color;

        List<JButton> buttonsList = new ArrayList<>();

        for (ColorsInUse color : warmColors) {
            JButton colorBtn = createColorButton(color, playerNumber, selectedColor);
            buttonsList.add(colorBtn);
            colorButtonsPanel.add(colorBtn);
        }

        // Store the button list for this player
        if (playerNumber == 1) {
            player1ColorButtons = buttonsList;
        } else {
            player2ColorButtons = buttonsList;
        }

        JScrollPane scrollPane = new JScrollPane(colorButtonsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(280, 120));
        scrollPane.setMinimumSize(new Dimension(280, 100));
        scrollPane.setMaximumSize(new Dimension(280, 100));
        //scrollPane.getViewport().setBackground(ColorsInUse.BG_COLOR.get());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JButton createColorButton(ColorsInUse color, int playerNumber, ColorsInUse selectedColor) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(20, 20));
        btn.setBackground(color.get());
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);

        // Store the color in the button for later reference
        btn.putClientProperty("color", color);
        btn.putClientProperty("playerNumber", playerNumber);

        // Check if this color is taken by the other player
        boolean isTakenByOther = (playerNumber == 1) ? (color == player2Color) : (color == player1Color);
        boolean isSelected = (color == selectedColor);

        updateColorButtonAppearance(btn, color, isSelected, isTakenByOther);

        btn.addActionListener(e -> {
            if (!isTakenByOther) {
                if (playerNumber == 1) {
                    player1Color = color;
                } else {
                    player2Color = color;
                }
                soundManager.playOnce(SoundManager.SoundId.SELECTION);
                // Refresh all color buttons to show updated state
                refreshColorButtons();
            }
        });

        return btn;
    }

    private void updateColorButtonAppearance(JButton btn, ColorsInUse color, boolean isSelected, boolean isTaken) {
        if (isTaken) {
            // Disabled state: grayed out appearance
            btn.setEnabled(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            // Create a dimmed version of the color
            Color originalColor = color.get();
            Color dimmedColor = new Color(
                (originalColor.getRed() + 32) / 2,
                (originalColor.getGreen() + 32) / 2,
                (originalColor.getBlue() + 32) / 2,
                128  // Semi-transparent
            );
            btn.setBackground(dimmedColor);

            // Add diagonal stripes pattern to indicate disabled state
            btn.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
            btn.setToolTipText("Already selected by other player");
        } else if (isSelected) {
            // Selected state: bright border with highlight
            btn.setEnabled(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBackground(color.get());
            btn.setBorder(BorderFactory.createLineBorder(ColorsInUse.BOARD_ACTIVE_BORDER.get(), 4));
            btn.setToolTipText("Selected");
        } else {
            // Available state: normal appearance
            btn.setEnabled(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBackground(color.get());
            btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            btn.setToolTipText("Click to select");
        }
    }

    private void refreshColorButtons() {
        // Update all player 1 color buttons
        if (player1ColorButtons != null) {
            for (JButton btn : player1ColorButtons) {
                ColorsInUse color = (ColorsInUse) btn.getClientProperty("color");
                if (color != null) {
                    boolean isSelected = (color == player1Color);
                    boolean isTaken = (color == player2Color);
                    updateColorButtonAppearance(btn, color, isSelected, isTaken);
                }
            }
        }

        // Update all player 2 color buttons
        if (player2ColorButtons != null) {
            for (JButton btn : player2ColorButtons) {
                ColorsInUse color = (ColorsInUse) btn.getClientProperty("color");
                if (color != null) {
                    boolean isSelected = (color == player2Color);
                    boolean isTaken = (color == player1Color);
                    updateColorButtonAppearance(btn, color, isSelected, isTaken);
                }
            }
        }
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 30));
        field.setMinimumSize(new Dimension(100, 30));
        field.setMaximumSize(new Dimension(100, 30));
        field.setBackground(ColorsInUse.BG_COLOR.get());
        // Set placeholder state by default for name fields
        field.putClientProperty("placeholder", NAME_PLACEHOLDER);
        field.setText(NAME_PLACEHOLDER);
        field.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());

        field.setCaretColor(Color.WHITE);
        field.setFont(FontsInUse.PIXEL.getSize(26f));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        field.setHorizontalAlignment(SwingConstants.CENTER);

        // Attach placeholder listener so focus changes toggle placeholder text/color
        field.addFocusListener(placeholderListener);
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
        btn.setFont(FontsInUse.PIXEL.getSize(28f));

        return btn;
    }

    private JButton createKoalaButton(String resourcePath, String text, String tooltip, GameDifficulty difficulty) {
        int SQUARE_SIZE = 130;

        // Load wood background
        ImageIcon woodBg = loadScaledIcon("btn-square", SQUARE_SIZE, SQUARE_SIZE);

        // Load koala icon
        ImageIcon koalaIcon = null;
        try {
            URL koalaUrl = getClass().getResource(resourcePath);
            if (koalaUrl != null) {
                ImageIcon icon = new ImageIcon(koalaUrl);
                Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                koalaIcon = new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.err.println("Error loading koala icon: " + e.getMessage());
        }

        // Create button using IconOnImageButton factory method
        IconOnImageButton btn = IconOnImageButton.createKoalaButton(
            woodBg,
            koalaIcon,
            text,
            tooltip,
            new Dimension(SQUARE_SIZE, SQUARE_SIZE),
            () -> {
                this.selectedDifficulty = difficulty;
                updateSelection();
            }
        );

        return btn;
    }

    //visual indication for difficulty selected
    private void updateSelection() {
        Color selectedColor = ColorsInUse.BOARD_ACTIVE_BORDER.get();
        resetSelection();
        soundManager.playOnce(SoundManager.SoundId.SELECTION);
        switch (selectedDifficulty) {
            case EASY:
                btnEasy.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnEasy, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                setGameInfo(GameDifficulty.EASY);
                gameInfoTitle.setOutlineColor(ColorsInUse.DIFFICULTY_EASY_OUTLINE);
                gameInfoStats.setOutlineColor(ColorsInUse.DIFFICULTY_EASY_OUTLINE);
                break;

            case MEDIUM:
                btnMedium.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnMedium, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                setGameInfo(GameDifficulty.MEDIUM);
                gameInfoTitle.setOutlineColor(ColorsInUse.DIFFICULTY_MEDIUM_OUTLINE);
                gameInfoStats.setOutlineColor(ColorsInUse.DIFFICULTY_MEDIUM_OUTLINE);
                break;
            case HARD:
                btnHard.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnHard, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                setGameInfo(GameDifficulty.HARD);
                gameInfoTitle.setOutlineColor(ColorsInUse.DIFFICULTY_HARD_OUTLINE);
                gameInfoStats.setOutlineColor(ColorsInUse.DIFFICULTY_HARD_OUTLINE);
                break;
        }
    }

    private void setGameInfo(GameDifficulty diff) {
        gameInfoTitle.setText(diff.name() + " DIFFICULTY");
        gameInfoStats.setText(
                "Grid: " + diff.getRows() + "x" + diff.getCols() +
                        " | Mines: " + diff.getMineCount() +
                        " | Activation Cost: " + diff.getActivationCost() + " pts"
        );
    }


    private JButton createTransparentIconButton(String resourcePath, int width, int height) {
        JButton btn = new JButton();

        // Core styling for transparency
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(width + 10, height + 10));

        try {
            URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon normalIcon = new ImageIcon(url);
                Image img = normalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);


                ImageIcon standard = new ImageIcon(img);
                btn.setIcon(standard);

                //create light version of the icon
                ImageIcon hover = createLighterIcon(img);

                //switch to lighter icon when hovered
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent evt) {
                        btn.setIcon(hover);
                    }

                    @Override
                    public void mouseExited(MouseEvent evt) {
                        btn.setIcon(standard);
                    }
                });
            }
        } catch (Exception e) {
            btn.setText("X");
            btn.setForeground(Color.WHITE);
        }

        return btn;
    }

    public KeyListener answerKeyListener(JTextField tf, JLabel name, JLabel bottomLabel) {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                // allow Ctrl+A / Ctrl+C / Ctrl+V / Ctrl+X etc.
                if (e.isControlDown() || e.isMetaDown() || e.isAltDown()) {
                    return;
                }

                if (tf.getText().length() <= PlAYER_TEXT_LENGTH * 0.7) {
                    name.setForeground(ColorsInUse.TEXT.get());
                    name.setToolTipText(null);
                    showBottomTextLimit(bottomLabel, false);
                } else if (tf.getText().length() >= PlAYER_TEXT_LENGTH) {
                    e.consume();
                    tf.setText(tf.getText().substring(0, PlAYER_TEXT_LENGTH));

                    long now = System.currentTimeMillis();
                    Long last = (Long) name.getClientProperty("shake.last");
                    if (last == null || now - last > 200) { // 200ms cooldown
                        name.putClientProperty("shake.last", now);
                        animator.shake(name);
                        soundManager.playOnce(SoundManager.SoundId.BLOCK);
                    }

                    name.setToolTipText("Maximum length reached");
                    showBottomTextLimit(bottomLabel, true);
                } else if (tf.getText().length() >= PlAYER_TEXT_LENGTH * 0.7) {
                    name.setForeground(ColorsInUse.DENY.get());
                    name.setToolTipText("Approaching maximum length " + tf.getText().length() + "/" + PlAYER_TEXT_LENGTH);
                    showBottomTextLimit(bottomLabel, true);
                }
            }
        };
    }

    private void showBottomTextLimit(JLabel bottomLabel, boolean toShow) {
        if (toShow) {
            bottomLabel.setText("Max length: " + PlAYER_TEXT_LENGTH);
            bottomLabel.setForeground(ColorsInUse.TEXT.get());
        } else {
            bottomLabel.setText(" ");
            bottomLabel.setForeground(new Color(0, 0, 0, 0));
        }
    }

    //method to make an icon lighter for hover effect
    private ImageIcon createLighterIcon(Image sourceImg) {
        int w = sourceImg.getWidth(null);
        int h = sourceImg.getHeight(null);
        BufferedImage buffered = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = buffered.createGraphics();
        g2.drawImage(sourceImg, 0, 0, null);
        g2.dispose();

        RescaleOp op = new RescaleOp(1.4f, 0, null);
        buffered = op.filter(buffered, null);

        return new ImageIcon(buffered);
    }

    private ImageIcon loadScaledIcon(String resourceBase, int width, int height) {
        String[] exts = {".png", ".jpg", ".jpeg", ".gif"};
        for (String ext : exts) {
            URL url = getClass().getResource("/" + resourceBase + ext);
            if (url != null) {
                try {
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

}
