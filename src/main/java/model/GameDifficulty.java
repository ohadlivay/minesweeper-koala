package main.java.model;

/* this will hold all constants that are the deltas between each difficulty level;
   grid size, mine count etc.
   need to add more info like activation cost or penalties which are specific to difficulty.
*/
public enum GameDifficulty {
    EASY(9, 9, 10,6,2, 5,10),
    MEDIUM(13, 13, 26,7,3, 8,8),
    HARD(16, 16, 44,11,4, 12,6);

    //even though rows=cols in all current  cases, we want to be prepared to support a feature where this is not the case.
    private final int rows;
    private final int cols;

    private final int mineCount;
    private final int questionCount;
    private final int surpriseCount;

    private final int activationCost;
    private final int initialHealthPool;
<<<<<<< Updated upstream
//
=======


    //

>>>>>>> Stashed changes
    private GameDifficulty(int rows, int cols, int mineCount,int questionCount, int surpriseCount, int activationCost, int initialHealthPool) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.activationCost = activationCost;
        this.questionCount = questionCount;
        this.surpriseCount = surpriseCount;
        this.initialHealthPool = initialHealthPool;
    }

    public int getRows()      { return rows; }
    public int getCols()      { return cols; }
    public int getMineCount() { return mineCount; }
    public int getActivationCost() { return activationCost; }
    public int getQuestionCount() {return questionCount;}
    public int getSurpriseCount() {return surpriseCount;}
    public int getInitialHealthPool() { return initialHealthPool; }
}
