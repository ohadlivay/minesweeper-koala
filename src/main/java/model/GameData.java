package main.java.model;

import java.time.LocalDateTime;

// GameData class to store the game data after the game is over
public class GameData
{
    private final LocalDateTime timeStamp;
    private final String rightPlayerName;
    private final String leftPlayerName;
    private final Difficulty gameDifficulty;
    private final int points;

    //Constructors
    public GameData(GameSession gameSession)
    {
        this.timeStamp = gameSession.getTimeStamp();
        this.rightPlayerName = gameSession.getRightPlayerName();
        this.leftPlayerName = gameSession.getLeftPlayerName();
        this.gameDifficulty = gameSession.getGameDifficulty();
        this.points = gameSession.getPoints();
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

    public Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public int getPoints() {
        return points;
    }
}
