package main.java.view;

import main.java.controller.NavigationController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuestionManagerScreen extends JPanel {

    private final NavigationController nav;

    private JPanel mainPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JButton homeButton;
    private JLabel historyLabel;


    public QuestionManagerScreen(NavigationController navigationController) {

        nav = navigationController;
        initUI();

    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorsInUse.BG_COLOR.get());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //top panel holds Label
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        Font font = new Font("Segoe UI Black", Font.BOLD, 14);

        historyLabel = new JLabel("Question Manager (Under construction)", SwingConstants.CENTER);
        historyLabel.setForeground(ColorsInUse.TEXT_COLOR.get());
        historyLabel.setFont(font);
        topPanel.add(historyLabel, BorderLayout.CENTER);
        topPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        //bottom panel holds home button
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton homeButton = createHomeButton();
        homeButton.addActionListener(e -> nav.goToHome());
        bottomPanel.add(homeButton, BorderLayout.WEST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createHomeButton() {
        homeButton = new JButton();
        homeButton.setPreferredSize(new Dimension(72, 36));
        java.net.URL icon = getClass().getResource("/home.png");
        if (icon != null) {
            homeButton.setIcon(new ImageIcon(icon));
        }

        homeButton.setBackground(new Color(10, 10, 10));
        homeButton.setBorder(BorderFactory.createLineBorder(new Color(70, 80, 100), 2));
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(true);

        return homeButton;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
