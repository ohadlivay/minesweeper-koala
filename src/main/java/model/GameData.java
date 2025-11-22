package main.java.model;

import main.java.test.Testable;

import java.time.LocalDateTime;

// GameData class to store the game data after the game is over
public class GameData implements Testable
{
    private final LocalDateTime timeStamp;
    private final String leftPlayerName;
    private final String rightPlayerName;
    private final GameDifficulty gameDifficulty;
    private final int points;

    //Constructors
    public GameData(GameSession gameSession)
    {
        this.timeStamp = gameSession.getTimeStamp();
        this.leftPlayerName = gameSession.getLeftPlayerName();
        this.rightPlayerName = gameSession.getRightPlayerName();
        this.gameDifficulty = gameSession.getGameDifficulty();
        this.points = gameSession.getPoints();
    }

    //Copy constructor
    public GameData(LocalDateTime timeStamp, String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty, int points)
    {
        this.timeStamp = timeStamp;
        this.leftPlayerName = leftPlayerName;
        this.rightPlayerName = rightPlayerName;
        this.gameDifficulty = gameDifficulty;
        this.points = points;
    }

    //Getters
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getRightPlayerName() {
        return rightPlayerName;
    }

    public String getLeftPlayerName() {
        return leftPlayerName;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public int getPoints() {
        return points;
    }

    //Class tests
    @Override
    public boolean runClassTests()
    {
        try {
            LocalDateTime ts = LocalDateTime.of(2020, 1, 1, 12, 0);
            GameData gd = new GameData(ts, "Left", "Right", GameDifficulty.HARD, 42);
            if (gd.getTimeStamp() == null || !gd.getTimeStamp().equals(ts)) return false;
            if (!"Left".equals(gd.getLeftPlayerName())) return false;
            if (!"Right".equals(gd.getRightPlayerName())) return false;
            if (gd.getGameDifficulty() != GameDifficulty.HARD) return false;
            return gd.getPoints() == 42;

            // All checks passed
        } catch (Exception ex) {
            return false;
        }
    }
}
