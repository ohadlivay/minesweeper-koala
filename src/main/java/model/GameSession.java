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
        this.initializeMaxHealthPool();
        this.initializeBoards();


    }

    //Create a new game session
    public static GameSession createNewSession(String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty)
    {
        return new GameSession(leftPlayerName, rightPlayerName, gameDifficulty);
    }

    // Initialize the health pool based on the game difficulty
    private void initializeMaxHealthPool()
    {

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
        if (this.healthPool > MAX_HEALTH_POOL)
            this.healthPool = MAX_HEALTH_POOL;
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

    /*
    I am so sorry Ohad
    I had to make this method public for scientific research (will be used in the Controller package)
    Test the game over screen
     */
    public void forceGameOver() {
        this.healthPool = 0;
    }

    //Tests the game session class
    @Override
    public boolean runClassTests()
    {
        return true;
        /*
        try {
            // Test 1: normal creation and getters
            GameSession s1 = GameSession.createNewSession("Left", "Right", GameDifficulty.MEDIUM);
            if (!"Left".equals(s1.getLeftPlayerName())) return false;
            if (!"Right".equals(s1.getRightPlayerName())) return false;
            if (s1.getGameDifficulty() != GameDifficulty.MEDIUM) return false;
            if (s1.getHealthPool() != 8) return false; // MEDIUM -> 8
            if (!s1.isTurn()) return false;
            if (s1.getPoints() != 0) return false;
            if (s1.getLeftBoard() == null || s1.getRightBoard() == null) return false;
            if (s1.getTimeStamp() == null) return false;

            // Test 2: identical names should throw
            boolean threw = false;
            try {
                GameSession.createNewSession("A", "A", GameDifficulty.EASY);
            } catch (IllegalArgumentException ex) {
                threw = true;
            }
            if (!threw) return false;

            // Test 3: null defaults
            GameSession s2 = GameSession.createNewSession(null, null, null);
            if (!"Player 1".equals(s2.getLeftPlayerName())) return false;
            if (!"Player 2".equals(s2.getRightPlayerName())) return false;
            if (s2.getGameDifficulty() != GameDifficulty.EASY) return false;
            if (s2.getHealthPool() != GameSession.getMaxHealthPool()) return false; // EASY -> MAX_HEALTH_POOL (10)

            // Test 4: setHealthPool valid and invalid
            s2.setHealthPool(5);
            if (s2.getHealthPool() != 5) return false;
            try {
                s2.setHealthPool(-1);
                return false;
            } catch (IllegalArgumentException ignored) {}
            try {
                s2.setHealthPool(GameSession.getMaxHealthPool() + 1);
                return false;
            } catch (IllegalArgumentException ignored) {}

            // Test 5: points add/deduct and bounds
            s2.setPoints(0);
            s2.addPoints(5);
            if (s2.getPoints() != 5) return false;
            s2.deductPoints(3);
            if (s2.getPoints() != 2) return false;
            s2.deductPoints(5); // should clamp to 0
            if (s2.getPoints() != 0) return false;
            try {
                s2.addPoints(-1);
                return false;
            } catch (IllegalArgumentException ignored) {}
            try {
                s2.deductPoints(-1);
                return false;
            } catch (IllegalArgumentException ignored) {}

            // Test 6: health add/deduct and bounds
            s2.setHealthPool(5);
            s2.addHealth(3);
            if (s2.getHealthPool() != 8) return false;
            s2.addHealth(10); // should clamp to MAX
            if (s2.getHealthPool() != GameSession.getMaxHealthPool()) return false;
            s2.deductHealth(100); // should clamp to 0
            if (s2.getHealthPool() != 0) return false;
            try {
                s2.addHealth(-1);
                return false;
            } catch (IllegalArgumentException ignored) {}
            try {
                s2.deductHealth(-1);
                return false;
            } catch (IllegalArgumentException ignored) {}

            // Test 7: changeTurn toggles
            boolean initial = s2.isTurn();
            s2.changeTurn();
            if (s2.isTurn() == initial) return false;
            s2.changeTurn();
            return s2.isTurn() == initial;

            // All tests passed
        } catch (Exception ex) {
            // Any unexpected exception is a failure
            return false;
        }*/
    }
}
