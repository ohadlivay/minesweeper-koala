package main.java.model;
import main.java.test.Testable;
import main.java.util.GameDataCSVManager;
import main.java.util.SoundManager;
import main.java.view.GameScreen;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    // Points given after flagging a mine
    private static final int pointsForFlaggingMine = 1;
    // Health lost after revealing a mine
    private static final int healthForRevealingMine = -1;
    // Points lost after flagging a number tile
    private static final int pointsForFlaggingNumber = -3;
    // Points gained after revealing a number tile
    private static final int pointsForRevealingNumber = 1;


    private final Set<ActionMadeListener> actionMadeListeners = new HashSet<>();
    private final Set<SurpriseListener> surpriseListeners = new HashSet<>();
    private String message = "";
    private static GameSession instance;
    private static GameSession testInstance;
    private DisplayQuestionListener displayQuestionListener;
    private GameOverListener gameOverListener;

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
        SysData sysData = SysData.getInstance();
        if (sysData.getQuestions().size()<20)
            throw new IllegalStateException("Not enough questions in the system to start a game. Please add more questions.");
        this.isGameOverProcessing = false; // Reset the flag
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
        return true;
    }

    public boolean setRightPlayerName(String rightPlayerName){
        this.rightPlayerName = rightPlayerName;
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
        return getHealthPool() <= 0||getLeftBoard().allMinesRevealed() || getRightBoard().allMinesRevealed();
    }

    private boolean isGameOverProcessing = false;
    private void initiateGameOver()
    {
        if (isGameOverProcessing) return; // Prevent multiple saves/overlays
        isGameOverProcessing = true;
        boolean winOrLose = getHealthPool() > 0;
        getLeftBoard().revealAll();
        getRightBoard().revealAll();
        if (getHealthPool() > 0) {
            gainPoints(getHealthPool()*getGameDifficulty().getActivationCost());
            int healthBefore = getHealthPool();
            setHealthPool(0);
            notifyListenersAfterAction("Game over! You won!",true,-healthBefore,healthBefore*getGameDifficulty().getActivationCost());
        }
        else
            notifyListenersAfterAction("Game over! You lost!",false,0,0);
        try{
            saveGame();
            gameOverListener.onGameOver(true,winOrLose,getPoints());
        }catch (Exception e){
            gameOverListener.onGameOver(false,winOrLose,getPoints());
        }

    }

    private void saveGame() throws IOException{
        GameData gameData = new GameData(this);
        SysData.getInstance().addGame(gameData);
        GameDataCSVManager.writeGameDataListToCSV("GameHistory.csv");
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
            message = "Excellent! Mine neutralized.";
            this.gainPoints(pointsForFlaggingMine);
            parentBoard.reveal(tile);
            SoundManager.getInstance().playOnce(SoundManager.SoundId.REVEAL);
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
            message = "Flag removed.";
            parentBoard.unflag(tile);
            notifyListenersAfterAction(message,true,0,0);
            //  this.changeTurn();
            return;
        }

        //case tile is not flagged (flag it)
        if(!tile.isActivated()&&!tile.isFlagged()) {
            System.out.println("Flagging tile");
            message = "Mistake! False alarm.";
            parentBoard.flag(tile);
            SoundManager.getInstance().playOnce(SoundManager.SoundId.POINTS_LOSE);
            this.gainPoints(pointsForFlaggingNumber);
            notifyListenersAfterAction(message,false,0,-3);
    }
        else {
            message = "You already unflagged this tile!";
            parentBoard.flag(tile);
            notifyListenersAfterAction(message,false,0,0);
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
//                    if (this.isGameOver())    //this was moved to updateAfterSurprise
//                        initiateGameOver();
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
                SoundManager.getInstance().playOnce(SoundManager.SoundId.BLOCK);
                return false;
            }

            //case its a mine
            if(tile instanceof MineTile){
                System.out.println("Mine");
                message = "BOOM! You hit a mine! Lost 1 health";
                this.gainHealth(healthForRevealingMine);
                parentBoard.reveal(tile);
                SoundManager.getInstance().playOnce(SoundManager.SoundId.MINE);
                notifyListenersAfterAction(message,false,-1,0);
                if (this.isGameOver())
                    initiateGameOver();
                else
                    this.changeTurn();
            }
            if(tile instanceof NumberTile){
                int tilesRevealed = parentBoard.reveal(tile);
                this.gainPoints(pointsForRevealingNumber*tilesRevealed);
                SoundManager.getInstance().playOnce(SoundManager.SoundId.REVEAL);
                System.out.println("Its a number tile");
                message = "Number tiles revealed, gained "+(pointsForRevealingNumber*tilesRevealed)+" points";
                notifyListenersAfterAction(message,true,0,pointsForRevealingNumber*tilesRevealed);
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
    }

    private boolean activateSpecialTile(SpecialTile specialTile, Board parentBoard) {
        System.out.println("Activation cost: " + getGameDifficulty().getActivationCost() + "\tPoints: " + this.getPoints());
        if (this.getPoints() < getGameDifficulty().getActivationCost()){
            System.out.println("Not enough points to activate special tile");
            message = "Not enough points to activate special tile";
            SoundManager.getInstance().playOnce(SoundManager.SoundId.BLOCK);
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
                if (plusMinus > 0)
                    notifyListenersAfterAction("Activated Surprise Tile", false, 0, -getGameDifficulty().getActivationCost());
                String message = (resultOfRandom)? "Good surprise!" : "Bad surprise!";
                System.out.println(message);
                this.message = message+" Points changed by: "+(plusMinus*getGameDifficulty().getSurprisePoints())+", Health changed by: "+(plusMinus*getGameDifficulty().getSurpriseHealth());
                this.gainPoints(plusMinus*getGameDifficulty().getSurprisePoints());
                boolean healthMaxedOut = this.getHealthPool()==10;
                this.gainHealth(plusMinus*getGameDifficulty().getSurpriseHealth());
                SoundManager.getInstance().playOnce(resultOfRandom ? SoundManager.SoundId.POINTS_WIN : SoundManager.SoundId.POINTS_LOSE);
                if (!healthMaxedOut||!resultOfRandom)
                    notifySurpriseListeners(plusMinus*getGameDifficulty().getSurpriseHealth(),plusMinus*getGameDifficulty().getSurprisePoints());
                else
                {
                    this.message+= " (Health is already maxed out)";
                    notifySurpriseListeners(0,plusMinus*getGameDifficulty().getSurprisePoints());
                }

            }
            if (specialTile instanceof QuestionTile questionTile)
            {
                message = "Question tile activated and it costed you "+getGameDifficulty().getActivationCost()+" points";
                notifyListenersAfterAction(message,true,0,-getGameDifficulty().getActivationCost());
                System.out.println("Question tile activated");
                displayQuestionListener.displayQuestion(parentBoard); //tom i just added the arg here for the new displayQuestion method
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
    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;}

    private void notifyListenersAfterAction(String message, boolean positiveMove, int healthChange, int pointsChange)
    {
        for (ActionMadeListener listener : actionMadeListeners)
            listener.onActionMade(message,positiveMove,healthChange,pointsChange);
    }

    public void notifySurpriseListeners(int healthChange, int pointsChange)
    {
        for (SurpriseListener listener : surpriseListeners)
            listener.revealSurprise(healthChange, pointsChange);
    }

    public void updateAfterQuestionResult(QuestionDifficulty difficulty, boolean correctAnswer, Board parentBoard)
    {
        Random random = new Random();
        boolean randomResult = random.nextBoolean();
        String correctly = (correctAnswer)? "correctly:)" : "incorrectly:(";
        message = "You answered "+correctly;
        SoundManager.getInstance().playOnce(correctAnswer ? SoundManager.SoundId.POINTS_WIN : SoundManager.SoundId.POINTS_LOSE);
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

        if(isGameOver())
            initiateGameOver();

    }

    private void updateAfterQuestionResultHard(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult) {
        switch (difficulty) {
            case EASY:
                if (correctAnswer) {
                    this.gainPoints(10);
                    this.gainHealth(1);
                    notifyListenersAfterAction(message,true,1,10);
                } else {
                    this.gainPoints(-10);
                    this.gainHealth(-1);
                    notifyListenersAfterAction(message,false,-1,-10);
                }
                break;
            case MEDIUM:
                System.out.println("Randomer is "+randomResult);
                int healthChanged = (randomResult)? 2:1;
                if (correctAnswer)
                {
                    this.gainPoints(15);
                    this.gainHealth(healthChanged);
                    notifyListenersAfterAction(message,true,healthChanged,15);
                }
                else
                {
                    this.gainPoints(-15);
                    this.gainHealth(-healthChanged);
                    notifyListenersAfterAction(message,false,-healthChanged,-15);
                }
                break;
            case HARD:
                if (correctAnswer)
                {
                    this.gainPoints(20);
                    this.gainHealth(2);
                    notifyListenersAfterAction(message,true,2,20);
                }
                else
                {
                    this.gainPoints(-20);
                    this.gainHealth(-2);
                    notifyListenersAfterAction(message,false,-2,-20);
                }
                break;
            case MASTER:
                if (correctAnswer)
                {
                    this.gainPoints(40);
                    this.gainHealth(3);
                    notifyListenersAfterAction(message,true,3,40);
                }
                else
                {
                    this.gainPoints(-40);
                    this.gainHealth(-3);
                    notifyListenersAfterAction(message,false,-3,-40);
                }
        }
    }

    private void updateAfterQuestionResultMedium(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult) {
        switch (difficulty) {
            case EASY:
                if (correctAnswer) {
                    this.gainPoints(8);
                    this.gainHealth(1);
                    notifyListenersAfterAction(message,true,1,8);
                } else {
                    this.gainPoints(-8);
                    notifyListenersAfterAction(message,false,-1,-8);
                }
                break;
            case MEDIUM:
                if (correctAnswer)
                {
                    this.gainPoints(10);
                    this.gainHealth(1);
                    notifyListenersAfterAction(message,true,1,10);
                }
                else
                {
                    System.out.println("Randomer is "+randomResult);
                    if (!randomResult)
                    {
                        this.gainPoints(-10);
                        this.gainHealth(-1);
                        notifyListenersAfterAction(message,false,-1,-10);
                    }

                }
                break;
            case HARD:
                if (correctAnswer)
                {
                    this.gainPoints(15);
                    this.gainHealth(1);
                    notifyListenersAfterAction(message,true,1,15);
                }
                else
                {
                    this.gainPoints(-15);
                    this.gainHealth(-1);
                    notifyListenersAfterAction(message,false,-1,-15);
                }
                break;
            case MASTER:
                if (correctAnswer)
                {
                    this.gainPoints(20);
                    this.gainHealth(2);
                    notifyListenersAfterAction(message,true,2,20);
                }
                else
                {
                    this.gainPoints(-20);
                    System.out.println("Randomer is "+randomResult);
                    int healthLost = (randomResult)? -1:-2;
                    this.gainHealth(healthLost);
                    notifyListenersAfterAction(message,false,healthLost,-20);
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
                    notifyListenersAfterAction(message,true,1,3);
                }
                else
                {
                    System.out.println("Randomer is "+randomResult);
                    int pointsLost = (randomResult)? 0:-3;
                    this.gainPoints(pointsLost);
                    notifyListenersAfterAction(message,false,0,pointsLost);
                }
                break;
            case MEDIUM:
                if(correctAnswer)
                {
                    parentBoard.revealRandomMine();
                    message+=" (Also Revealed a mine!)";
                    this.gainPoints(6);
                    notifyListenersAfterAction(message,true,0,6);
                }
                else
                {
                    System.out.println("Randomer is "+randomResult);
                    int pointsLost = (randomResult)? 0:-6;
                    this.gainPoints(pointsLost);
                    notifyListenersAfterAction(message,false,0,pointsLost);
                }
                break;
            case HARD:
                if(correctAnswer)
                {
                    parentBoard.revealGrid();
                    message+=" (Also Revealed a grid in the board!)";
                    this.gainPoints(10);
                    notifyListenersAfterAction(message,true,0,10);
                }
                else
                {
                    this.gainPoints(-10);
                    notifyListenersAfterAction(message,false,0,-10);
                }
                break;
            case MASTER:
                if(correctAnswer)
                {
                    this.gainPoints(15);
                    this.gainHealth(2);
                    notifyListenersAfterAction(message,true,2,15);
                }
                else
                {
                    this.gainPoints(-15);
                    this.gainHealth(-1);
                    notifyListenersAfterAction(message,false,-1,-15);
                }
                break;
        }
    }


    public void setActionMadeListener(ActionMadeListener actionMadeListener) {
        this.actionMadeListeners.add(actionMadeListener);
    }


    public void setSurpriseListener(SurpriseListener surpriseListener) {
        this.surpriseListeners.add(surpriseListener);
    }

    public void updateAfterSurpriseRevealed(int healthChange, int pointsChange, boolean positiveMove)
    {
        if (isGameOver()) {
            initiateGameOver();
            return;
        }
        String message = (positiveMove)? "Good Surprise!" : "Bad Surprise!";
        notifyListenersAfterAction(message, positiveMove, healthChange,pointsChange);
    }

    //clear listeners to avoid memory leaks when playing more than 1 game per running instance
    public void clearListeners() {
        this.actionMadeListeners.clear();
        this.surpriseListeners.clear();
    }
}