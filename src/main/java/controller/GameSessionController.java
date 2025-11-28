/* Asserted imports for this class:
model.GameSession
 */


package main.java.controller;

import main.java.model.Board;
import main.java.model.GameSession;
import main.java.view.GameScreen;

public class GameSessionController {

    private static GameSessionController controller;

    private GameSessionController() {

    }

    public static GameSessionController getinstance() {
        if (GameSessionController.controller == null) {
            GameSessionController.controller = new GameSessionController();
        }
        return GameSessionController.controller;
    }

    public GameScreen startNewGame(NavigationController nav) {
        BoardsController boardsController = BoardsController.getInstance();
        boardsController.initBoards();

        Board left  = boardsController.getBoard1();
        Board right = boardsController.getBoard2();

        GameScreen gameScreen = new GameScreen(nav);
        gameScreen.setBoards(left, right);

        return gameScreen;
    }


}

