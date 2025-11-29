/* Asserted imports for this class:
model.GameSession
 */


package main.java.controller;

import main.java.model.Board;
import main.java.model.GameDifficulty;
import main.java.model.GameSession;
import main.java.view.GameScreen;

public class GameSessionController {
    private GameSession session;

    private static GameSessionController controller;

    private GameSessionController() {

    }

    public static GameSessionController getInstance() {
        if (GameSessionController.controller == null) {
            GameSessionController.controller = new GameSessionController();
        }
        return GameSessionController.controller;
    }

    // for now only hardcoded values, later will get from user input
    // cool
    public GameScreen startNewGame(NavigationController nav) {
        String leftName  = "George";
        String rightName = "Ali";
        GameDifficulty difficulty = GameDifficulty.HARD;

        session = GameSession.getInstance();
        assert session != null;
        if( !(session.setLeftPlayerName(leftName) && session.setRightPlayerName(leftName) && session.setDifficulty(difficulty))){
            System.out.println("couldnt set either difficulty or player names");
        }

        return new GameScreen(nav, session);
    }





    public GameSession getSession() {
        return session;
    }
}

