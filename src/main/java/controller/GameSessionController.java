/* Asserted imports for this class:
model.GameSession
 */


package main.java.controller;

import main.java.model.GameData;
import main.java.model.GameDifficulty;
import main.java.model.GameSession;
import main.java.model.Tile;
import main.java.view.GameScreen;

public class GameSessionController {
    private GameSession session;

    private static GameSessionController instance;

    private GameSessionController() {

    }

    public static GameSessionController getInstance() {
        if (GameSessionController.instance == null) {
            GameSessionController.instance = new GameSessionController();
        }
        return GameSessionController.instance;
    }

    // for now only hardcoded values, later will get from user input
    // cool
    public GameScreen startNewGame(NavigationController nav) {
        String leftName  = "George";
        String rightName = "Ali";
        GameDifficulty difficulty = GameDifficulty.HARD;

        session = GameSession.getInstance();
        assert session != null;
        if( !(session.setLeftPlayerName(leftName) && session.setRightPlayerName(rightName) && session.setDifficulty(difficulty))){
            System.out.println("couldnt set either difficulty or player names");
        }

        session.initializeBoards();

        return new GameScreen(nav, session);
    }

    public void tileRightClick(Tile tile) {

        /*
        when a player right clicks (tries to flag) a tile, this method is activated.
         */
        if(!tile.getParentBoard().getTurn()){
            return;
        }
        if (tile.isFlagged()) {
            tile.unflag();
        }
        else {
            tile.flag();
        }
    }

    public void tileLeftClick(Tile tile) {
        if(!tile.getParentBoard().getTurn()){
            return;
        }
        session.LeftClickedTile(tile);
    }

    public void endGame(GameSession session,NavigationController nav) {
        session.forceGameOver();

    }

    public GameSession getSession() {
        return session;
    }
}


