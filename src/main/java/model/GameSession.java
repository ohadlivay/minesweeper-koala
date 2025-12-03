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

<<<<<<< Updated upstream
=======
    private List<PointsListener> pointsListeners = new ArrayList<>();
    private List<HealthListener> healthListeners = new ArrayList<>();
    private List<GameOverListener> gameOverListeners = new ArrayList<>();

>>>>>>> Stashed changes
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
        setPoints(0);
        initiateGameOver();
    }

    //Methods for the gameplay
    private boolean isGameOver()
    {
        return leftBoard.allMinesRevealed() || rightBoard.allMinesRevealed() || healthPool <= 0;
    }
    private void initiateGameOver()
    {
        leftBoard.revealAll();
        rightBoard.revealAll();
        if (healthPool > 0)
            this.gainPoints(healthPool*gameDifficulty.getActivationCost());
        for (GameOverListener listener: gameOverListeners)
            listener.gameOver();

    }

    public void reveal(int r, int c,boolean left) throws Exception
    {
        Board board = (left) ? leftBoard : rightBoard;
        Board.RevealResult result;
        try
        {
            result = board.reveal(r, c);
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!result.wasActivated)
        {
            Tile t = result.revealedTile;
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
        else initiateGameOver();

    }
    public void flag(int r, int c,boolean left) throws Exception
    {
        Board board = (left) ? leftBoard : rightBoard;
        Tile tile = board.getTileAt(r, c);
        if (tile == null) {
            throw new Exception("Invalid tile coordinates");
        }
        if (tile instanceof MineTile)
        {
            if (!tile.isRevealed())
            {
                try {
                    board.reveal(r, c);
                } catch (Exception ignored) {}
                addPoints(1);
                boolean over = board.allMinesRevealed();
            }
        }
        Board.FlagResult result;
        try {
            result = board.flag(r, c);
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!result.wasActivated)
        {
            Tile t = result.flaggedTile;
            if (t instanceof NumberTile nt)
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
<<<<<<< Updated upstream
=======

    public void RightClickedTile(Tile tile) {
        /*
        this encompasses all the logic that happens when a user tries to flag/unflag a tile
        its responsible for switching turns, gaining points and ordering board to flag/unflag
         */
        System.out.println("Right clicked tile");
        Board parentBoard = tile.getParentBoard();

        //turn test
        if(!parentBoard.getTurn()){
            System.out.println("Invalid turn");
            return; //not his turn
        }

        //case tile was already revealed
        if(tile.isRevealed()) {
            System.out.println("already Revealed tile");
            return;
        }

        //case tile is a mine
        if(tile instanceof MineTile){
            System.out.println("Flagging and revealing mine");
            this.gainPoints(1);
            parentBoard.reveal(tile);
            if (isGameOver()) initiateGameOver();
            this.changeTurn();   //revealing a mine by flagging does change a turn!
            return;
        }

        //case tile is flagged (unflag it)
        if(tile.isFlagged()) {
            System.out.println("Unflagging tile");
            parentBoard.unflag(tile);
          //  this.changeTurn();
            return;
        }

        //case tile is not flagged (flag it)
        System.out.println("Flagging tile");
        parentBoard.flag(tile);
        this.gainPoints(-3);
        //this.changeTurn();
    }

    public void LeftClickedTile(Tile tile) {
        /*
        this encompasses all the logic that happens when a user tries to reveal a tile
        its responsible for switching turns, gaining points and ordering board to reveal
         */
        System.out.println("Left clicked tile");
        Board parentBoard = tile.getParentBoard();

        //turn test
        if( ! parentBoard.getTurn()){
            System.out.println("Invalid turn");
            return; //not his turn
        }

        //case tile was already revealed
        if(tile.isRevealed()) {
            System.out.println("already Revealed tile");
            return;
        }

        //in case tile is flagged (do nothing)
        if(tile.isFlagged()) {
            System.out.println("tile is flagged and cannot be revealed");
            return;
        }

        //case its a mine
        if(tile instanceof MineTile){
            System.out.println("Mine");
            this.gainHealth(-1);
            parentBoard.reveal(tile);
            this.changeTurn();
            return;
        }
        if(tile instanceof NumberTile){
            this.gainPoints(1);
            System.out.println("Its a number tile");
            parentBoard.reveal(tile);
        }

        if (isGameOver()) initiateGameOver();
        this.changeTurn();

    }
    private boolean hisTurn(Tile tile){
        return tile.getParentBoard().getTurn();
    }

    private void gainPoints(int points){
        this.setPoints(this.getPoints() + points);
    }
    private void gainHealth(int health){this.setHealth(this.getHealthPool() + health);}

    private void setPoints(int i) {
        this.points = i;
        if(points < 0) points = 0;
        for (PointsListener listener : pointsListeners) {
            listener.onPointsChanged(i); // your view should implement PointsListener and that method onPointsChanged should update the view
        }
    }
    private void setHealth(int health)
    {
        this.healthPool = health;
        if(healthPool < 0) healthPool = 0;
        if(healthPool > MAX_HEALTH_POOL) healthPool = MAX_HEALTH_POOL;
        for (HealthListener listener : healthListeners){
            listener.onHealthChanged(health); // your view should implement HealthListener and that method onHealthChanged should update the view
        }

    }
>>>>>>> Stashed changes
}
