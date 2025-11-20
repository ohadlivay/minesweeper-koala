package main.java.model;

public class BoardGenerator {
    private int gridSize;
    private int numMines;
    private int numQuestionTiles;
    private int numSurpriseTiles;
    private int seed; //seed based randomization for repeatability and testing.

    BoardGenerator(Difficulty difficulty){
        gridSize = difficulty.getGridSize();
        numMines = difficulty.getMineCount();
        seed = 42; //for now
    }
}
