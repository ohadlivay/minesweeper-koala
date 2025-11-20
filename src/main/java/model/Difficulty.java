package main.java.model;
/* this will hold all constants that are the deltas between each difficulty level; grid size, mine count etc.
need to add more info like activation cost or penalties which are specific to difficulty.
 */
public enum Difficulty {
    EASY(9,10),
    MEDIUM(13, 40),
    HARD(16, 99);

    private final int gridSize;
    private final int mineCount;

    Difficulty(int gridSize, int mineCount) {
        this.gridSize = rows;
        this.mineCount = mineCount;
    }

    public int getGridSize()  { return gridSize; }
    public int getMineCount() { return mineCount; }
}

