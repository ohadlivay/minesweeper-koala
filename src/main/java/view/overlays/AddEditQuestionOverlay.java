package main.java.view.overlays;
import main.java.controller.NavigationController;
import main.java.controller.QuestionManagerController;
import main.java.model.Question;
import main.java.model.QuestionDifficulty;
import main.java.model.SysData;
import main.java.util.QuestionCSVManager;
import main.java.util.SoundManager;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import main.java.view.OutlinedLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.HashSet;

public class AddEditQuestionOverlay extends OverlayView {
    private final Question existingQuestion;
    private final boolean isEditing;
    private JTextArea questionArea;
    private QuestionDifficulty selectedDifficulty;
    private JButton btnEasy;
    private JButton btnMedium;
    private JButton btnHard;
    private JButton btnMaster;
    private JTextField answer1;
    private JTextField answer2;
    private JTextField answer3;
    private JTextField answer4;
    private JTextField [] wrongAnswers;
    private String QuestionPlaceholder;
    private String CorrectAnswerPlaceholder;
    private String WrongAnswerPlaceholder;
    private JLabel textLimitLabel;

    public AddEditQuestionOverlay(NavigationController navigationController, Question q) {
        super(navigationController, true);
        this.existingQuestion = q;
        this.isEditing = (q != null);
        if (isEditing){
            this.selectedDifficulty = q.getDifficulty();
        }

        // Configure ToolTip duration (stay longer)
        ToolTipManager.sharedInstance().setDismissDelay(15000); // 15 seconds
        ToolTipManager.sharedInstance().setInitialDelay(500);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                close();
            }
        });

        initUI();
    }

    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        contentPanel.setBackground(ColorsInUse.BG_COLOR.get());
        contentPanel.setPreferredSize(new Dimension(800, 600));

        String titleText = isEditing ? "EDITING QUESTION " + existingQuestion.getId() : "ADDING QUESTION " + ((int)SysData.getInstance().getMaxId() + 1);
        OutlinedLabel titleLabel = new OutlinedLabel(titleText, Color.BLACK, 5f);
        titleLabel.setFont(FontsInUse.PIXEL.getSize(40f));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        QuestionPlaceholder = "Write your question here...";
        CorrectAnswerPlaceholder = "Correct Answer...";
        WrongAnswerPlaceholder = "Wrong Answer...";

        formPanel.add(createLabel("Question Text:"), gbc);
        gbc.gridy++;
        questionArea = new JTextArea(6, 20);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(FontsInUse.PIXEL.getSize(22f));
        questionArea.setBackground(ColorsInUse.BTN_COLOR.get());
        questionArea.setForeground(ColorsInUse.TEXT.get());
        questionArea.setCaretColor(ColorsInUse.TEXT.get());
        questionArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ColorsInUse.TEXT_BOX_BORDER.get(), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        questionArea.setText(QuestionPlaceholder);
        questionArea.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
        questionArea.addFocusListener(placeholderListener);

        textLimitLabel = new JLabel("0/" + Question.getMaxQuestionLength());
        textLimitLabel.setFont(FontsInUse.PIXEL.getSize(14f));
        textLimitLabel.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());

        questionArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (questionArea.getText().length() >= Question.getMaxQuestionLength()) {
                    e.consume();
                    questionArea.setText(questionArea.getText().substring(0, Question.getMaxQuestionLength()));
                    animator.flashForeground(textLimitLabel, ColorsInUse.DENY.get(), ColorsInUse.TEXT.get());
                }
                textLimitLabel.setText((questionArea.getText().length()) + "/" + Question.getMaxQuestionLength());
            }
        });

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(textLimitLabel, gbc);
        gbc.gridy++;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.5;
        formPanel.add(questionArea, gbc);
        gbc.gridy++;

        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        difficultyPanel.setOpaque(false);
        difficultyPanel.add(createLabel("Select Difficulty:"));

        String easyTip = "<html><b>Easy</b><br/>Correct: +3 to +10 pts & +1 Life<br/>Incorrect: -3 to -10 pts or nothing</html>";
        String medTip = "<html><b>Medium</b><br/>Correct: Reveal Mine or 3x3 Area, +6 to +15 pts<br/>Incorrect: -6 to -15 pts, possible Life loss</html>";
        String hardTip = "<html><b>Hard</b><br/>Correct: +15 to +20 pts & +1-2 Lives<br/>Incorrect: -15 to -20 pts & -1 Life loss</html>";
        String expertTip = "<html><b>Expert</b><br/>Correct: +15 to +40 pts & +2-3 Lives<br/>Incorrect: -15 to -40 pts & up to -3 Lives loss</html>";

        btnEasy = createKoalaButton("/green-koala-pixel.png", easyTip, QuestionDifficulty.EASY);
        btnMedium = createKoalaButton("/yellow-koala-pixel.png", medTip, QuestionDifficulty.MEDIUM);
        btnHard = createKoalaButton("/red-koala-pixel.png", hardTip, QuestionDifficulty.HARD);
        btnMaster = createKoalaButton("/master-koala-pixel.png", expertTip, QuestionDifficulty.MASTER);

        difficultyPanel.add(createLabeledDifficultyPanel(btnEasy, "Easy"));
        difficultyPanel.add(createLabeledDifficultyPanel(btnMedium, "Medium"));
        difficultyPanel.add(createLabeledDifficultyPanel(btnHard, "Hard"));
        difficultyPanel.add(createLabeledDifficultyPanel(btnMaster, "Expert"));

        gbc.insets = new Insets(10, 0, 10, 0);
        formPanel.add(difficultyPanel, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(4, 0, 4, 0);
        formPanel.add(createLabel("Answers:"), gbc);
        gbc.gridy++;

        answer1 = createStyledTextField();
        answer2 = createStyledTextField();
        answer3 = createStyledTextField();
        answer4 = createStyledTextField();
        wrongAnswers = new JTextField[] {answer2, answer3, answer4};

        formPanel.add(createAnswerFieldWrapper(answer1, ColorsInUse.CONFIRM.get(), CorrectAnswerPlaceholder, gbc), gbc);
        gbc.gridy++;

        for (JTextField tf : wrongAnswers) {
            formPanel.add(createAnswerFieldWrapper(tf, ColorsInUse.DENY.get(), WrongAnswerPlaceholder, gbc), gbc);
            gbc.gridy++;
        }

        contentPanel.add(formPanel, BorderLayout.CENTER);

        //save and cancel buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = createIconButton("/x-pixel.png", "Cancel");
        cancelBtn.addActionListener(e -> close());

        JButton saveBtn = createIconButton("/v-pixel.png", "Save");
        saveBtn.addActionListener(e -> onSave());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        getContentPane().add(contentPanel);

        updateSelection();
        if (isEditing) {
            populateFields();
        }
    }

    private JPanel createLabeledDifficultyPanel(JButton btn, String labelText) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(btn);

        wrapper.add(Box.createVerticalStrut(5));

        JLabel lbl = new JLabel(labelText);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setFont(FontsInUse.PIXEL.getSize(20f));
        lbl.setForeground(ColorsInUse.TEXT.get());
        wrapper.add(lbl);

        return wrapper;
    }

    private JButton createKoalaButton(String resourcePath, String tooltip, QuestionDifficulty difficulty) {
        JButton btn = new JButton();
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(80, 80));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);
        btn.setBackground(ColorsInUse.BG_COLOR.get());

        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            btn.setText(difficulty.name());
        }
        btn.addActionListener(e -> {
            this.selectedDifficulty = difficulty;
            updateSelection();
        });
        return btn;
    }

    private JButton createIconButton(String resourcePath, String tooltip) {
        JButton btn = new JButton();
        btn.setToolTipText(tooltip);
        btn.setBackground(ColorsInUse.BTN_COLOR.get());
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setFocusPainted(false);

        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            } else {
                btn.setText(tooltip);
            }
        } catch (Exception e) {
            btn.setText(tooltip);
        }

        //hover animation for buttons
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(ColorsInUse.BTN_COLOR.get().brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(ColorsInUse.BTN_COLOR.get());
            }
        });

        return btn;
    }

    private void updateSelection() {
        Color selectedColor = ColorsInUse.TEXT.get();
        Color unselectedColor = new Color(0,0,0,0);
        btnEasy.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.EASY ? selectedColor : unselectedColor, 3));
        btnMedium.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.MEDIUM ? selectedColor : unselectedColor, 3));
        btnHard.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.HARD ? selectedColor : unselectedColor, 3));
        btnMaster.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.MASTER ? selectedColor : unselectedColor, 3));

        if (selectedDifficulty != null) {
            soundManager.playOnce(SoundManager.SoundId.SELECTION);
            JButton target = switch (selectedDifficulty) {
                case EASY -> btnEasy;
                case MEDIUM -> btnMedium;
                case HARD -> btnHard;
                case MASTER -> btnMaster;
            };
            animator.flashBackground(target, ColorsInUse.FEEDBACK_GOOD_COLOR.get(), ColorsInUse.BG_COLOR.get());
        }
    }

    private void populateFields() {
        questionArea.setText(existingQuestion.getQuestionText());
        questionArea.setForeground(ColorsInUse.TEXT.get());
        textLimitLabel.setText((questionArea.getText().length()) + "/" + Question.getMaxQuestionLength());
        answer1.setText(existingQuestion.getAnswer1());
        answer1.setForeground(ColorsInUse.TEXT.get());
        answer2.setText(existingQuestion.getAnswer2());
        answer2.setForeground(ColorsInUse.TEXT.get());
        answer3.setText(existingQuestion.getAnswer3());
        answer3.setForeground(ColorsInUse.TEXT.get());
        answer4.setText(existingQuestion.getAnswer4());
        answer4.setForeground(ColorsInUse.TEXT.get());
        updateSelection();
    }

    private void onSave() {
        String qText = questionArea.getText().trim();
        String a1 = answer1.getText().trim();
        String a2 = answer2.getText().trim();
        String a3 = answer3.getText().trim();
        String a4 = answer4.getText().trim();

        if (qText.isEmpty() || qText.equals(QuestionPlaceholder) || a1.isEmpty() || a1.equals(CorrectAnswerPlaceholder)) {
            JOptionPane.showMessageDialog(this, "All text fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedDifficulty == null) {
            JOptionPane.showMessageDialog(this, "Please select a difficulty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Correct duplicate detection: start with an empty set and add normalized answers.
        java.util.Set<String> checkSet = new HashSet<>();
        String[] answers = {a1, a2, a3, a4};
        for (String a : answers) {
            String norm = a.trim().toLowerCase();
            if (!checkSet.add(norm)) {
                JOptionPane.showMessageDialog(this, "Duplicate answer found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int currentId = isEditing ? existingQuestion.getId() : -1;
        for (Question q : SysData.getInstance().getQuestions()) {
            if (q.getId() != currentId && q.getQuestionText().equalsIgnoreCase(qText)) {
                JOptionPane.showMessageDialog(this,
                        "This question already exists in the system!",
                        "Duplicate Question", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        QuestionManagerController qmc = QuestionManagerController.getInstance();
        if (isEditing) {
            qmc.userSavedEditedQuestion(existingQuestion.getId(), qText, selectedDifficulty, a1, a2, a3, a4);
        } else {
            Question newQ = qmc.userAddedQuestion();
            try {
                newQ.setQuestionText(qText);
                newQ.setDifficulty(selectedDifficulty);
                newQ.setAnswer1(a1);
                newQ.setAnswer2(a2);
                newQ.setAnswer3(a3);
                newQ.setAnswer4(a4);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SysData.getInstance().addQuestion(newQ);
            try { QuestionCSVManager.rewriteQuestionsToCSVFromSysData(); } catch (Exception ignored) {}
            qmc.refreshAndJumpToLastPage();
        }
        qmc.refreshQuestionList();
        close();
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsInUse.PIXEL.getSize(24f));
        lbl.setForeground(ColorsInUse.TEXT.get());
        return lbl;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FontsInUse.PIXEL.getSize(22f));
        tf.setBackground(ColorsInUse.BTN_COLOR.get());
        tf.setForeground(ColorsInUse.TEXT.get());
        tf.setCaretColor(ColorsInUse.TEXT.get());
        tf.setBorder(new EmptyBorder(5,5,5,5));
        tf.setPreferredSize(new Dimension(200, 45));
        return tf;
    }

    FocusAdapter placeholderListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getComponent() instanceof JTextComponent tc &&
                    (tc.getText().equals(QuestionPlaceholder) || tc.getText().equals(CorrectAnswerPlaceholder) || tc.getText().equals(WrongAnswerPlaceholder))) {
                tc.setText("");
                tc.setForeground(ColorsInUse.TEXT.get());
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (e.getComponent() instanceof JTextComponent tc && tc.getText().trim().isEmpty()) {
                if (tc == questionArea) tc.setText(QuestionPlaceholder);
                else if (tc == answer1) tc.setText(CorrectAnswerPlaceholder);
                else tc.setText(WrongAnswerPlaceholder);
                tc.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
            }
        }
    };

    private JPanel createAnswerFieldWrapper(JTextField field, Color bgColor, String placeholder, GridBagConstraints gbc) {
        field.setBackground(bgColor);
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ColorsInUse.TEXT_BOX_BORDER.get(), 1), new EmptyBorder(5, 5, 5, 5)));
        field.setText(placeholder);
        field.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
        field.addFocusListener(placeholderListener);

        JLabel errorLabel = new JLabel();
        ImageIcon icon = getScaledErrorIcon();
        if (icon != null) errorLabel.setIcon(icon);
        errorLabel.setVisible(false);
        errorLabel.setToolTipText("Max " + Question.getMaxAnswerLength() + " chars.");
        field.addKeyListener(answerKeyListener(field, errorLabel));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(field, BorderLayout.CENTER);
        wrapper.add(errorLabel, BorderLayout.EAST);
        return wrapper;
    }

    private ImageIcon getScaledErrorIcon() {
        java.net.URL errUrl = getClass().getResource("/error.png");
        if (errUrl == null) return null;
        Image img = new ImageIcon(errUrl).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public KeyListener answerKeyListener(JTextField tf, JLabel errorLabel) {
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
                errorLabel.setVisible(tf.getText().length() >= Question.getMaxAnswerLength() * 0.7);
                if (tf.getText().length() >= Question.getMaxAnswerLength()) {
                    e.consume();
                    tf.setText(tf.getText().substring(0, Question.getMaxAnswerLength()));
                    soundManager.playOnce(SoundManager.SoundId.BLOCK);
                }
            }
        };
    }
}

