/* Asserted imports for this class:
model.GameSession
 */


package main.java.controller;

import main.java.model.*;
import main.java.util.GameDataCSVManager;
import main.java.view.GameScreen;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;

public class GameSessionController implements DisplayQuestionListener {
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

    // retrieves user inputs and sets up a new game session
    public void setupGame(String leftName, String rightName, GameDifficulty difficulty) {
        session = GameSession.getInstance();
        assert session != null;
        if( !(session.setLeftPlayerName(leftName) && session.setRightPlayerName(rightName) && session.setGameDifficulty(difficulty))) {
            System.out.println("couldnt set either difficulty or player names");
        }
        session.initializeBoards();
        session.setDisplayQuestionListener(this);
    }

    // Creates a new GameScreen with the current session
    public GameScreen startNewGame(NavigationController nav) {
        return new GameScreen(nav, session);
    }

    public void tileRightClick(Tile tile) {

        if(!tile.getParentBoard().getTurn()){
            return;
        }
        session.RightClickedTile(tile);
    }

    public void tileLeftClick(Tile tile) {
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

    public GameSession getSession() {
        return session;
    }

    @Override
    public void displayQuestion() {
        System.out.println("display question called in controller");
        QuestionResult result = QuestionResult.getInstance();
        /*
        result.setCorrect(true);
        result.setDifficulty(QuestionDifficulty.HARD);

         */
        Random rand = new Random();
        boolean answer = rand.nextBoolean();
        result.setCorrect(answer);
        QuestionDifficulty[] difficulties = QuestionDifficulty.values();
        int randomDifficultyIndex = rand.nextInt(difficulties.length);
        result.setDifficulty(difficulties[randomDifficultyIndex]);
        String correct = answer ? "Correct" : "Incorrect";

        System.out.println("Your answer was: "+result.isCorrect()+ " and the difficulty was: "+result.getDifficulty());
    }
}


