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

    public static GameSessionController getinstance() {
        if (GameSessionController.controller == null) {
            GameSessionController.controller = new GameSessionController();
        }
        return GameSessionController.controller;
    }

    // for now only hardcoded values, later will get from user input
    public GameScreen startNewGame(NavigationController nav) {
        String leftName  = "George";
        String rightName = "Ali";
        GameDifficulty difficulty = GameDifficulty.HARD;

        session = GameSession.createNewSession(leftName, rightName, difficulty);

        return new GameScreen(nav, session);
    }





    public GameSession getSession() {
        return session;
    }
}

