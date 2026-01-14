package main.java.model;

import main.java.util.GameDataCSVManager;
import main.java.util.SoundManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

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


    // Game session attributes
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

    //Singleton pattern
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

    // Initialize game stats
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

    // Initialize the game session
    public void initGame()
    {
        System.out.println("TTT");
        SysData sysData = SysData.getInstance();
        int availableQuestions = sysData.getQuestions().size();

        // Get the requirement directly from the current difficulty
        GameDifficulty difficulty = this.getGameDifficulty();
        int required = difficulty.getQuestionCount() * 2;

        // Check if there are enough questions for the current difficulty
        if (availableQuestions < required) {
            int missing = required - availableQuestions;
            System.out.println("DDD");

            throw new IllegalStateException(String.format(
                    "Not enough questions for %s mode. Current: %d, Required: %d. Please add %d more questions.",
                    difficulty.name(), availableQuestions, required, missing
            ));
        }

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

    public void forceGameOver() {
        this.setHealthPool(0);
        initiateGameOver();
    }

    //Methods for the gameplay
    private boolean isGameOver()
    {
        return getHealthPool() <= 0||getLeftBoard().allMinesRevealed() || getRightBoard().allMinesRevealed();
    }

    // to prevent multiple calls to game over processing
    private boolean isGameOverProcessing = false;
    // handles all game over logic
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
            saveGame(winOrLose);
            gameOverListener.onGameOver(true,winOrLose,getPoints());
        }catch (Exception e){
            gameOverListener.onGameOver(false,winOrLose,getPoints());
        }

    }

    // saves the game data to the CSV file
    private void saveGame(boolean winOrLose) throws IOException{
        GameData gameData = new GameData(this.timeStamp, this.leftPlayerName, this.rightPlayerName, this.gameDifficulty, this.points, winOrLose);
        SysData.getInstance().addGame(gameData);
        GameDataCSVManager.writeGameDataListToCSV("GameHistory.csv");
    }

    // handles right click on tile logic
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
            notifyListenersAfterAction("This is not your turn!",false,0,0);
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

    // handles left click on tile logic
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
            notifyListenersAfterAction("This is not your turn!",false,0,0);
            return false; //not his turn
        }

        //case tile was already revealed
        if(tile.isRevealed()) {
            if (tile instanceof SpecialTile specialTile)
            {
                if (!specialTile.isUsed())
                {
                    this.activateSpecialTile(specialTile,parentBoard);
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

    // helper methods for gaining points and health
    private void gainPoints(int points){
        System.out.println("Points 'added': "+points);
        this.setPoints(this.getPoints() + points);
    }
    public void setPoints(int i) {
        this.points = i;
        if (this.getPoints()<0) this.points = 0;
    }

    // helper methods for gaining health
    private void gainHealth(int health) {
        System.out.println("Health 'added': "+health);
        this.setHealthPool(this.getHealthPool() + health);
    }

    // health cannot go below 0 or above max health pool
    private void setHealthPool(int i) {
        this.healthPool = i;
        if (this.getHealthPool()<0) this.healthPool = 0;
        if (this.getHealthPool()>MAX_HEALTH_POOL) {
            this.gainPoints((i-MAX_HEALTH_POOL)*getGameDifficulty().getActivationCost());
            this.healthPool = MAX_HEALTH_POOL;
        }
    }

    // handles special tile activation logic
    private boolean activateSpecialTile(SpecialTile specialTile, Board parentBoard) {
        System.out.println("Activation cost: " + getGameDifficulty().getActivationCost() + "\tPoints: " + this.getPoints());
        // case activation cost is greater than points
        if (this.getPoints() < getGameDifficulty().getActivationCost()){
            System.out.println("Not enough points to activate special tile");
            message = "Not enough points to activate special tile";
            SoundManager.getInstance().playOnce(SoundManager.SoundId.BLOCK);
            notifyListenersAfterAction(message,false,0,0);
            return false;
        }
        else {
            // case activation cost is less than or equal to points
            this.gainPoints(-getGameDifficulty().getActivationCost());
            System.out.println("Points after activation cost: "+this.getPoints());
            // case special tile is a surprise tile
            if (specialTile instanceof SurpriseTile surpriseTile)
            {
                System.out.println("Surprise tile activated");
                Random random = new Random();
                boolean resultOfRandom = random.nextBoolean();
                int plusMinus  = (resultOfRandom) ? 1 : -1;
                int rewardPoints = plusMinus * getGameDifficulty().getSurprisePoints();
                int rewardHealth = plusMinus * getGameDifficulty().getSurpriseHealth();

                int startPoints = getPoints();
                int startHealth = getHealthPool();

                this.gainPoints(rewardPoints);
                this.gainHealth(rewardHealth);

                int pointsAfter = getPoints() - startPoints;
                int healthAfter = getHealthPool() - startHealth;

                String baseMessage = (resultOfRandom)? "Good surprise!" : "Bad surprise!";

                if (resultOfRandom) {
                    String extraDetail = extraHpString(startHealth, rewardHealth);
                    this.message = baseMessage + " +" + rewardPoints + " pts." + extraDetail;
                } else {
                    this.message = baseMessage + " " + rewardHealth + " HP, " + rewardPoints + " pts.";
                }

                notifySurpriseListeners(healthAfter, pointsAfter);
            }
            // case special tile is a question tile
            if (specialTile instanceof QuestionTile questionTile)
            {
                message = "Question tile activated and it costed you "+getGameDifficulty().getActivationCost()+" points";
                notifyListenersAfterAction(message,true,0,-getGameDifficulty().getActivationCost());
                System.out.println("Question tile activated");
                displayQuestionListener.displayQuestion(parentBoard); //tom i just added the arg here for the new displayQuestion method
            }
            specialTile.setUsed();
            System.out.println("Points: "+" "+this.getPoints()+"    Health: "+this.getHealthPool()+"\n");
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
        String correctly = (correctAnswer)? "Correct!" : "Incorrect!";
        message = correctly;
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
        //orginal values before the changes
        int startPoints = getPoints();
        int startHealth = getHealthPool();
        switch (difficulty) {
            case EASY:
                if (correctAnswer) {
                    message += extraHpString(this.healthPool, 1);
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
                    message += extraHpString(this.healthPool, healthChanged);
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
                    message += extraHpString(this.healthPool, 2);
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
                    message += extraHpString(this.healthPool, 3);
                    this.gainPoints(40);
                    this.gainHealth(3);
                }
                else
                {
                    this.gainPoints(-40);
                    this.gainHealth(-3);
                }
        }
        // calculate the change in health and points
        int pointsAfter = getPoints() - startPoints;
        int healthAfter = getHealthPool() - startHealth;

        if (!correctAnswer && pointsAfter == 0) {
            message = "Incorrect! But you were lucky this time...";
        } else {
            String sign;
            if (pointsAfter > 0) sign = "+";
            else if (pointsAfter < 0) sign = "";
            else sign = correctAnswer ? "+" : "-";
            message += " (" + sign + pointsAfter + " pts)";
        }

        notifyListenersAfterAction(message, correctAnswer, healthAfter, pointsAfter);
    }

    private void updateAfterQuestionResultMedium(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult) {
        //orginal values before the changes
        int startPoints = getPoints();
        int startHealth = getHealthPool();
        switch (difficulty) {
            case EASY:
                if (correctAnswer) {
                    message += extraHpString(this.healthPool, 1);
                    this.gainPoints(8);
                    this.gainHealth(1);
                } else {
                    this.gainPoints(-8);
                }
                break;
            case MEDIUM:
                if (correctAnswer)
                {
                    message += extraHpString(this.healthPool, 1);
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
                    message += extraHpString(this.healthPool, 1);
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
                    message += extraHpString(this.healthPool, 2);
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
        //calculate the change in health and points
        int pointsAfter = getPoints() - startPoints;
        int healthAfter = getHealthPool() - startHealth;

        if (!correctAnswer && pointsAfter == 0) {
            message = "Incorrect! But you were lucky this time...";
        } else {
            String sign;
            if (pointsAfter > 0) sign = "+";
            else if (pointsAfter < 0) sign = "";
            else sign = correctAnswer ? "+" : "-";
            message += " (" + sign + pointsAfter + " pts)";
        }

        notifyListenersAfterAction(message, correctAnswer, healthAfter, pointsAfter);
    }

    private void updateAfterQuestionResultEasy(QuestionDifficulty difficulty, boolean correctAnswer, boolean randomResult, Board parentBoard)
    {
        //orginal values before the changes
        int startPoints = getPoints();
        int startHealth = getHealthPool();
        switch(difficulty){
            case EASY:
                if(correctAnswer)
                {
                    message += extraHpString(this.healthPool, 1);
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
                    message+=" (Also Revealed a mine!)";
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
                    message+=" (Revealed a grid!)";
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
                    message += extraHpString(this.healthPool, 2);
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
        //calculate the change in health and points
        int pointsAfter = getPoints() - startPoints;
        int healthAfter = getHealthPool() - startHealth;

        if (!correctAnswer && pointsAfter == 0) {
            message = "Incorrect! But you were lucky this time...";
        } else {
            String sign;
            if (pointsAfter > 0) sign = "+";
            else if (pointsAfter < 0) sign = "";
            else sign = correctAnswer ? "+" : "-";
            message += " (" + sign + pointsAfter + " pts)";
        }

        notifyListenersAfterAction(message, correctAnswer, healthAfter, pointsAfter);

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
        if (this.message == null || this.message.isEmpty()) {
            this.message = (positiveMove) ? "Good Surprise!" : "Bad Surprise!";
        }
        notifyListenersAfterAction(message, positiveMove, healthChange,pointsChange);
        this.message = "";
    }

    //clear listeners to avoid memory leaks when playing more than 1 game per running instance
    public void clearListeners() {
        this.actionMadeListeners.clear();
        this.surpriseListeners.clear();
    }

    //create a string specifically for when the user gains extra points from extra hp
    private String extraHpString(int currentHealth, int healthReward) {
        int potentialHealth = currentHealth + healthReward;
        int activationCost = getGameDifficulty().getActivationCost();

        //check if the reward pushes the player beyond the max health pool
        if (potentialHealth > MAX_HEALTH_POOL) {
            int overflowAmount = potentialHealth - MAX_HEALTH_POOL;
            int pointsFromHealth = overflowAmount * activationCost;
            int actualHpGain = MAX_HEALTH_POOL - currentHealth;

            if (actualHpGain > 0) {
                return " (Gained " + actualHpGain + " HP + " + pointsFromHealth + " pts from " + overflowAmount + " extra HP)";
            }
            else
            {
                return " (Max HP! +" + pointsFromHealth + " pts from " + overflowAmount + " extra HP)";
            }
        }
        return healthReward > 0 ? " (Gained " + healthReward + " HP)" : "";
    }
}