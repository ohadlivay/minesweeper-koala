package main.java.model;
import java.time.LocalDateTime;
import java.util.Objects;


/*
Dear Ohad,
I would love to create the boards myself using the constructor or initiate the method with the appropriate values.
unfortunately, I do not have access to those methods as they are both Private
*/

public class GameSession
{
    //Indicates when the game session was created
    private final LocalDateTime timeStamp;

    //Names of the players involved in the game session
    private String rightPlayerName;
    private String leftPlayerName;

    //Game difficulty
    private Difficulty gameDifficulty;

    //Boards of the players involved in the game session
    private Board leftBoard;
    private Board rightBoard;

    //Health pool and points of the players involved in the game session
    private int healthPool;
    private int points;

    //Indicates whether it is the left player's turn or not
    private boolean turn;

    //Constructors


    public GameSession(String leftPlayerName, String rightPlayerName, Difficulty gameDifficulty)
    {
        this.timeStamp = LocalDateTime.now();
        if (Objects.equals(rightPlayerName, leftPlayerName))
            throw new IllegalArgumentException("Right player name cannot be the same as left player name");
        this.gameDifficulty = Objects.requireNonNullElse(gameDifficulty, Difficulty.EASY);
        this.rightPlayerName = Objects.requireNonNullElse(rightPlayerName, "Player 2");
        this.leftPlayerName = Objects.requireNonNullElse(leftPlayerName, "Player 1");
        this.turn = true;
        // Initialize the boards
        this.leftBoard = new Board();
        this.rightBoard = new Board();
        this.initializeBoards();

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

    public Difficulty getGameDifficulty() {
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
        this.healthPool = healthPool;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setGameDifficulty(Difficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    //Methods

    //Initializes the boards with the given grid size and number of mines
    private void initializeBoards()
    {
        leftBoard.populateBoard();
        rightBoard.populateBoard();
    }


}
