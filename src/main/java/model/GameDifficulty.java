package main.java.model;

// Enum representing different game difficulty levels
public enum GameDifficulty {
    //rows, cols, mineCount, questionCount, surpriseCount, activationCost, initialHealthPool, surprisePoints, surpriseHealth
    EASY(9, 9, 10,6,2, 5,10,8,1),
    MEDIUM(13, 13, 26,7,3, 8,8,12,1),
    HARD(16, 16, 44,11,4, 12,6,16,1);

    // Fields
    private final int rows;
    private final int cols;

    private final int mineCount;
    private final int questionCount;
    private final int surpriseCount;

    private final int activationCost;
    private final int initialHealthPool;

    private final int surprisePoints;
    private final int surpriseHealth;



    // Constructor
    private GameDifficulty(int rows, int cols, int mineCount,int questionCount, int surpriseCount, int activationCost, int initialHealthPool, int surprisePoints, int surpriseHealth) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.activationCost = activationCost;
        this.questionCount = questionCount;
        this.surpriseCount = surpriseCount;
        this.initialHealthPool = initialHealthPool;
        this.surprisePoints = surprisePoints;
        this.surpriseHealth = surpriseHealth;
    }

    // Getters
    public int getRows()      { return rows; }
    public int getCols()      { return cols; }
    public int getMineCount() { return mineCount; }
    public int getActivationCost() { return activationCost; }
    public int getQuestionCount() {return questionCount;}
    public int getSurpriseCount() {return surpriseCount;}
    public int getInitialHealthPool() { return initialHealthPool; }
    public int getSurprisePoints() {return surprisePoints;}
    public int getSurpriseHealth() {return surpriseHealth;}
}
