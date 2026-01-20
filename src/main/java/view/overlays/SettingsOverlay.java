package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;
import main.java.util.SoundManager;
import main.java.view.BackgroundPanel;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import main.java.view.OutlinedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class SettingsOverlay extends OverlayView {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonBack;
    private JButton btnEasy;
    private JButton btnMedium;
    private JButton btnHard;
    private JTextField player1Name;
    private JTextField player2Name;
    private JLabel gameInfo;
    private GameDifficulty selectedDifficulty;
    private JComboBox<String> player1ColorCombo;
    private JComboBox<String> player2ColorCombo;
    private ColorsInUse player1SelectedColor;
    private ColorsInUse player2SelectedColor;

    private final int PlAYER_TEXT_LENGTH = 15;

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
                btnEasy.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), new EmptyBorder(5, 5, 5, 5)));
                System.out.println("Selected Difficulty: EASY");
            }
        });

        btnMedium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.MEDIUM;
                btnMedium.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), new EmptyBorder(5, 5, 5, 5)));
                System.out.println("Selected Difficulty: MEDIUM");
            }
        });

        btnHard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetSelection();
                selectedDifficulty = GameDifficulty.HARD;
                btnHard.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), new EmptyBorder(5, 5, 5, 5)));
                System.out.println("Selected Difficulty: HARD");
            }
        });
    }

    public SettingsOverlay(NavigationController nav, String player1, String player2, GameDifficulty difficulty) {
        this(nav, player1, player2, difficulty, null, null);
    }

    public SettingsOverlay(NavigationController nav, String player1, String player2, GameDifficulty difficulty, ColorsInUse player1Color, ColorsInUse player2Color) {
        this(nav);
        player1Name.setText(player1);
        player2Name.setText(player2);
        if (difficulty != null) {
            this.selectedDifficulty = difficulty;
            updateSelection();
        } else {
            resetSelection();
        }
        
        // Set color selections if provided
        if (player1Color != null && player2Color != null) {
            ColorsInUse[] warmColors = ColorsInUse.getWarmBoardColors();
            for (int i = 0; i < warmColors.length; i++) {
                if (warmColors[i] == player1Color) {
                    player1ColorCombo.setSelectedIndex(i);
                    player1SelectedColor = player1Color;
                }
                if (warmColors[i] == player2Color) {
                    player2ColorCombo.setSelectedIndex(i);
                    player2SelectedColor = player2Color;
                }
            }
        }
    }

    private void initUI() {
        contentPane = new BackgroundPanel("/overlay-bg.png");
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(700, 530));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ColorsInUse.BG_COLOR.get());
        OutlinedLabel title = new OutlinedLabel("CHOOSE DIFFICULTY:", Color.BLACK, 6f);
        title.setFont(FontsInUse.PIXEL.getSize(52f));
        title.setForeground(ColorsInUse.TEXT.get());
        titlePanel.add(title);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        difficultyPanel.setBackground(ColorsInUse.BG_COLOR.get());

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

        // --- game info (centered to screen) ---
        gameInfo = new JLabel("");
        gameInfo.setOpaque(false);
        gameInfo.setForeground(ColorsInUse.TEXT.get());
        gameInfo.setFont(FontsInUse.PIXEL.getSize(24f));
        gameInfo.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy++;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        gameInfo.setPreferredSize(new Dimension(560, 44));
        gameInfo.setMinimumSize(new Dimension(560, 44));
        centerPanel.add(gameInfo, gbc);

        // --- names panel (YOU FORGOT TO ADD IT) ---
        JPanel namesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        namesPanel.setBackground(ColorsInUse.BG_COLOR.get());
        namesPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Initialize color combos with warm board colors
        ColorsInUse[] warmColors = ColorsInUse.getWarmBoardColors();
        String[] colorNames = new String[warmColors.length];
        for (int i = 0; i < warmColors.length; i++) {
            colorNames[i] = warmColors[i].name().replace("_", " ");
        }

        player1ColorCombo = new JComboBox<>(colorNames);
        player2ColorCombo = new JComboBox<>(colorNames);

        // Set default selections (different colors)
        player1ColorCombo.setSelectedIndex(0);
        player2ColorCombo.setSelectedIndex(1);
        player1SelectedColor = warmColors[0];
        player2SelectedColor = warmColors[1];

        // Style the combo boxes
        styleColorComboBox(player1ColorCombo);
        styleColorComboBox(player2ColorCombo);

        // Add listeners to prevent same color selection
        player1ColorCombo.addActionListener(e -> {
            int selectedIndex = player1ColorCombo.getSelectedIndex();
            player1SelectedColor = warmColors[selectedIndex];
            if (player1SelectedColor == player2SelectedColor) {
                // Find a different color for player 2
                int newIndex = (selectedIndex + 1) % warmColors.length;
                player2ColorCombo.setSelectedIndex(newIndex);
                player2SelectedColor = warmColors[newIndex];
            }
        });

        player2ColorCombo.addActionListener(e -> {
            int selectedIndex = player2ColorCombo.getSelectedIndex();
            player2SelectedColor = warmColors[selectedIndex];
            if (player2SelectedColor == player1SelectedColor) {
                // Find a different color for player 1
                int newIndex = (selectedIndex + 1) % warmColors.length;
                player1ColorCombo.setSelectedIndex(newIndex);
                player1SelectedColor = warmColors[newIndex];
            }
        });

        namesPanel.add(createInputGroup("Player 1", player1Name = createTextField(), player1ColorCombo));
        namesPanel.add(createInputGroup("Player 2", player2Name = createTextField(), player2ColorCombo));

        gbc.gridy++;
        gbc.insets = new Insets(5, 50, 5, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        centerPanel.add(namesPanel, gbc);

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
        String player1 = getPlayer1Name();
        String player2 = getPlayer2Name();

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
            GameSessionController.getInstance().setupGame(player1, player2, selectedDifficulty, player1SelectedColor, player2SelectedColor);
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
        return createInputGroup(labelText, textField, null);
    }

    private JPanel createInputGroup(String labelText, JTextField textField, JComboBox<String> colorCombo) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(ColorsInUse.BG_COLOR.get());

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setForeground(ColorsInUse.TEXT.get());
        label.setFont(FontsInUse.PIXEL.getSize(28f));

        textField.setPreferredSize(new Dimension(150, 30));
        textField.setMinimumSize(new Dimension(150, 30));
        textField.setMaximumSize(new Dimension(150, 30));

        JLabel bottomLabel = new JLabel("Max length: " + PlAYER_TEXT_LENGTH);
        bottomLabel.setFont(FontsInUse.PIXEL.getSize(20f));
        bottomLabel.setText(" ");
        bottomLabel.setForeground(new Color(0, 0, 0, 0));

        panel.add(label, BorderLayout.NORTH);

        if (colorCombo != null) {
            // Create a panel to hold both text field and color selector
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setBackground(ColorsInUse.BG_COLOR.get());

            textField.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputPanel.add(textField);
            inputPanel.add(Box.createVerticalStrut(5));

            JLabel colorLabel = new JLabel("Board Color:", SwingConstants.CENTER);
            colorLabel.setForeground(ColorsInUse.TEXT.get());
            colorLabel.setFont(FontsInUse.PIXEL.getSize(18f));
            colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputPanel.add(colorLabel);
            inputPanel.add(Box.createVerticalStrut(3));

            colorCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputPanel.add(colorCombo);

            panel.add(inputPanel, BorderLayout.CENTER);
        } else {
            panel.add(textField, BorderLayout.CENTER);
        }

        panel.add(bottomLabel, BorderLayout.SOUTH);

        textField.addKeyListener(answerKeyListener(textField, label, bottomLabel));
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 30));
        field.setMinimumSize(new Dimension(100, 30));
        field.setMaximumSize(new Dimension(100, 30));
        field.setBackground(ColorsInUse.BG_COLOR.get());
        field.setForeground(ColorsInUse.TEXT.get());
        field.setCaretColor(Color.WHITE);
        field.setFont(FontsInUse.PIXEL.getSize(28f));
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
        btn.setFont(FontsInUse.PIXEL.getSize(28f));

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
        resetSelection();
        soundManager.playOnce(SoundManager.SoundId.SELECTION);
        switch (selectedDifficulty) {
            case EASY:
                btnEasy.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnEasy, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                gameInfo.setText("<html><center>" +
                        "<b><font color='#0cc42b'>EASY DIFFICULTY</font><br/>" +
                        "<font color='#0cc42b'>Grid:</font> " + GameDifficulty.EASY.getRows() + "x" + GameDifficulty.EASY.getCols() +
                        " | <font color='#0cc42b'>Mines:</font> " + GameDifficulty.EASY.getMineCount() +
                        " | <font color='#0cc42b'>Activation Cost:</font> " + GameDifficulty.EASY.getActivationCost() + " pts" +
                        "</center></html>");
                break;
            case MEDIUM:
                btnMedium.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnMedium, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                gameInfo.setText("<html><center>" +
                        "<b><font color='#c78800'>MEDIUM DIFFICULTY</font><br/>" +
                        "<font color='#c78800'>Grid:</font> " + GameDifficulty.MEDIUM.getRows() + "x" + GameDifficulty.MEDIUM.getCols() +
                        " | <font color='#c78800'>Mines:</font> " + GameDifficulty.MEDIUM.getMineCount() +
                        " | <font color='#c78800'>Activation Cost:</font> " + GameDifficulty.MEDIUM.getActivationCost() + " pts" +
                        "</center></html>");
                break;
            case HARD:
                btnHard.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnHard, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                gameInfo.setText("<html><center>" +
                        "<b><font color='#FF6B6B'>HARD DIFFICULTY</font><br/>" +
                        "<font color='#FF6B6B'>Grid:</font> " + GameDifficulty.HARD.getRows() + "x" + GameDifficulty.HARD.getCols() +
                        " | <font color='#FF6B6B'>Mines:</font> " + GameDifficulty.HARD.getMineCount() +
                        " | <font color='#FF6B6B'>Activation Cost:</font> " + GameDifficulty.HARD.getActivationCost() + " pts" +
                        "</center></html>");
                break;
        }
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
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon normalIcon = new ImageIcon(url);
                Image img = normalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);


                ImageIcon standard = new ImageIcon(img);
                btn.setIcon(standard);

                //create light version of the icon
                ImageIcon hover = createLighterIcon(img);

                //switch to lighter icon when hovered
                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        btn.setIcon(hover);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
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
        java.awt.image.BufferedImage buffered = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = buffered.createGraphics();
        g2.drawImage(sourceImg, 0, 0, null);
        g2.dispose();

        java.awt.image.RescaleOp op = new java.awt.image.RescaleOp(1.4f, 0, null);
        buffered = op.filter(buffered, null);

        return new ImageIcon(buffered);
    }

    private void styleColorComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 30));
        comboBox.setMaximumSize(new Dimension(150, 30));
        comboBox.setBackground(ColorsInUse.BG_COLOR.get());
        comboBox.setForeground(ColorsInUse.TEXT.get());
        comboBox.setFont(FontsInUse.PIXEL.getSize(16f));
        comboBox.setFocusable(false);
        
        // Custom renderer to show color preview
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(FontsInUse.PIXEL.getSize(16f));
                
                if (value != null) {
                    String colorName = value.toString();
                    ColorsInUse[] warmColors = ColorsInUse.getWarmBoardColors();
                    for (ColorsInUse color : warmColors) {
                        if (color.name().replace("_", " ").equals(colorName)) {
                            // Create a small colored square as icon
                            int size = 16;
                            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2 = img.createGraphics();
                            g2.setColor(color.get());
                            g2.fillRect(0, 0, size, size);
                            g2.setColor(Color.BLACK);
                            g2.drawRect(0, 0, size-1, size-1);
                            g2.dispose();
                            label.setIcon(new ImageIcon(img));
                            break;
                        }
                    }
                }
                
                if (isSelected) {
                    label.setBackground(ColorsInUse.BTN_COLOR.get());
                    label.setForeground(ColorsInUse.TEXT.get());
                } else {
                    label.setBackground(ColorsInUse.BG_COLOR.get());
                    label.setForeground(ColorsInUse.TEXT.get());
                }
                
                return label;
            }
        });
    }

}
