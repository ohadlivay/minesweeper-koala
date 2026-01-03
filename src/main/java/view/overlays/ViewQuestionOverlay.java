package main.java.view.overlays;

import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.controller.QuestionController;
import main.java.model.*;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;
import main.java.view.OutlinedLabel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewQuestionOverlay extends OverlayView implements DisplayQuestionListener {
    private JPanel contentPane;
    private JLabel titleLabel;
    private JLabel difficultyLabel;
    private JTextArea questionText;
    private JButton buttonSubmit;
    private List<JButton> answerButtons;
    private JPanel answersPanel;
    private Board activeBoard;

    private int selected = -1; //represents the currently selected answer
    private int correct = -1; //track which button holds the correct answer

    private Question currentQuestion;

    public ViewQuestionOverlay(NavigationController navigationController) {
        super(navigationController, false); //LIRAN KEEP YOUR DIRTY HANDS OFF OF THIS LINE
        GameSessionController.getInstance().setBlocked(true); //blocks board interaction
        initUI();
        displayQuestion(activeBoard);
        buttonSubmit.addActionListener(e -> onSubmit());
    }

    @Override
    public void close() {
        GameSessionController.getInstance().setBlocked(false);
        super.close();
    }

    public void setBoard(Board board) {
        this.activeBoard = board;
    }

    private void initUI() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(ColorsInUse.BG_COLOR.get());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(800, 600));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ColorsInUse.BG_COLOR.get());

        OutlinedLabel titleLabel = new OutlinedLabel("Question", Color.BLACK, 6f);
        titleLabel.setFont(FontsInUse.PIXEL.getSize(62f));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultyLabel = new JLabel("Difficulty: Easy");
        difficultyLabel.setFont(FontsInUse.PIXEL.getSize(32f));
        difficultyLabel.setForeground(ColorsInUse.TEXT.get());
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(difficultyLabel);
        titlePanel.add(Box.createVerticalStrut(20));

        contentPane.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(ColorsInUse.BG_COLOR.get());
        centerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        questionText = new JTextArea("Loading Question...");
        questionText.setFont(FontsInUse.PIXEL.getSize(28f));
        questionText.setForeground(ColorsInUse.TEXT.get());
        questionText.setBackground(ColorsInUse.BG_COLOR.get());
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setEditable(false);
        questionText.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(questionText);
        centerPanel.add(Box.createVerticalStrut(20));

        answersPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        answersPanel.setBackground(ColorsInUse.BG_COLOR.get());
        answersPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        answersPanel.setMaximumSize(new Dimension(800, 300));

        centerPanel.add(answersPanel);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        buttonSubmit = createButton("/v-pixel.png");

        bottomPanel.add(buttonSubmit);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);
    }

    private void onSubmit() {
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //is the answer correct?
        boolean isCorrect = (selected == correct);
        QuestionController.getInstance().submitQuestionResult(isCorrect, currentQuestion.getDifficulty(), activeBoard);
        close();
    }

    // --- HELPER METHODS ---

    private void updateSelection(int index) {
        this.selected = index;
        for (int i = 0; i < answerButtons.size(); i++) {
            JButton btn = answerButtons.get(i);
            if (i == index) {
                btn.setBorder(new CompoundBorder(new LineBorder(ColorsInUse.CONFIRM.get(), 3), new EmptyBorder(10, 15, 10, 15)));
            } else {
                btn.setBorder(new CompoundBorder(new LineBorder(ColorsInUse.BG_COLOR.get(), 3), new EmptyBorder(10, 15, 10, 15)));
            }
        }
        contentPane.revalidate();
        contentPane.repaint();
    }

    private JButton createAnswerButton(String text, int index) {
        JButton btn = new JButton(text);
        btn.setFont(FontsInUse.PIXEL.getSize(24f));
        btn.setForeground(ColorsInUse.TEXT.get());
        btn.setBackground(ColorsInUse.BTN_COLOR.get());
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setBorder(new CompoundBorder(new LineBorder(ColorsInUse.BG_COLOR.get(), 2), new EmptyBorder(10, 15, 10, 15)));

        btn.addActionListener(e -> updateSelection(index));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (selected != index) btn.setBackground(ColorsInUse.BTN_COLOR.get().brighter());
            }

            public void mouseExited(MouseEvent e) {
                if (selected != index) btn.setBackground(ColorsInUse.BTN_COLOR.get());
            }
        });

        return btn;
    }

    private JButton createButton(String iconPath) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setMinimumSize(new Dimension(150, 45));
        btn.setMaximumSize(new Dimension(150, 45));
        btn.setBackground(ColorsInUse.BTN_COLOR.get());
        btn.setFocusPainted(false);

        java.net.URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } else {
            btn.setText("Submit");
            btn.setForeground(ColorsInUse.TEXT.get());
            btn.setFont(FontsInUse.PIXEL.getSize(32f));
        }

        return btn;
    }

    //retrieve the question from QuestionController
    @Override
    public void displayQuestion(Board board) {
        this.activeBoard = board;
        currentQuestion = QuestionController.getInstance().pollQuestion();
        if (currentQuestion == null) {
            questionText.setText("Error: No question available");
            return;
        }
        questionText.setText(currentQuestion.getQuestionText());
        QuestionDifficulty diff = currentQuestion.getDifficulty();
        difficultyLabel.setText("Difficulty: " + (diff != null ? diff.toString() : "UNKNOWN"));

        List<String> answerStrings = new ArrayList<>();
        String correctAnswerText = currentQuestion.getAnswer1();

        if (currentQuestion.getAnswer1() != null) answerStrings.add(currentQuestion.getAnswer1());
        if (currentQuestion.getAnswer2() != null) answerStrings.add(currentQuestion.getAnswer2());
        if (currentQuestion.getAnswer3() != null) answerStrings.add(currentQuestion.getAnswer3());
        if (currentQuestion.getAnswer4() != null) answerStrings.add(currentQuestion.getAnswer4());

        Collections.shuffle(answerStrings);

        answerButtons = new ArrayList<>();
        answersPanel.removeAll();

        for (int i = 0; i < answerStrings.size(); i++) {
            String ans = answerStrings.get(i);
            JButton btn = createAnswerButton(ans, i);

            //check if this button holds the correct answer text
            if (ans.equals(correctAnswerText)) {
                correct = i;
            }

            answerButtons.add(btn);
            answersPanel.add(btn);
        }
        contentPane.revalidate();
        contentPane.repaint();
    }
}