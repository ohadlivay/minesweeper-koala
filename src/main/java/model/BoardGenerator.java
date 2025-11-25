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

        // 1. Calculate adjacent mines and update cell values (0 to 8)
        calculateAdjacentMines(grid);

        // 2. Distribute special tiles (0 to 9 or 10)
        distributeSpecialTiles(grid);

        // 3. Convert the final blueprint into Tiles
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
                tempTiles[r][c] = 100; // MINE VALUE IS NOW 100
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

                        if (grid[ni][nj] == 100) {
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
                if (cell == 100) total++;
            }
        }
        return total;
    }

    private void calculateAdjacentMines(int[][] grid) {
        int n = grid.length;
        if (n == 0) return;
        int m = grid[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (grid[i][j] != 100) {
                    int mineCount = 0;

                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (di == 0 && dj == 0) continue;

                            int ni = i + di;
                            int nj = j + dj;

                            if (ni >= 0 && ni < n && nj >= 0 && nj < m) {
                                if (grid[ni][nj] == 100) { // Check for 100
                                    mineCount++;
                                }
                            }
                        }
                    }

                    grid[i][j] = mineCount; // Set value to 0-8
                }
            }
        }
    }

    /*
     * Distributes question and surprise tiles onto candidate cells (cells with 0 adjacent mines, value 0).
     * Updates the grid value: 9 for QuestionTile, 10 for SurpriseTile.
     */
    private void distributeSpecialTiles(int[][] grid) {
        int n = grid.length;
        if (n == 0) return;
        int m = grid[0].length;

        // Using a non-seeded Random ensures tile placement is unpredictable on each run
        // once a valid mine layout is found.
        java.util.Random rng = new java.util.Random();

        int placedQuestion = 0;
        int placedSurprise = 0;

        // Collect all coordinates of candidate tiles (grid value == 0)
        java.util.List<int[]> candidates = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Candidates are cells that have 0 adjacent mines (value 0)
                if (grid[i][j] == 0) {
                    candidates.add(new int[]{i, j});
                }
            }
        }

        // Shuffle the candidates list to ensure random selection of the
        // 0-neighbor tiles for special placement.
        java.util.Collections.shuffle(candidates, rng);

        // --- Distribution Phase ---

        // 1. Distribute Question Tiles (value 9)
        int requiredQuestion = this.numQuestionTiles;
        for (int[] coord : candidates) {
            if (placedQuestion < requiredQuestion) {
                grid[coord[0]][coord[1]] = 9; // 9 = Question Tile
                placedQuestion++;
            } else {
                break;
            }
        }

        // 2. Distribute Surprise Tiles (value 10)
        int requiredSurprise = this.numSurpriseTiles;
        // Continue iterating over the remaining candidates slots.
        for (int[] coord : candidates) {
            // Only consider cells that were NOT just set to 9 (Question Tile)
            if (grid[coord[0]][coord[1]] == 0) {
                if (placedSurprise < requiredSurprise) {
                    grid[coord[0]][coord[1]] = 10; // 10 = Surprise Tile
                    placedSurprise++;
                } else {
                    break;
                }
            }
        }
    }

    private Tile[][] toTileGrid(int[][] grid) { //privatize
        int rows = grid.length;
        int cols = grid[0].length;

        Tile[][] tiles = new Tile[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                int cellValue = grid[r][c];

                if (cellValue == 100) {
                    // Mine tile (value 100)
                    tiles[r][c] = new MineTile();
                } else if (cellValue == 9) {
                    // Question tile (value 9)
                    tiles[r][c] = new QuestionTile();
                } else if (cellValue == 10) {
                    // Surprise tile (value 10)
                    tiles[r][c] = new SurpriseTile();
                } else {
                    // Number tile (values 0 through 8)
                    NumberTile numberTile = new NumberTile();
                    numberTile.setAdjacentMines(cellValue);
                    tiles[r][c] = numberTile;
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