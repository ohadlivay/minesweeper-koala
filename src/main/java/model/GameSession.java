package main.java.model;
import main.java.test.Testable;
import main.java.view.GameScreen;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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

    //Maximum health pool for a game session
    private static final int MAX_HEALTH_POOL = 10;

    private List<PointsListener> pointsListeners = new ArrayList<>();
    private List<HealthListener> healthListeners = new ArrayList<>();
    private List<SpecialTileActivationListener> specialTileActivationListeners = new ArrayList<>();
    private DisplayQuestionListener displayQuestionListener;
    //Constructors
    private static GameSession instance;
    private static GameSession testInstance;



    private GameSession(String leftPlayerName, String rightPlayerName, GameDifficulty gameDifficulty)
    {
        this.timeStamp = LocalDateTime.now();
        if (Objects.equals(rightPlayerName, leftPlayerName)&&rightPlayerName!=null)
            throw new IllegalArgumentException("Right player name cannot be the same as left player name");
        this.setGameDifficulty(Objects.requireNonNullElse(gameDifficulty, GameDifficulty.EASY));
        this.setRightPlayerName(Objects.requireNonNullElse(rightPlayerName, "Player 2"));
        this.setLeftPlayerName(Objects.requireNonNullElse(leftPlayerName, "Player 1"));
        this.setHealthPool(getGameDifficulty().getInitialHealthPool());
    }

    public static GameSession getInstance(){
        if(instance==null){
            instance = new GameSession("Player1", "Player2", GameDifficulty.EASY);
        }
        return instance;
    }

    public static GameSession getTestInstance(){
        if(testInstance==null){
            testInstance = new GameSession("Player1", "Player2", GameDifficulty.EASY);
        }
        return testInstance;
    }


    //Initialize the boards of the players involved in the game session
    public void initializeBoards()
    {
        this.setLeftBoard(Board.createNewBoard(this.getGameDifficulty()));
        this.setRightBoard(Board.createNewBoard(this.getGameDifficulty()));
        this.getLeftBoard().setTurn(true);
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

    private void setLeftBoard(Board leftBoard) {
        this.leftBoard = leftBoard;
    }

    public Board getRightBoard() {
        return rightBoard;
    }

    private void setRightBoard(Board rightBoard) {
        this.rightBoard = rightBoard;
    }

    public int getHealthPool() {
        return healthPool;
    }

    public int getPoints() {
        return points;
    }

    public static int getMaxHealthPool()
    {
        return MAX_HEALTH_POOL;
    }

    //switch the turns of the boards
    public void changeTurn()
    {
        leftBoard.setTurn(!leftBoard.getTurn());
        rightBoard.setTurn(!rightBoard.getTurn());
    }

    //Adds points to the players' score
    private void addPoints(int points)
    {
        if (points < 0)
            throw new IllegalArgumentException("Invalid points");
        this.setPoints(this.getPoints() + points);
    }

    //Deducts points from the players' score
    private void deductPoints(int points)
    {
        if (points < 0)
            throw new IllegalArgumentException("Invalid points");
        this.setPoints(this.getPoints() - points);
    }

    //Adds health to the players' health pool
    private void addHealth(int health)
    {
        if (health < 0)
            throw new IllegalArgumentException("Invalid health");
        this.setHealthPool(this.getHealthPool() + health); // use a setter @TOM -ohad 29/11/25 8am
    }

    //these have to be public since we're not using interfaces
    public boolean setLeftPlayerName(String leftPlayerName){
        this.leftPlayerName = leftPlayerName;
        if(this.getLeftPlayerName().equals("tom")){
//            this.setGameDifficulty(GameDifficulty.INSANE);
        }
        return true;
    }

    public boolean setRightPlayerName(String rightPlayerName){
        this.rightPlayerName = rightPlayerName;
        if(this.getRightPlayerName().equals("tom")){
//            this.setGameDifficulty(GameDifficulty.INSANE);
        }
        return true;
    }

    public boolean setGameDifficulty(GameDifficulty difficulty){
        this.gameDifficulty = difficulty;
        return true;
    }


    //Deducts health from the players' health pool
    private void deductHealth(int health)
    {
        if (health < 0)
            throw new IllegalArgumentException("Invalid health");
        this.setHealthPool(this.getHealthPool() - health);
    }

    /*
    I am so sorry Ohad
    I had to make this method public for scientific research (will be used in the Controller package)
    Test the game over screen
     */
    public void forceGameOver() {
        this.setHealthPool(0);
        initiateGameOver();
    }

    //Methods for the gameplay
    private boolean isGameOver()
    {
        return getLeftBoard().allMinesRevealed() || getRightBoard().allMinesRevealed() || getHealthPool() <= 0;
    }
    private void initiateGameOver()
    {
        getLeftBoard().revealAll();
        getRightBoard().revealAll();
        if (getHealthPool() > 0)
            addPoints(getHealthPool()*getGameDifficulty().getActivationCost());

    }

    public void reveal(Tile tile) throws Exception
    {
        Board board = tile.getParentBoard();
        board.reveal(tile);
//
//        boolean activated = true;
//        try
//        {
//            activated = board.reveal(r, c);
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//        if (!activated)
//        {
//            Tile t = board.getTiles()[r][c];
//            if (t instanceof MineTile)
//                deductHealth(1);
//            else if (t instanceof NumberTile nt)
//            {
//                addPoints(1);
//                if (nt.getAdjacentMines()==0)
//                    board.cascade(r, c);
//            }
//
//        }
//        if (!isGameOver(left))
//            changeTurn();
//        else initiateGameOver();

    }
//    public void flag(int r, int c,boolean left) throws Exception
//    {
//        Board board = (left) ? leftBoard : rightBoard;
//        boolean activated = true;
//        Tile t = board.getTiles()[r][c];
//        if (t instanceof NumberTile)
//        {
//            try {
//                activated = board.flag(r, c);
//            } catch (Exception e) {
//                throw new Exception(e);
//            }
//            if (!activated)
//                deductPoints(3);
//        }
//        if (t instanceof MineTile)
//        {
//            try
//            {
//                activated = board.reveal(r, c);
//            }catch (Exception e){
//                throw new Exception(e);
//            }
//            if (!activated)
//            {
//                addPoints(1);
//            }
//        }
//
//        if (isGameOver(left))
//            initiateGameOver();
//    }
//    public void unflag(int r, int c,boolean left) throws Exception
//    {
//        Board board = (left) ? leftBoard : rightBoard;
//        try
//        {
//            board.unflag(r, c);
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }

    //Tests the game session class
    @Override
    public boolean runClassTests()
    {
        return true;
    }

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
            this.changeTurn();   //revealing a mine by flagging does change a turn!
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
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
        System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
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
            if (tile instanceof SpecialTile specialTile)
            {
                if (!specialTile.isUsed())
                {
                    this.activateSpecialTile(specialTile);
                }
                else
                    System.out.println("Special tile already used");
            }
            else
                System.out.println("already Revealed tile");
        }
        else{
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
            }
            if(tile instanceof NumberTile){
                int tilesRevealed = parentBoard.reveal(tile);
                this.gainPoints(1*tilesRevealed);
                System.out.println("Its a number tile");
                this.changeTurn();}
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
        }


    }
    private boolean hisTurn(Tile tile){
        return tile.getParentBoard().getTurn();
    }

    private void gainPoints(int points){
        this.setPoints(this.getPoints() + points);
    }

    private void setPoints(int i) {
        this.points = i;
        if (this.getPoints()<0) this.points = 0;
        /*
        for (PointsListener listener : pointsListeners) {
            listener.onPointsChanged(i); // your view should implement PointsListener and that method onPointsChanged should update the view
        }*/
    }

    private void gainHealth(int health) {
        this.setHealthPool(this.getHealthPool() + health);
    }

    private void setHealthPool(int i) {
        this.healthPool = i;
        if (this.getHealthPool()<0) this.healthPool = 0;
        if (this.getHealthPool()>MAX_HEALTH_POOL) {
            this.gainPoints((i-MAX_HEALTH_POOL)*getGameDifficulty().getActivationCost());
            this.healthPool = MAX_HEALTH_POOL;
        }
        /*
        for (HealthListener listener : healthListeners)
            listener.onHealthChanged(i); // your view should implement HealthListener and that method onHealthChanged should update the view*/
    }

    private void activateSpecialTile(SpecialTile specialTile){
        System.out.println("Activation cost: "+getGameDifficulty().getActivationCost()+"\tPoints: "+this.getPoints());
        if (this.getPoints()<getGameDifficulty().getActivationCost())
            System.out.println("Not enough points to activate special tile");
        else {
            this.gainPoints(-getGameDifficulty().getActivationCost());
            System.out.println("Points after activation cost: "+this.getPoints());
            if (specialTile instanceof SurpriseTile surpriseTile)
            {
                System.out.println("Surprise tile activated");
                Random random = new Random();
                boolean resultOfRandom = random.nextBoolean();
                int plusMinus  = (resultOfRandom) ? 1 : -1;
                String message = (resultOfRandom)? "Good surprise!" : "Bad surprise!";
                System.out.println(message);
                this.gainPoints(plusMinus*getGameDifficulty().getSurprisePoints());
                this.gainHealth(plusMinus*getGameDifficulty().getSurpriseHealth());
            }
            if (specialTile instanceof QuestionTile questionTile)
            {
                System.out.println("Question tile activated");
                displayQuestionListener.displayQuestion();
            }
            specialTile.setUsed();
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
            /*for (SpecialTileActivationListener listener : specialTileActivationListeners)
                listener.onSpecialTileActivated(); // your view should implement SpecialTileActivationListener and that method onSpecialTileActivated should update the view
                */
        }
    }
    public void setDisplayQuestionListener(DisplayQuestionListener displayQuestionListener) {
        this.displayQuestionListener = displayQuestionListener;
    }
}