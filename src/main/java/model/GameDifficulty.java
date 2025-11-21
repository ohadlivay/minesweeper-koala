package main.java.model;

/* this will hold all constants that are the deltas between each difficulty level;
   grid size, mine count etc.
   need to add more info like activation cost or penalties which are specific to difficulty.
*/
public enum GameDifficulty {
    EASY(9, 9, 10, 5),
    MEDIUM(13, 13, 40, 8),
    HARD(16, 16, 99, 12);

    //even though rows=cols in all current  cases, we want to be prepared to support a feature where this is not the case.
    private final int rows;
    private final int cols;

    private final int mineCount;

    private final int activationCost;

    GameDifficulty(int rows, int cols, int mineCount, int activationCost) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.activationCost = activationCost;
    }

    public int getRows()      { return rows; }
    public int getCols()      { return cols; }
    public int getMineCount() { return mineCount; }
    public int getActivationCost() { return activationCost; }
}
