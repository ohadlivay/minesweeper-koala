package main.java.model;

import main.java.test.Testable;

import java.util.ArrayList;
import java.util.List;

public class SysData implements Testable
{
    //Singleton instance
    private static SysData instance = null;
    //List of game data
    private final List<GameData> games;

    //Private constructor
    private SysData()
    {
        games = new ArrayList<>();
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

    //Class tests
    @Override
    public boolean runClassTests() {
        try {
            // ensure singleton and clean state
            SysData s1 = SysData.getInstance();
            s1.clearGames();
            if (s1.getNumberOfGames() != 0) return false;

            // add/get operations
            GameData g1 = new GameData(null, null, null, null, 0);
            GameData g2 = new GameData(null, null, null, null, 1);
            s1.addGame(g1);
            if (s1.getNumberOfGames() != 1) return false;
            if (s1.getGame(0) != g1) return false;

            s1.addGame(g2);
            if (s1.getNumberOfGames() != 2) return false;
            if (s1.getGame(1) != g2) return false;

            // getGames returns a copy (modifying returned list does not affect internal list)
            List<GameData> copy = s1.getGames();
            if (copy.size() != 2) return false;
            copy.remove(0);
            if (s1.getNumberOfGames() != 2) return false;

            // clear and verify empty
            s1.clearGames();
            if (s1.getNumberOfGames() != 0) return false;

            // getGame on empty should throw
            boolean threw = false;
            try {
                s1.getGame(0);
            } catch (IndexOutOfBoundsException ex) {
                threw = true;
            }
            if (!threw) return false;

            // singleton identity
            SysData s2 = SysData.getInstance();
            return s1 == s2;
        } catch (Exception e) {
            return false;
        }
    }
}
