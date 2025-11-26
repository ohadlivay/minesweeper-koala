package main.java.model;
import main.java.test.Testable;

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

public class GameSession implements Testable
{
    //Indicates when the game session was created
    private final LocalDateTime timeStamp;

    //Names of the players involved in the game session
    private final String rightPlayerName;
    private final String leftPlayerName;

    //Game difficulty
    private final GameDifficulty gameDifficulty;

    //Boards of the players involved in the game session
    private Board leftBoard;
    private Board rightBoard;

    //Health pool and points of the players involved in the game session
    private int healthPool;
    private int points;

    //Indicates whether it is the left player's turn or not
    private boolean turn;

    //Maximum health pool for a game session
    private static final int MAX_HEALTH_POOL = 10;

    //Constructors


    private GameSession(String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty)
    {
        this.timeStamp = LocalDateTime.now();
        if (Objects.equals(rightPlayerName, leftPlayerName)&&rightPlayerName!=null)
            throw new IllegalArgumentException("Right player name cannot be the same as left player name");
        this.gameDifficulty = Objects.requireNonNullElse(gameDifficulty, GameDifficulty.EASY);
        this.rightPlayerName = Objects.requireNonNullElse(rightPlayerName, "Player 2");
        this.leftPlayerName = Objects.requireNonNullElse(leftPlayerName, "Player 1");
        this.turn = true;
        this.healthPool = gameDifficulty.getInitialHealthPool();
        this.initializeBoards();


    }

    //Create a new game session
    public static GameSession createNewSession(String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty)
    {
        return new GameSession(leftPlayerName, rightPlayerName, gameDifficulty);
    }

    //Initialize the boards of the players involved in the game session
    private void initializeBoards()
    {
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

    public static int getMaxHealthPool()
    {
        return MAX_HEALTH_POOL;
    }

    //Changes the turn of the game session
    private void changeTurn()
    {
        this.turn = !this.turn;
    }

    //Adds points to the players' score
    private void addPoints(int points)
    {
        if (points < 0)
            throw new IllegalArgumentException("Invalid points");
        this.points += points;
    }

    //Deducts points from the players' score
    private void deductPoints(int points)
    {
        if (points < 0)
            throw new IllegalArgumentException("Invalid points");
        this.points -= points;
        if (this.points < 0)
            this.points = 0;
    }

    //Adds health to the players' health pool
    private void addHealth(int health)
    {
        if (health < 0)
            throw new IllegalArgumentException("Invalid health");
        this.healthPool += health;
        if (this.healthPool > MAX_HEALTH_POOL)
            this.healthPool = MAX_HEALTH_POOL;
    }

    //Deducts health from the players' health pool
    private void deductHealth(int health)
    {
        if (health < 0)
            throw new IllegalArgumentException("Invalid health");
        this.healthPool -= health;
        if (this.healthPool < 0)
            this.healthPool = 0;
    }

    /*
    I am so sorry Ohad
    I had to make this method public for scientific research (will be used in the Controller package)
    Test the game over screen
     */
    public void forceGameOver() {
        this.healthPool = 0;
        initiateGameOver();
    }

    //Methods for the gameplay
    private boolean isGameOver(boolean left)
    {
        boolean over = (left) ? leftBoard.allMinesRevealed() : rightBoard.allMinesRevealed();
        return over || healthPool <= 0;
    }
    private void initiateGameOver()
    {
        leftBoard.revealAll();
        rightBoard.revealAll();
        if (healthPool > 0)
            addPoints(healthPool*gameDifficulty.getActivationCost());

    }

    public void reveal(int r, int c,boolean left) throws Exception
    {
        Board board = (left) ? leftBoard : rightBoard;
        boolean activated = true;
        try
        {
            activated = board.reveal(r, c);
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!activated)
        {
            Tile t = board.getTiles()[r][c];
            if (t instanceof MineTile)
                deductHealth(1);
            else if (t instanceof NumberTile nt)
            {
                addPoints(1);
                if (nt.getAdjacentMines()==0)
                    board.cascade(r, c);
            }
                
        }
        if (!isGameOver(left))
            changeTurn();

    }
    public void flag(int r, int c,boolean left) throws Exception
    {
        Board board = (left) ? leftBoard : rightBoard;
        boolean activated = true;
        try {
            activated = board.flag(r, c);
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!activated)
        {
            Tile t = board.getTiles()[r][c];
            if (t instanceof MineTile)
            {
                addPoints(1);
                boolean over = false;
                over = board.reveal(r,c);
                over = board.allMinesRevealed();
            }
            else if (t instanceof NumberTile nt)
            {
                deductPoints(3);
            }
                
        }

    }
    public void unflag(int r, int c,boolean left) throws Exception
    {
        Board board = (left) ? leftBoard : rightBoard;
        try
        {
           board.unflag(r, c);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    

    //Tests the game session class
    @Override
    public boolean runClassTests()
    {
        return true;
    }
}
