package main.java.controller;

import main.java.model.Board;
import main.java.model.GameDifficulty;
import main.java.model.Question;
import main.java.view.overlays.*;

public class OverlayController {
    private static OverlayController instance;
    private final NavigationController nav;
    private OverlayView currentOverlay;

    private OverlayController(NavigationController nav) {
        this.nav = nav;
    }

    public static OverlayController getInstance(NavigationController nav) {
        if (instance == null) {
            instance = new OverlayController(nav);
        }
        return instance;
    }

    public static OverlayController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("OverlayController not initialized!");
        }
        return instance;
    }

    public void closeCurrentOverlay() {
        if (currentOverlay != null) {
            currentOverlay.close(); //triggers the unlocking of the main screen
            currentOverlay = null;
        }
    }

    public void showOverlay(OverlayType type) {
        closeCurrentOverlay(); //closes the previous overlay if its still open
        OverlayView overlay = getOverlay(type);
        if (overlay != null) {
            this.currentOverlay = overlay;
            overlay.open();
        }
    }

    public void showSettingsOverlay(String player1, String player2, GameDifficulty difficulty) {
        closeCurrentOverlay();
        SettingsOverlay overlay = new SettingsOverlay(nav, player1, player2, difficulty);
        this.currentOverlay = overlay;
        overlay.open();
    }

    public void showGameOverOverlay(boolean isWin, int score) {
        closeCurrentOverlay();
        GameOverOverlay overlay = new GameOverOverlay(nav, isWin, score);
        this.currentOverlay = overlay;
        overlay.open();
    }

    //this overlay specifically needs the current board
    public void showQuestionOverlay(Board board) {
        closeCurrentOverlay();
        ViewQuestionOverlay overlay = new ViewQuestionOverlay(nav);
        this.currentOverlay = overlay;
        overlay.displayQuestion(board);
        overlay.open();
    }

    public void showAddEditOverlay(Question question) {
        closeCurrentOverlay();
        AddEditQuestionOverlay overlay = new AddEditQuestionOverlay(nav, question);
        this.currentOverlay = overlay;
        overlay.open();
    }

    public void showDeleteQuestionOverlay(Question question) {
        closeCurrentOverlay();
        DeleteQuestionOverlay overlay = new DeleteQuestionOverlay(nav, question);
        this.currentOverlay = overlay;
        overlay.open();
    }

    //method to choose the current overlay based on the enum types
        private OverlayView getOverlay(OverlayType type) {
            OverlayView overlay = null;
            if (type == OverlayType.SETTINGS) {
                overlay = new SettingsOverlay(nav);
            }
            if (type == OverlayType.ADD_EDIT_QUESTION) {
                overlay = new AddEditQuestionOverlay(nav, null); //if this is called through generic showOverlay, we assume it's for adding a new question
            }
            if (type == OverlayType.VIEW_QUESTION) {
                overlay = new ViewQuestionOverlay(nav);
            }
            if (type == OverlayType.INSTRUCTIONS) {
                overlay = new GameInstructionOverlay(nav);
            }

            return overlay;
        }

    }


