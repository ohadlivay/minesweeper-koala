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
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

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

        //we enable closing the overlay when the user clicks the X button
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
        contentPanel.setPreferredSize(new Dimension(800, 550));

        String titleText = isEditing ? "Editing Question " + existingQuestion.getId() : "Adding Question " + ((int)SysData.getInstance().getMaxId() + 1);
        JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
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

        //question textbox is here
        formPanel.add(createLabel("Question Text:"), gbc);
        gbc.gridy++;
        questionArea = new JTextArea(8, 20); // more rows
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(FontsInUse.PIXEL.getSize(22f));
        questionArea.setBackground(ColorsInUse.BTN_COLOR.get());
        questionArea.setForeground(ColorsInUse.TEXT.get());
        questionArea.setCaretColor(ColorsInUse.TEXT.get());
        questionArea.setPreferredSize(new Dimension(600, 400));
        questionArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ColorsInUse.TEXT_BOX_BORDER.get(), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        //placeholder text when empty
        questionArea.setText(QuestionPlaceholder);
        questionArea.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
        questionArea.addFocusListener(placeholderListener);

        // label to show text limit
        textLimitLabel = new JLabel("0/" + Question.getMaxQuestionLength());
        textLimitLabel.setFont(FontsInUse.PIXEL.getSize(14f));
        textLimitLabel.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());

        // listener to make sure the question's text is not more than 200 chars
        // to change length, go to Question/MaxQuestionLength
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
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(textLimitLabel, gbc);
        gbc.gridy++;


        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(questionArea, gbc);
        gbc.gridy++;

        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        //difficulty row
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        difficultyPanel.setOpaque(false);
        difficultyPanel.add(createLabel("Select Difficulty:"), BorderLayout.WEST);
        btnEasy = createKoalaButton("/green-koala-pixel.png", "Easy", QuestionDifficulty.EASY);
        btnMedium = createKoalaButton("/yellow-koala-pixel.png", "Medium", QuestionDifficulty.MEDIUM);
        btnHard = createKoalaButton("/red-koala-pixel.png", "Hard", QuestionDifficulty.HARD);
        btnMaster = createKoalaButton("/master-koala-pixel.png", "Master", QuestionDifficulty.MASTER);
        difficultyPanel.add(btnEasy);
        difficultyPanel.add(btnMedium);
        difficultyPanel.add(btnHard);
        difficultyPanel.add(btnMaster);

        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(difficultyPanel, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(4, 0, 4, 0);
        formPanel.add(createLabel("Answers:"), gbc);
        gbc.gridy++;

        //answers textboxes are here
        // Initialize the text fields
        answer1 = createStyledTextField();
        answer2 = createStyledTextField();
        answer3 = createStyledTextField();
        answer4 = createStyledTextField();
        wrongAnswers = new JTextField[] {answer2, answer3, answer4};

        // correct answer
        formPanel.add(createAnswerFieldWrapper(answer1, ColorsInUse.CONFIRM.get(), CorrectAnswerPlaceholder, gbc), gbc);
        gbc.gridy++;

        // wrong answers
        for (JTextField tf : wrongAnswers) {
            formPanel.add(createAnswerFieldWrapper(tf, ColorsInUse.DENY.get(), WrongAnswerPlaceholder, gbc), gbc);
            gbc.gridy++;
        }

        contentPanel.add(formPanel, BorderLayout.CENTER);

        //bottom save and delete buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        btnPanel.setOpaque(false);
        JButton saveBtn = createActionButton("SAVE", ColorsInUse.CONFIRM.get());
        saveBtn.addActionListener(e -> onSave());
        JButton cancelBtn = createActionButton("CANCEL", ColorsInUse.DENY.get());
        cancelBtn.addActionListener(e -> close());
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        getContentPane().add(contentPanel);

        //for initialization
        updateSelection();
        if (isEditing) {
            populateFields();
        }
    }

    private JButton createKoalaButton(String resourcePath, String tooltip, QuestionDifficulty difficulty) {
        JButton btn = new JButton();
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(80, 80));
        btn.setMinimumSize(new Dimension(80, 80));
        btn.setMaximumSize(new Dimension(80, 80));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);

        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
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
        btnEasy.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.EASY ? selectedColor : unselectedColor, 3));
        btnMedium.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.MEDIUM ? selectedColor : unselectedColor, 3));
        btnHard.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.HARD ? selectedColor : unselectedColor, 3));
        btnMaster.setBorder(new LineBorder(selectedDifficulty == QuestionDifficulty.MASTER ? selectedColor : unselectedColor, 3));
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

        if (qText.equals(QuestionPlaceholder)) qText = "";
        if (a1.equals(CorrectAnswerPlaceholder)) a1 = "";
        if (a2.equals(WrongAnswerPlaceholder)) a2 = "";
        if (a3.equals(WrongAnswerPlaceholder)) a3 = "";
        if (a4.equals(WrongAnswerPlaceholder)) a4 = "";

        if (qText.isEmpty() || a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All text fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedDifficulty == null) {
            JOptionPane.showMessageDialog(this, "Please select a difficulty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        QuestionManagerController qmc = QuestionManagerController.getInstance();

        if (isEditing) {
            qmc.userSavedEditedQuestion(existingQuestion.getId(), qText, selectedDifficulty, a1, a2, a3, a4);
        }
        else
        {
            Question newQ = qmc.userAddedQuestion();
            try {
                newQ.setQuestionText(qText);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Question text too long! Max "+Question.getMaxQuestionLength()+" characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newQ.setDifficulty(selectedDifficulty);
            try {
                newQ.setAnswer1(a1);
                newQ.setAnswer2(a2);
                newQ.setAnswer3(a3);
                newQ.setAnswer4(a4);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "One of the answers is too long! Max "+Question.getMaxAnswerLength()+" characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SysData.getInstance().addQuestion(newQ);
            try {
                QuestionCSVManager.rewriteQuestionsToCSVFromSysData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            qmc.refreshAndJumpToLastPage();
        }

        qmc.refreshQuestionList();
        close();
    }

    // --- helper methods ---

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsInUse.PIXEL.getSize(24f));
        lbl.setForeground(ColorsInUse.TEXT.get());
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FontsInUse.PIXEL.getSize(22f));
        tf.setBackground(ColorsInUse.BTN_COLOR.get());
        tf.setForeground(ColorsInUse.TEXT.get());
        tf.setCaretColor(ColorsInUse.TEXT.get());
        tf.setBorder(new LineBorder(ColorsInUse.TEXT.get(), 1));
        tf.setBorder(new EmptyBorder(5,5,5,5));
        tf.setPreferredSize(new Dimension(200, 45));
        return tf;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FontsInUse.PIXEL.getSize(24f));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 50));
        return btn;
    }

    FocusAdapter placeholderListener = new FocusAdapter() {

        @Override
        public void focusGained(FocusEvent e) {
            Object src = e.getComponent();

            if (src == questionArea && questionArea.getText().equals(QuestionPlaceholder)) {
                questionArea.setText("");
                questionArea.setForeground(ColorsInUse.TEXT.get());
            } else if (src == answer1 && answer1.getText().equals(CorrectAnswerPlaceholder)) {
                answer1.setText("");
                answer1.setForeground(ColorsInUse.TEXT.get());
            } else {
                for (JTextField tf : wrongAnswers) {
                    if (src == tf && tf.getText().equals(WrongAnswerPlaceholder)) {
                        tf.setText("");
                        tf.setForeground(ColorsInUse.TEXT.get());
                        break;
                    }
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            Object src = e.getComponent();

            if (src == questionArea && questionArea.getText().trim().isEmpty()) {
                questionArea.setText(QuestionPlaceholder);
                questionArea.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
            } else if (src == answer1 && answer1.getText().trim().isEmpty()) {
                answer1.setText(CorrectAnswerPlaceholder);
                answer1.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
            } else {
                for (JTextField tf : wrongAnswers) {
                    if (src == tf && tf.getText().trim().isEmpty()) {
                        tf.setText(WrongAnswerPlaceholder);
                        tf.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
                        break;
                    }
                }
            }
        }
    };

    private JPanel createAnswerFieldWrapper(JTextField field, Color bgColor, String placeholder, GridBagConstraints gbc) {
        // Style the field
        field.setBackground(bgColor);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ColorsInUse.TEXT_BOX_BORDER.get(), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        field.setText(placeholder);
        field.setForeground(ColorsInUse.PLACEHOLDER_TEXT.get());
        field.addFocusListener(placeholderListener);

        // Create error label
        JLabel errorLabel = new JLabel();
        ImageIcon icon = getScaledErrorIcon(); // Helper for the icon
        if (icon != null) errorLabel.setIcon(icon);

        errorLabel.setVisible(false);
        errorLabel.setToolTipText("Answer must be less than " + Question.getMaxAnswerLength() + " characters.");
        errorLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

        // Enforce logic and update icon visibility
        field.addKeyListener(answerKeyListener(field, errorLabel));

        // Wrap and return
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(field, BorderLayout.CENTER);
        wrapper.add(errorLabel, BorderLayout.EAST);

        return wrapper;
    }

    // Utility to load the icon once
    private ImageIcon getScaledErrorIcon() {
        java.net.URL errUrl = getClass().getResource("/error.png");
        if (errUrl == null) return null;
        ImageIcon icon = new ImageIcon(errUrl);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public KeyListener answerKeyListener(JTextField tf, JLabel errorLabel) {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf.getText().length() >= (int)(Question.getMaxAnswerLength() * 0.7)) {
                    errorLabel.setVisible(true);
                } else {
                    errorLabel.setVisible(false);
                }
                if (tf.getText().length() >= Question.getMaxAnswerLength()) {
                    e.consume();
                    tf.setText(tf.getText().substring(0, Question.getMaxAnswerLength()));
                    soundManager.playOnce(SoundManager.SoundId.BLOCK);
                }
            }
        };
    }
}

