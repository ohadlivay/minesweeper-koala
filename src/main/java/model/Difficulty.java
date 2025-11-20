package main.java.model;

/* this will hold all constants that are the deltas between each difficulty level;
   grid size, mine count etc.
   need to add more info like activation cost or penalties which are specific to difficulty.
*/
public enum Difficulty {
    EASY(9, 9, 10),
    MEDIUM(13, 13, 40),
    HARD(16, 16, 99);

    //even though rows=cols in all current  cases, we want to be prepared to support a feature where this is not the case.
    private final int rows;
    private final int cols;

    private final int mineCount;

    Difficulty(int rows, int cols, int mineCount) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
    }

    public int getRows()      { return rows; }
    public int getCols()      { return cols; }
    public int getMineCount() { return mineCount; }
}
