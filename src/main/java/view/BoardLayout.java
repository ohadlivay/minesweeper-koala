package main.java.view;

import main.java.controller.GameSessionController;
import main.java.model.Board;
import main.java.model.MinesLeftListener;
import main.java.model.Tile;
import main.java.model.TurnListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardLayout extends JPanel implements TurnListener, MinesLeftListener {

    private static final String CARD_BOARD  = "BOARD";
    private static final String CARD_HIDDEN = "HIDDEN";

    private final int rows;
    private final int cols;
    private final GameSessionController gameSessionController = GameSessionController.getInstance();
    private Board board; // only use for getters

    private final CardLayout cardLayout;
    private final JPanel gridPanel;    // holds the TileViews
    private final JPanel hiddenPanel;  // shown when it's not this player's turn

    public BoardLayout(Board board) {
        setBoard(board);
        this.rows = board.getRows();
        this.cols = board.getCols();

        // card layout on the whole BoardLayout
        this.cardLayout = new CardLayout();
        setLayout(cardLayout);

        // grid with tiles
        this.gridPanel = new JPanel(new GridLayout(rows, cols));
        gridPanel.setOpaque(true); // or false if background comes from outside

        // hidden screen
        this.hiddenPanel = createHiddenPanel();

        populateBoard();

        // add both cards
        add(gridPanel, CARD_BOARD);
        add(hiddenPanel, CARD_HIDDEN);

        // listen to turn changes
        board.setTurnListener(this);    // could this be the responsibility of the controller?
                                        //i think so -Liran

        // initial state depending on whose turn it is
        applyTurnStyling(board.getTurn());

        setupHover();
    }

    // Fill the grid with TileView
    private void populateBoard() {
        gridPanel.removeAll();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile t = board.getTiles()[i][j];
                if (t != null) {
                    gridPanel.add(new TileView(t));
                } else {
                    gridPanel.add(new JButton()); // placeholder button for debugging
                }
            }
        }

        revalidate();
        repaint();
    }

    // create the panel shown when this board is "hidden"
    private JPanel createHiddenPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(true);
        p.setBackground(new Color(0, 0, 0, 200));

        JLabel label = new JLabel("Waiting for your turn...", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));

        p.add(label, BorderLayout.CENTER);

        return p;
    }

    // Central place to switch card + border based on turn
    private void applyTurnStyling(boolean isPlayersTurn) {
        if (isPlayersTurn) {
            // show the real board
            cardLayout.show(this, CARD_BOARD);

            setBorder(BorderFactory.createMatteBorder(
                    5, 5, 5, 5,
                    new Color(255, 255, 0, 150)   // translucent yellow
            ));
        } else {
            // show the hidden / waiting screen
            cardLayout.show(this, CARD_HIDDEN);

            setBorder(BorderFactory.createMatteBorder(
                    5, 5, 5, 5,
                    new Color(0, 0, 0, 150)       // translucent black
            ));
        }

        revalidate();
        repaint();
    }

    private void setupHover() {
        hiddenPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!board.getTurn()) {
                    cardLayout.show(BoardLayout.this, CARD_BOARD);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!board.getTurn()) {
                    cardLayout.show(BoardLayout.this, CARD_HIDDEN);
                }
            }
        });

        gridPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!board.getTurn()) {
                    cardLayout.show(BoardLayout.this, CARD_BOARD);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!board.getTurn()) {
                    cardLayout.show(BoardLayout.this, CARD_HIDDEN);
                }
            }
        });
    }

    @Override
    public void updateTurn() {

        // is 400 enough time while also not feeling too laggy?
        new javax.swing.Timer(400, e -> {
            // whenever turn changes, update styling
            applyTurnStyling(board.getTurn());
        }) {{
            setRepeats(false);
            start();
        }};

    }

    @Override
    public void updateMinesLeft(int minesLeft) {
        /*
         whoever implements the mines left in this view;
         rest assured that this method will be used whenever minesLeft is updated.
         you just have to implement the gui thingies
        */
        // e.g. update a JLabel in some parent panel
    }

    // ===== getters & setters =====
    public void setBoard(Board board) {
        if (board == null)
            throw new IllegalArgumentException("Board cannot be null");
        this.board = board;
    }
}
