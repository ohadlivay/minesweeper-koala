package main.java.model;
import java.time.LocalDateTime;
import java.util.Objects;
/*
Dear Ohad,
I would love to create the boards myself using the constructor or initiate the method with the appropriate values.
unfortunately, I do not have access to those methods as they are both Private
*/

/*
Dearest Thomas,

Sadly, it would be bad practice to expose my constructor or private methods,
as that would tightly couple your code to my internal implementation.
However! I have created a dedicated public method just for you.

You may safely call:
    createNewBoard(gameDifficulty)

Rest assured — it will return a valid and fully initialized Board.

Cheers and all the best to you and your family,
Ohad
*/

public class GameSession
{
    //Indicates when the game session was created
    private final LocalDateTime timeStamp;

    //Names of the players involved in the game session
    private String rightPlayerName;
    private String leftPlayerName;

    //Game difficulty
    private GameDifficulty gameDifficulty;

    //Boards of the players involved in the game session
    private Board leftBoard;
    private Board rightBoard;

    //Health pool and points of the players involved in the game session
    private int healthPool;
    private int points;

    //Indicates whether it is the left player's turn or not
    private boolean turn;

    //Constructors


    public GameSession(String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty)
    {
        this.timeStamp = LocalDateTime.now();
        if (Objects.equals(rightPlayerName, leftPlayerName))
            throw new IllegalArgumentException("Right player name cannot be the same as left player name");
        this.gameDifficulty = Objects.requireNonNullElse(gameDifficulty, GameDifficulty.EASY);
        this.rightPlayerName = Objects.requireNonNullElse(rightPlayerName, "Player 2");
        this.leftPlayerName = Objects.requireNonNullElse(leftPlayerName, "Player 1");
        this.turn = true;
        // Initialize the health pool based on the game difficulty
        switch (gameDifficulty)
        {
            case EASY:
                this.healthPool = 10;
                break;
            case MEDIUM:
                this.healthPool = 8;
                break;
            case HARD:
                this.healthPool = 6;
        }
        // Initialize the boards
        this.leftBoard = Board.createNewBoard(this.gameDifficulty);
        this.rightBoard = Board.createNewBoard(this.gameDifficulty);

    }

    //Getters and setters
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

    public Board getLeftBoard() {
        return leftBoard;
    }

    public Board getRightBoard() {
        return rightBoard;
    }

    public int getHealthPool() {
        return healthPool;
    }

    public int getPoints() {
        return points;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setRightPlayerName(String rightPlayerName) {
        this.rightPlayerName = rightPlayerName;
    }

    public void setLeftPlayerName(String leftPlayerName) {
        this.leftPlayerName = leftPlayerName;
    }

    public void setLeftBoard(Board leftBoard) {
        this.leftBoard = leftBoard;
    }

    public void setRightBoard(Board rightBoard) {
        this.rightBoard = rightBoard;
    }

    public void setHealthPool(int healthPool) {
        if (healthPool < 0||healthPool>10)
            throw new IllegalArgumentException("Invalid health pool");
        this.healthPool = healthPool;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    //Changes the turn of the game session
    public void changeTurn()
    {
        this.turn = !this.turn;
    }

    //Adds points to the players' score
    public void addPoints(int points)
    {
        if (points < 0)
            throw new IllegalArgumentException("Invalid points");
        this.points += points;
    }

    //Deducts points from the players' score
    public void deductPoints(int points)
    {
        if (points < 0)
            throw new IllegalArgumentException("Invalid points");
        this.points -= points;
        if (this.points < 0)
            this.points = 0;
    }

    //Adds health to the players' health pool
    public void addHealth(int health)
    {
        if (health < 0)
            throw new IllegalArgumentException("Invalid health");
        this.healthPool += health;
        if (this.healthPool > 10)
            this.healthPool = 10;
    }

    //Deducts health from the players' health pool
    public void deductHealth(int health)
    {
        if (health < 0)
            throw new IllegalArgumentException("Invalid health");
        this.healthPool -= health;
        if (this.healthPool < 0)
            this.healthPool = 0;
    }


}
