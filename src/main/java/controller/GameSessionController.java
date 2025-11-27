/* Asserted imports for this class:
model.GameSession
 */


package main.java.controller;

import main.java.model.GameSession;

public class GameSessionController {

    private static GameSessionController controller;

    private GameSessionController() {
        GameSessionController.controller = new GameSessionController();
    }

    public static GameSessionController getinstance() {
        if (GameSessionController.controller == null) {
            GameSessionController.controller = new GameSessionController();
        }
        return GameSessionController.controller;
    }


}

