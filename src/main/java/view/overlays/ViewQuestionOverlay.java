package main.java.view.overlays;

import main.java.controller.NavigationController;
import main.java.model.QuestionDifficulty;
import main.java.model.QuestionResult;
import main.java.view.ColorsInUse;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ViewQuestionOverlay extends OverlayView {
    private JPanel contentPane;
    private JLabel titleLabel;
    private JLabel difficultyLabel;
    private JTextArea questionText;
    private JButton buttonSubmit;
    private List<JButton> answerButtons;

    private int selected = -1; //represents the currently selected answer
    private int correct = 0; //for now it's hardcoded to answer A

    public ViewQuestionOverlay(NavigationController navigationController)
    {
        super(navigationController);
        initUI();
        buttonSubmit.addActionListener(e->onSubmit());

    }

    private void initUI() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(ColorsInUse.BG_COLOR.get());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setPreferredSize(new Dimension(800, 600));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ColorsInUse.BG_COLOR.get());

        titleLabel = new JLabel("Question");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 32));
        titleLabel.setForeground(ColorsInUse.TEXT.get());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultyLabel = new JLabel("Difficulty: Easy");
        difficultyLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
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

        // question is hardcoded, need to change when Question model exists
        questionText = new JTextArea("Q: Which Design Pattern is classified as 'Behavioral'?");
        questionText.setFont(new Font("Segoe UI Black", Font.BOLD, 20));
        questionText.setForeground(ColorsInUse.TEXT.get());
        questionText.setBackground(ColorsInUse.BG_COLOR.get());
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setEditable(false);
        questionText.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(questionText);
        centerPanel.add(Box.createVerticalStrut(20));

        JPanel answersGrid = new JPanel(new GridLayout(4, 1, 0, 15));
        answersGrid.setBackground(ColorsInUse.BG_COLOR.get());
        answersGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        answersGrid.setMaximumSize(new Dimension(800, 300));

        //hardcoded answers, need to retrieve after Q model is ready
        answerButtons = new ArrayList<>();
        String[] options = {"A: Singleton", "B: Adapter", "C: Observer", "D: Factory Method"};

        for (int i = 0; i < options.length; i++) {
            JButton btn = createAnswerButton(options[i], i);
            answerButtons.add(btn);
            answersGrid.add(btn);
        }

        centerPanel.add(answersGrid);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(ColorsInUse.BG_COLOR.get());
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        buttonSubmit = createButton("Submit");
        buttonSubmit.setBackground(ColorsInUse.CONFIRM.get());

        bottomPanel.add(buttonSubmit);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.setContentPane(contentPane);
    }

    private void onSubmit() {

    }

    // --- HELPER METHODS ---

    private void updateSelection(int index) {
        this.selected = index;
        for (int i = 0; i < answerButtons.size(); i++) {
            JButton btn = answerButtons.get(i);
            if (i == index) {
                btn.setBorder(new CompoundBorder(new LineBorder(ColorsInUse.CONFIRM.get(), 2),new EmptyBorder(10, 15, 10, 15)));
            }
            else {
                btn.setBorder(new CompoundBorder(new LineBorder(ColorsInUse.BG_COLOR.get(), 2), new EmptyBorder(10, 15, 10, 15)));
            }
        }
        contentPane.revalidate();
        contentPane.repaint();
    }

    private JButton createAnswerButton(String text, int index) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
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

}



