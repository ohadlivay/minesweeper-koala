package main.java.view.overlays;
import main.java.controller.NavigationController;
import main.java.controller.QuestionManagerController;
import main.java.model.Question;
import main.java.model.QuestionDifficulty;
import main.java.model.SysData;
import main.java.util.QuestionCSVManager;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

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

    public AddEditQuestionOverlay(NavigationController navigationController, Question q) {
        super(navigationController);
        this.existingQuestion = q;
        this.isEditing = (q != null);
        if (isEditing){
            this.selectedDifficulty = q.getDifficulty();
        }
        initUI();
    }

    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        contentPanel.setBackground(ColorsInUse.BG_COLOR.get());
        contentPanel.setPreferredSize(new Dimension(800, 550));

        String titleText = isEditing ? "Editing Question " + existingQuestion.getId() : "Adding Question " + SysData.getInstance().getMaxId() + 1;
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
        questionArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        questionArea.setBorder(new LineBorder(ColorsInUse.TEXT.get(), 1));
        questionArea.setPreferredSize(new Dimension(600, 400));

        JScrollPane scrollPane = new JScrollPane(questionArea);
        scrollPane.setPreferredSize(new Dimension(10, 160));
        scrollPane.setBorder(new LineBorder(ColorsInUse.TEXT.get(), 1));

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
        btnEasy = createKoalaButton("/green-koala.png", "Easy", QuestionDifficulty.EASY);
        btnMedium = createKoalaButton("/yellow-koala.png", "Medium", QuestionDifficulty.MEDIUM);
        btnHard = createKoalaButton("/red-koala.png", "Hard", QuestionDifficulty.HARD);
        btnMaster = createKoalaButton("/red-koala.png", "Master", QuestionDifficulty.MASTER);
        difficultyPanel.add(btnEasy);
        difficultyPanel.add(btnMedium);
        difficultyPanel.add(btnHard);
        difficultyPanel.add(btnMaster);

        gbc.insets = new Insets(15, 0, 15, 0);
        formPanel.add(difficultyPanel, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(createLabel("Answers:"), gbc);
        gbc.gridy++;
        //answers textboxes are here
        answer1 = createStyledTextField();
        answer1.setBackground(ColorsInUse.CONFIRM.get());
        formPanel.add(answer1,gbc);
        gbc.gridy++;

        answer2 = createStyledTextField();
        answer2.setBackground(ColorsInUse.DENY.get());
        formPanel.add(answer2,gbc);
        gbc.gridy++;

        answer3 = createStyledTextField();
        answer3.setBackground(ColorsInUse.DENY.get());
        formPanel.add(answer3,gbc);
        gbc.gridy++;

        answer4 = createStyledTextField();
        answer4.setBackground(ColorsInUse.DENY.get());
        formPanel.add(answer4,gbc);

        contentPanel.add(formPanel, BorderLayout.CENTER);

        //bottom save and delete buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        btnPanel.setOpaque(false);
        JButton saveBtn = createActionButton("SAVE", new Color(50, 150, 50));
        saveBtn.addActionListener(e -> onSave());
        JButton cancelBtn = createActionButton("CANCEL", new Color(150, 50, 50));
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
    }

    private void populateFields() {
        questionArea.setText(existingQuestion.getQuestionText());
        answer1.setText(existingQuestion.getAnswer1());
        answer2.setText(existingQuestion.getAnswer2());
        answer3.setText(existingQuestion.getAnswer3());
        answer4.setText(existingQuestion.getAnswer4());
        updateSelection();
    }

    private void onSave() {
        String qText = questionArea.getText().trim();
        String a1 = answer1.getText().trim();
        String a2 = answer2.getText().trim();
        String a3 = answer3.getText().trim();
        String a4 = answer4.getText().trim();

        if (qText.isEmpty() || a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        QuestionManagerController qmc = QuestionManagerController.getInstance();

        if (isEditing) {
            qmc.userSavedEditedQuestion(existingQuestion.getId(), qText, selectedDifficulty, a1, a2, a3, a4);
        }
        else
        {
            int newId = SysData.getInstance().getMaxId() + 1;
            Question newQ = new Question(newId, qText, selectedDifficulty, a1, a2, a3, a4);
            SysData.getInstance().addQuestion(newQ);
            try {
                QuestionCSVManager.rewriteQuestionsToCSVFromSysData();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

}

