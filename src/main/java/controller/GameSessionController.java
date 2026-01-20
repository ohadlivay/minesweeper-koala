/* Asserted imports for this class:
model.GameSession
 */

package main.java.controller;

import main.java.model.*;
import main.java.view.ColorsInUse;
import main.java.view.GameScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GameSessionController implements DisplayQuestionListener, InputBlockListener{
    private GameSession session;
    private boolean isBlocked = false;
    private ArrayList<InputBlockListener> blockListeners = new ArrayList<>();
    private static GameSessionController instance;
    private ColorsInUse leftBoardColor;
    private ColorsInUse rightBoardColor;
    
    private GameSessionController() {

    }

    public static GameSessionController getInstance() {
        if (GameSessionController.instance == null) {
            GameSessionController.instance = new GameSessionController();
        }
        return GameSessionController.instance;
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
        for (InputBlockListener listener : blockListeners) {
            listener.onInputBlock(blocked);
        }
    }

    public void addInputBlockListener(InputBlockListener listener) {
        this.blockListeners.add(listener);
    }

    // retrieves user inputs and sets up a new game session
    public void setupGame(String leftName, String rightName, GameDifficulty difficulty) {
        setupGame(leftName, rightName, difficulty, null, null);
    }

    // retrieves user inputs and sets up a new game session with board colors
    public void setupGame(String leftName, String rightName, GameDifficulty difficulty, ColorsInUse leftColor, ColorsInUse rightColor) {
        session = GameSession.getInstance();
        session.clearListeners();
        this.blockListeners.clear();
        this.isBlocked = false;
        this.leftBoardColor = leftColor;
        this.rightBoardColor = rightColor;
        assert session != null;
        if( !(session.setLeftPlayerName(leftName) && session.setRightPlayerName(rightName) && session.setGameDifficulty(difficulty))) {
            System.out.println("couldnt set either difficulty or player names");
        }
        session.initGame();
        session.setDisplayQuestionListener(this);
    }

    // Creates a new GameScreen with the current session
    public GameScreen startNewGame(NavigationController nav) {
        if (leftBoardColor != null && rightBoardColor != null) {
            return new GameScreen(nav, session, leftBoardColor, rightBoardColor);
        }
        return new GameScreen(nav, session);
    }

    public void tileRightClick(Tile tile) {
        if(isBlocked) {
            return;
        }
        /*if(!tile.getParentBoard().getTurn()){
            return;
        }
         */
        session.RightClickedTile(tile);
    }

    public void tileLeftClick(Tile tile) {
        if(isBlocked) {
            return;
        }
        /*
        if(!tile.getParentBoard().getTurn()){
            return;
        }
         */
        session.LeftClickedTile(tile);
    }

    private boolean isSaving = false;


    // used for testing purposes only
    public void endGame(GameSession session,NavigationController nav) throws IOException {
        if (isSaving)
            return;
        isSaving = true;
        try {
            session.forceGameOver();
        } finally {
            isSaving = false;
        }
    }

    @Override
    public void displayQuestion(Board board) {
        OverlayController.getInstance().showQuestionOverlay(board);
    }

    public GameSession getSession() {
        return session;
    }


    @Override
    public void onInputBlock(boolean isBlocked) {

    }

    public void setSurpriseToGameScreen(int healthChange, int pointsChange, boolean positiveMove) {
        session.updateAfterSurpriseRevealed(healthChange, pointsChange, positiveMove);
    }
    
}


