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

    //Maximum health pool for a game session
    private static final int MAX_HEALTH_POOL = 10;

    private List<PointsListener> pointsListeners = new ArrayList<>();
    private List<HealthListener> healthListeners = new ArrayList<>();
    private List<ActionMadeListener> actionMadeListeners = new ArrayList<>();
    private List<SpecialTileActivationListener> specialTileActivationListeners = new ArrayList<>();
    private String message = "";
    private static GameSession instance;
    private static GameSession testInstance;
    private DisplayQuestionListener displayQuestionListener;

    //Constructors
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

    private void initiateGameStats() {
        this.setPoints(0);
        this.setHealthPool(getGameDifficulty().getInitialHealthPool());
    }


    //Initialize the boards of the players involved in the game session
    public void initializeBoards()
    {
        this.setLeftBoard(Board.createNewBoard(this.getGameDifficulty()));
        this.setRightBoard(Board.createNewBoard(this.getGameDifficulty()));
        this.getLeftBoard().setTurn(true);
    }

    public void initGame()
    {
        initiateGameStats();
        initializeBoards();
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
            gainPoints(getHealthPool()*getGameDifficulty().getActivationCost());

    }

    public void RightClickedTile(Tile tile) {
        /*
        this encompasses all the logic that happens when a user tries to flag/unflag a tile
        its responsible for switching turns, gaining points and ordering board to flag/unflag
         */
        message = "";
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
            message = "Flagging and revealing mine";
            this.gainPoints(1);
            parentBoard.reveal(tile);
            this.changeTurn();   //revealing a mine by flagging does change a turn!
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
            notifyListenersAfterAction(message,true,0,1);
            if (this.isGameOver())
                initiateGameOver();
            else
            {
                return;
            }

        }

        //case tile is flagged (unflag it)
        if(tile.isFlagged()) {
            System.out.println("Unflagging tile");
            message = "Unflagging tile";
            parentBoard.unflag(tile);
            notifyListenersAfterAction(message,true,0,0);
            //  this.changeTurn();
            return;
        }

        //case tile is not flagged (flag it)
        if(!tile.isActivated()) {
            System.out.println("Flagging tile");
            message = "Flagging tile";
            parentBoard.flag(tile);
            this.gainPoints(-3);
            notifyListenersAfterAction(message,false,0,-3);
        }
        System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
        //this.changeTurn();
    }

    public boolean LeftClickedTile(Tile tile) {
        /*
        this encompasses all the logic that happens when a user tries to reveal a tile
        its responsible for switching turns, gaining points and ordering board to reveal
         */
        message = "";
        System.out.println("Left clicked tile");
        Board parentBoard = tile.getParentBoard();

        //turn test
        if( ! parentBoard.getTurn()){
            System.out.println("Invalid turn");
            return false; //not his turn
        }

        //case tile was already revealed
        if(tile.isRevealed()) {
            if (tile instanceof SpecialTile specialTile)
            {
                if (!specialTile.isUsed())
                {
                    this.activateSpecialTile(specialTile,parentBoard);
                    if (this.isGameOver())
                        initiateGameOver();
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
                return false;
            }

            //case its a mine
            if(tile instanceof MineTile){
                System.out.println("Mine");
                message = "Mine revealed, lost 1 health";
                this.gainHealth(-1);
                parentBoard.reveal(tile);
                notifyListenersAfterAction(message,false,-1,0);
                if (this.isGameOver())
                    initiateGameOver();
                else
                    this.changeTurn();
            }
            if(tile instanceof NumberTile){
                int tilesRevealed = parentBoard.reveal(tile);
                this.gainPoints(1*tilesRevealed);
                System.out.println("Its a number tile");
                message = "Number tiles revealed, gained "+(1*tilesRevealed)+" points";
                notifyListenersAfterAction(message,true,0,1*tilesRevealed);
                this.changeTurn();}
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
        }

        return true;
    }

    private boolean hisTurn(Tile tile){
        return tile.getParentBoard().getTurn();
    }

    private void gainPoints(int points){
        System.out.println("Points 'added': "+points);
        this.setPoints(this.getPoints() + points);
    }

    public void setPoints(int i) {
        this.points = i;
        if (this.getPoints()<0) this.points = 0;

        for (PointsListener listener : pointsListeners) {
            listener.onPointsChanged(i); // your view should implement PointsListener and that method onPointsChanged should update the view
        }
    }

    private void gainHealth(int health) {
        System.out.println("Health 'added': "+health);
        this.setHealthPool(this.getHealthPool() + health);
    }

    // didnt want to make gainHealth public so... for now this stupid solution until we think of something smarter :(
    public void testOnlyGainHealth(int health) {
        System.out.println("Health 'added': "+health);
        this.setHealthPool(this.getHealthPool() + health);
    }

    private void setHealthPool(int i) {
        this.healthPool = i;
        if (this.getHealthPool()<0) this.healthPool = 0;
        if (this.getHealthPool()>MAX_HEALTH_POOL) {
            this.gainPoints((i-MAX_HEALTH_POOL)*getGameDifficulty().getActivationCost());
            this.healthPool = MAX_HEALTH_POOL;
        }

        for (HealthListener listener : healthListeners)
            listener.onHealthChanged(this.healthPool); // your view should implement HealthListener and that method onHealthChanged should update the view
    }

    private boolean activateSpecialTile(SpecialTile specialTile, Board parentBoard) {
        System.out.println("Activation cost: " + getGameDifficulty().getActivationCost() + "\tPoints: " + this.getPoints());
        if (this.getPoints() < getGameDifficulty().getActivationCost()){
            System.out.println("Not enough points to activate special tile");
            message = "Not enough points to activate special tile";
            notifyListenersAfterAction(message,false,0,0);
            return false;
        }
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
                this.message = message+" Points changed by: "+(plusMinus*getGameDifficulty().getSurprisePoints())+",Health changed by: "+(plusMinus*getGameDifficulty().getSurpriseHealth());
                this.gainPoints(plusMinus*getGameDifficulty().getSurprisePoints());
                this.gainHealth(plusMinus*getGameDifficulty().getSurpriseHealth());
                notifyListenersAfterAction(this.message,resultOfRandom,plusMinus*getGameDifficulty().getSurpriseHealth(),plusMinus*getGameDifficulty().getSurprisePoints());
            }
            if (specialTile instanceof QuestionTile questionTile)
            {
                System.out.println("Question tile activated");
                displayQuestionListener.displayQuestion();
                QuestionResult result = QuestionResult.getInstance();
                QuestionDifficulty difficulty = result.getDifficulty();
                boolean answer = result.isCorrect();
                updateAfterQuestionResult(difficulty, answer, parentBoard);

            }
            specialTile.setUsed();
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
            /*for (SpecialTileActivationListener listener : specialTileActivationListeners)
                listener.onSpecialTileActivated(); // your view should implement SpecialTileActivationListener and that method onSpecialTileActivated should update the view
                */
        }
        return true;
    }
    public void setDisplayQuestionListener(DisplayQuestionListener displayQuestionListener) {
        this.displayQuestionListener = displayQuestionListener;
    }
    private void notifyListenersAfterAction(String message, boolean positiveMove, int healthChange, int pointsChange)
    {
        for (ActionMadeListener listener : actionMadeListeners)
            listener.onActionMade(message,positiveMove,healthChange,pointsChange);
    }
    private void updateAfterQuestionResult(QuestionDifficulty difficulty, boolean correctAnswer, Board parentBoard)
    {
        Random random = new Random();
        boolean randomResult = random.nextBoolean();
        switch (this.gameDifficulty)
        {
            case EASY:
                updateAfterQuestionResultEasy(difficulty, correctAnswer, randomResult, parentBoard);
                break;
            case MEDIUM:
                updateAfterQuestionResultMedium(difficulty,correctAnswer,randomResult);
                break;
            case HARD:
                updateAfterQuestionResultHard(difficulty,correctAnswer,randomResult);
        }

    }

    private void updateAfterQuestionResultHard(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult) {
        switch (difficulty) {
            case EASY:
                if (correctAnswer) {
                    this.gainPoints(10);
                    this.gainHealth(1);
                } else {
                    this.gainPoints(-10);
                    this.gainHealth(-1);
                }
                break;
            case MEDIUM:
                System.out.println("Randomer is "+randomResult);
                int healthChanged = (randomResult)? 2:1;
                if (correctAnswer)
                {
                    this.gainPoints(15);
                    this.gainHealth(healthChanged);
                }
                else
                {
                    this.gainPoints(-15);
                    this.gainHealth(-healthChanged);
                }
                break;
            case HARD:
                if (correctAnswer)
                {
                    this.gainPoints(20);
                    this.gainHealth(2);
                }
                else
                {
                    this.gainPoints(-20);
                    this.gainHealth(-2);
                }
                break;
            case MASTER:
                if (correctAnswer)
                {
                    this.gainPoints(40);
                    this.gainHealth(3);
                }
                else
                {
                    this.gainPoints(-40);
                    this.gainHealth(-3);
                }
        }
    }

    private void updateAfterQuestionResultMedium(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult) {
        switch (difficulty) {
            case EASY:
                if (correctAnswer) {
                    this.gainPoints(8);
                    this.gainHealth(1);
                } else {
                    this.gainPoints(-8);
                }
                break;
            case MEDIUM:
                if (correctAnswer)
                {
                    this.gainPoints(10);
                    this.gainHealth(1);
                }
                else
                {
                    System.out.println("Randomer is "+randomResult);
                    if (!randomResult)
                    {
                        this.gainPoints(-10);
                        this.gainHealth(-1);
                    }

                }
                break;
            case HARD:
                if (correctAnswer)
                {
                    this.gainPoints(15);
                    this.gainHealth(1);
                }
                else
                {
                    this.gainPoints(-15);
                    this.gainHealth(-1);
                }
                break;
            case MASTER:
                if (correctAnswer)
                {
                    this.gainPoints(20);
                    this.gainHealth(2);
                }
                else
                {
                    this.gainPoints(-20);
                    System.out.println("Randomer is "+randomResult);
                    int healthLost = (randomResult)? -1:-2;
                    this.gainHealth(healthLost);
                }
        }
    }

    private void updateAfterQuestionResultEasy(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult, Board parentBoard)
    {
        switch(difficulty){
            case EASY:
                if(correctAnswer)
                {
                    this.gainPoints(3);
                    this.gainHealth(1);
                }
                else
                {
                    System.out.println("Randomer is "+randomResult);
                    int pointsLost = (randomResult)? 0:-3;
                    this.gainPoints(pointsLost);
                }
                break;
            case MEDIUM:
                if(correctAnswer)
                {
                    parentBoard.revealRandomMine();
                    this.gainPoints(6);
                }
                else
                {
                    System.out.println("Randomer is "+randomResult);
                    int pointsLost = (randomResult)? 0:-6;
                    this.gainPoints(pointsLost);
                }
                break;
            case HARD:
                if(correctAnswer)
                {
                    parentBoard.revealGrid();
                    this.gainPoints(10);
                }
                else
                {
                    this.gainPoints(-10);
                }
                break;
            case MASTER:
                if(correctAnswer)
                {
                    this.gainPoints(15);
                    this.gainHealth(2);
                }
                else
                {
                    this.gainPoints(-15);
                    this.gainHealth(-1);
                }
                break;
        }
    }

    public void setPointsListener(PointsListener pointsListener) {
        this.pointsListeners.add(pointsListener);
    }

    public void setHealthListener(HealthListener healthListener) {
        this.healthListeners.add(healthListener);
    }

    public void setActionMadeListener(ActionMadeListener actionMadeListener) {
        this.actionMadeListeners.add(actionMadeListener);
    }

}