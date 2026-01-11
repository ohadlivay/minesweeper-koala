package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameDifficulty;
import main.java.util.SoundManager;
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
    private GameDifficulty selectedDifficulty;

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

    private void initUI() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(ColorsInUse.BG_COLOR.get());
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
        gbc.insets = new Insets(15, 0, 0, 0);
        centerPanel.add(buttonStart, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());

        buttonBack = createTransparentIconButton("/back-pixel-2.png", 60, 50);
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
            //check to see if the system has enough questions to start a game
            GameSessionController.getInstance().setupGame(player1, player2, selectedDifficulty);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, "Not enough questions in the system to start a game. Please add more questions.", "Error", JOptionPane.ERROR_MESSAGE);
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
        panel.add(textField, BorderLayout.CENTER);
        panel.add(bottomLabel, BorderLayout.SOUTH);

        textField.addKeyListener(answerKeyListener(textField, label, bottomLabel));
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
                break;
            case MEDIUM:
                btnMedium.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnMedium, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
                break;
            case HARD:
                btnHard.setBorder(new LineBorder(selectedColor, 3));
                animator.flashForeground(btnHard, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.TEXT.get());
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

                // also allow backspace, delete, etc. (they aren't "real" characters)
                char ch = e.getKeyChar();
                if (Character.isISOControl(ch)) {
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

}
