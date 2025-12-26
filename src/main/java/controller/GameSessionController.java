/* Asserted imports for this class:
model.GameSession
 */


package main.java.controller;

import main.java.model.*;
import main.java.util.GameDataCSVManager;
import main.java.view.GameScreen;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameSessionController implements DisplayQuestionListener, InputBlockListener{
    private GameSession session;
    private boolean isBlocked = false;
    private ArrayList<InputBlockListener> blockListeners = new ArrayList<>();
    private static GameSessionController instance;
    private NavigationController currentNav;
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
        this.blockListeners.clear();
        session = GameSession.getInstance();
        this.isBlocked = false;
        assert session != null;
        if( !(session.setLeftPlayerName(leftName) && session.setRightPlayerName(rightName) && session.setGameDifficulty(difficulty))) {
            System.out.println("couldnt set either difficulty or player names");
        }
        session.initGame();
        session.setDisplayQuestionListener(this);
    }

    // Creates a new GameScreen with the current session
    public GameScreen startNewGame(NavigationController nav) {
        this.currentNav = nav;
        return new GameScreen(nav, session);
    }

    public void tileRightClick(Tile tile) {
        if(isBlocked) {
            return;
        }

        if(!tile.getParentBoard().getTurn()){
            return;
        }
        session.RightClickedTile(tile);
    }

    public void tileLeftClick(Tile tile) {
        if(isBlocked) {
            return;
        }
        if(!tile.getParentBoard().getTurn()){
            return;
        }
        session.LeftClickedTile(tile);
    }

    public void endGame(GameSession session,NavigationController nav) throws IOException {
        session.forceGameOver();
        GameData gameData = new GameData(session);
        SysData.getInstance().addGame(gameData);
        GameDataCSVManager.writeGameDataListToCSV("GameHistory.csv");
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

    // TESTS FOR GAME OVER OVERLAY

    public void testShowWinUI() {
        if (currentNav == null) {
            System.err.println("NavigationController not set! Start a game first.");
            return;
        }
        // Parameters: isWin = true, score = 999
        OverlayController.getInstance().showGameOverOverlay(true, 999);
    }

    /**
     * Force shows the Loss Overlay for UI testing.
     */
    public void testShowLossUI() {
        if (currentNav == null) {
            System.err.println("NavigationController not set! Start a game first.");
            return;
        }
        // Parameters: isWin = false, score = 0
        OverlayController.getInstance().showGameOverOverlay(false, 0);
    }
}


