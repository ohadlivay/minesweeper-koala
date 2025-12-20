package main.java.view.overlays;

import main.java.controller.NavigationController;
import main.java.controller.QuestionManagerController;
import main.java.model.Question;
import main.java.view.ColorsInUse;
import main.java.view.FontsInUse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class DeleteQuestionOverlay extends OverlayView {
    private final Question question;
    public DeleteQuestionOverlay(NavigationController navigationController, Question question) {
        super(navigationController, true);
        this.question = question;
        initUI();
    }
    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(20,10));
        contentPanel.setBackground(ColorsInUse.BG_COLOR.get());
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setPreferredSize(new Dimension(500, 300));

        JLabel title = new JLabel("Delete Question?", SwingConstants.CENTER);
        title.setFont(FontsInUse.PIXEL.getSize(40f));
        title.setForeground(ColorsInUse.TEXT.get());
        contentPanel.add(title, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);

        String qText = question.getQuestionText();
        JTextPane message = new JTextPane();
        message.setText("Are you sure you want to delete the following question:\n\n\""
                + qText + "\"\n\n(This action cannot be undone)");
        message.setEditable(false);
        message.setFocusable(false);
        message.setOpaque(false);
        message.setBackground(ColorsInUse.BG_COLOR.get());
        message.setFont(FontsInUse.PIXEL.getSize(24f));
        message.setForeground(ColorsInUse.TEXT.get());

        StyledDocument doc = message.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        messagePanel.add(message, BorderLayout.CENTER);
        contentPanel.add(messagePanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        btnPanel.setOpaque(false);
        JButton btnDelete = createActionButton("Delete", ColorsInUse.DENY.get());
        btnDelete.addActionListener(e -> onDeleteConfirm());
        JButton btnCancel = createActionButton("Cancel", ColorsInUse.BOARD_BACKGROUND.get());
        btnCancel.addActionListener(e -> close());
        btnPanel.add(btnCancel);
        btnPanel.add(btnDelete);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        getContentPane().add(contentPanel);

    }

    private void onDeleteConfirm() {
        QuestionManagerController.getInstance().userDeletedQuestion(question);
        QuestionManagerController.getInstance().refreshQuestionList();
        close();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(FontsInUse.PIXEL.getSize(24f));
        label.setForeground(ColorsInUse.TEXT.get());
        return label;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FontsInUse.PIXEL.getSize(20f));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 45));
        return btn;
    }


}
