package main.java.view;

import main.java.model.Tile;

import javax.swing.*;

public class TileViewTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TileView Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Tile tile = new Tile();          // model
            TileView tileView = new TileView(tile);  // view

            frame.add(tileView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
