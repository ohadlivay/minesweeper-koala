package main.java.model;

import java.util.Random;

public class BoardGenerator {
    private int rows;
    private int cols;
    private int numMines;
    private int numQuestionTiles;
    private int numSurpriseTiles;
    private static BoardGenerator instance;

    private BoardGenerator(GameDifficulty gameDifficulty){
        rows = gameDifficulty.getRows();
        cols = gameDifficulty.getCols();
        numMines = gameDifficulty.getMineCount();
    }

    public static BoardGenerator getInstance(GameDifficulty gameDifficulty){

        if (instance == null) {
            instance = new BoardGenerator(gameDifficulty);
        }
        // If you want a strict singleton per configuration, you could add checks here.
        return instance;
    }
/*
    public Tile[][] generateValidBoard(int seed){

 */
    public void generateValidBoard(int seed){
        boolean validBoardCreated = false;
        while(!validBoardCreated){
            int[][] grid = generateTempBoard(seed);
            int numCandidates = numCandidateTiles(grid);
            if (numCandidates >= (this.numQuestionTiles + this.numSurpriseTiles)){
                validBoardCreated = true;
            }
        }
        // at this point, we know grid is a valid blueprint for generating a legal board.

    }

    private int[][] generateTempBoard(int seed) {
        // create gridSize x gridSize grid filled with 0's
        int[][] tempTiles = new int[rows][cols];

        Random rng = new Random(seed);

        int placed = 0;
        while (placed < numMines) {
            int r = rng.nextInt(rows); // 0 .. gridSize-1
            int c = rng.nextInt(cols); // 0 .. gridSize-1

            // only place a mine if the cell is still 0
            if (tempTiles[r][c] == 0) {
                tempTiles[r][c] = 1;
                placed++;
            }
        }

        return tempTiles;
    }
    private int numCandidateTiles(int[][] grid) {
        int n = grid.length;
        if (n == 0) return 0;
        int m = grid[0].length;

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                // only interested in 0-cells
                if (grid[i][j] != 0) continue;

                boolean hasNeighborOne = false;

                // check all 8 neighbors (and skip (0,0) itself)
                for (int di = -1; di <= 1 && !hasNeighborOne; di++) {
                    for (int dj = -1; dj <= 1 && !hasNeighborOne; dj++) {

                        if (di == 0 && dj == 0) continue; // skip self

                        int ni = i + di;
                        int nj = j + dj;

                        // bounds check
                        if (ni < 0 || ni >= n || nj < 0 || nj >= m) {
                            continue;
                        }

                        if (grid[ni][nj] == 1) {
                            hasNeighborOne = true;
                        }
                    }
                }

                if (!hasNeighborOne) {
                    count++;
                }
            }
        }

        return count;
    }

}
