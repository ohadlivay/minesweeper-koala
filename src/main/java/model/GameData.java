package main.java.model;

import main.java.test.Testable;

import java.time.LocalDateTime;

// GameData class to store the game data after the game is over
public class GameData
{
    private final LocalDateTime timeStamp;
    private final String leftPlayerName;
    private final String rightPlayerName;
    private final GameDifficulty gameDifficulty;
    private final int points;
    private final boolean isWin;

    //Constructors
    public GameData(GameSession gameSession)
    {
        this.timeStamp = gameSession.getTimeStamp();
        this.leftPlayerName = gameSession.getLeftPlayerName();
        this.rightPlayerName = gameSession.getRightPlayerName();
        this.gameDifficulty = gameSession.getGameDifficulty();
        this.points = gameSession.getPoints();
        this.isWin = gameSession.getHealthPool()>0;
    }

    //Copy constructor
    public GameData(LocalDateTime timeStamp, String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty, int points, boolean isWin)
    {
        this.timeStamp = timeStamp;
        this.leftPlayerName = leftPlayerName;
        this.rightPlayerName = rightPlayerName;
        this.gameDifficulty = gameDifficulty;
        this.points = points;
        this.isWin = isWin;
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

    public boolean isWin() { return isWin; }
    
}
