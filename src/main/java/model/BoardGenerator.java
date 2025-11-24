package main.java.model;

import main.java.test.Testable;

import java.util.Arrays;
import java.util.Random;

public class BoardGenerator implements Testable {
    private int rows;
    private int cols;
    private int numMines;
    private int numQuestionTiles;
    private int numSurpriseTiles;

    public BoardGenerator(GameDifficulty gameDifficulty){
        rows = gameDifficulty.getRows();
        cols = gameDifficulty.getCols();
        numMines = gameDifficulty.getMineCount();
    }

    /*
     * Generates a valid board configuration.
     *
     * Repeatedly creates temporary grids using a seeded generator
     * until the grid contains enough candidate tiles to support
     * the required number of question and surprise tiles.
     *
     * Once a valid layout is found, it is converted into a
     * fully-initialized Tile[][] board.
     */
    public Tile[][] generateValidBoard(int seed) {
        int[][] grid;

        // Keep generating temporary boards until the minimum
        // candidate-tile requirement is satisfied
        do {
            grid = generateTempBoard(seed);
            seed += 1;
        } while (numCandidateTiles(grid) < (this.numQuestionTiles + this.numSurpriseTiles));

        // A valid blueprint has been found — convert it into Tiles

        return toTileGrid(grid);
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
    private int countMines(int[][] grid) {
        int total = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 1) total++;
            }
        }
        return total;
    }
    private Tile[][] toTileGrid(int[][] grid) { //privatize
        int rows = grid.length;
        int cols = grid[0].length;

        Tile[][] tiles = new Tile[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c] == 1) {
                    // mine tile
                    tiles[r][c] = new MineTile();
                } else {
                    // empty tile
                    tiles[r][c] = new Tile();
                }
            }
        }

        return tiles;
    }

    @Override
    public boolean runClassTests() {
        // simple deterministic test:
        int seed = 20;

        int[][] grid = generateTempBoard(seed);

        // 1. size sanity
        if (grid.length != rows) return false;
        if (grid[0].length != cols) return false;

        // 2. mine count sanity
        if (countMines(grid) != numMines) return false;

        // 3. test valid board generator
        Tile[][] tileGrid = generateValidBoard(seed);

        System.out.println("Sample valid board:");
        for (Tile[] row : tileGrid) {
            for (Tile tile : row) {
                System.out.print(tile.toString() + " ");
            }
            System.out.println();
        }


        return true;
    }
}