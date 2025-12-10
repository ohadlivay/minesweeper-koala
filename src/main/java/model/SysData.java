package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class SysData
{
    //Singleton instance
    private static SysData instance = null;
    //List of game data
    private final List<GameData> games;
    //need to add questions here??
    private final List<Question> questions;

    //Private constructor
    private SysData()
    {
        games = new ArrayList<>();
        questions = new ArrayList<>();
    }

    //Getter for the singleton instance
    public static SysData getInstance()
    {
        if (instance == null)
            instance = new SysData();
        return instance;
    }

    //Method to add a game to the list
    public void addGame(GameData game)
    {
        games.add(game);
    }

    //Method to get a game from the list
    public GameData getGame(int index)
    {
        return games.get(index);
    }

    //Method to get the number of games in the list
    public int getNumberOfGames()
    {
        return games.size();
    }

    //Method to clear the list of games
    public void clearGames()
    {
        games.clear();
    }

    //Method to get a copy of the game list
    public List<GameData> getGames()
    {
        return new ArrayList<>(games);
    }

    //Getter for the questions list
    public List<Question> getQuestions() {
        return questions;
    }

    //Add a question to the list
    public void addQuestion(Question question) {
        questions.add(question);
    }

    //Clear the list of questions
    public void clearQuestions() {
        questions.clear();
    }

    //Get a question from the list
    public void getQuestion(int index) {
        questions.get(index);
    }

    //Get the number of questions in the list
    public int getNumberOfQuestions() {
        return questions.size();
    }

}
