package main.java.model;

import main.java.test.Testable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BoardGenerator implements Testable {
    private int rows;
    private int cols;
    private int numMines;
    private int numQuestionTiles;
    private int numSurpriseTiles;

    // String Constants for Grid Values
    private static final String MINE_STR = "MINE";
    private static final String QUESTION_STR = "QUESTION";
    private static final String SURPRISE_STR = "SURPRISE";
    private static final String EMPTY_STR = "0";

    public BoardGenerator(GameDifficulty gameDifficulty){
        rows = gameDifficulty.getRows();
        cols = gameDifficulty.getCols();
        numMines = gameDifficulty.getMineCount();
        numQuestionTiles = gameDifficulty.getQuestionCount();
        numSurpriseTiles = gameDifficulty.getSurpriseCount();
    }

    /*
     * Generates a valid board configuration.
     */
    public Tile[][] generateValidBoard(int seed) {
        String[][] grid;

        // Keep generating temporary boards until the minimum
        // candidate-tile requirement is satisfied
        do {
            grid = generateTempBoard(seed);
            seed += 1;
        } while (numCandidateTiles(grid) < (this.numQuestionTiles + this.numSurpriseTiles));

        // A valid blueprint has been found — convert it into Tiles

        // 1. Calculate adjacent mines and update cell values ("0" to "8")
        calculateAdjacentMines(grid);

        // 2. Distribute special tiles ("QUESTION" or "SURPRISE")
        distributeSpecialTiles(grid);

        // 3. Convert the final blueprint into Tiles
        return toTileGrid(grid);
    }

    private String[][] generateTempBoard(int seed) {
        // create grid filled with "0"s
        String[][] tempTiles = new String[rows][cols];

        // Initialize grid with "0" because String arrays default to null
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tempTiles[i][j] = EMPTY_STR;
            }
        }

        Random rng = new Random(seed);

        int placed = 0;
        while (placed < numMines) {
            int r = rng.nextInt(rows);
            int c = rng.nextInt(cols);

            // only place a mine if the cell is still "0"
            if (tempTiles[r][c].equals(EMPTY_STR)) {
                tempTiles[r][c] = MINE_STR;
                placed++;
            }
        }

        return tempTiles;
    }

    private int numCandidateTiles(String[][] grid) {
        int n = grid.length;
        if (n == 0) return 0;
        int m = grid[0].length;

        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                // only interested in "0"-cells (empty spots)
                if (!grid[i][j].equals(EMPTY_STR)) continue;

                boolean hasNeighborMine = false;

                // check all 8 neighbors
                for (int di = -1; di <= 1 && !hasNeighborMine; di++) {
                    for (int dj = -1; dj <= 1 && !hasNeighborMine; dj++) {

                        if (di == 0 && dj == 0) continue; // skip self

                        int ni = i + di;
                        int nj = j + dj;

                        // bounds check
                        if (ni < 0 || ni >= n || nj < 0 || nj >= m) {
                            continue;
                        }

                        if (grid[ni][nj].equals(MINE_STR)) {
                            hasNeighborMine = true;
                        }
                    }
                }

                if (!hasNeighborMine) {
                    count++;
                }
            }
        }

        return count;
    }

    private int countMines(String[][] grid) {
        int total = 0;
        for (String[] row : grid) {
            for (String cell : row) {
                if (cell.equals(MINE_STR)) total++;
            }
        }
        return total;
    }

    private void calculateAdjacentMines(String[][] grid) {
        int n = grid.length;
        if (n == 0) return;
        int m = grid[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (!grid[i][j].equals(MINE_STR)) {
                    int mineCount = 0;

                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (di == 0 && dj == 0) continue;

                            int ni = i + di;
                            int nj = j + dj;

                            if (ni >= 0 && ni < n && nj >= 0 && nj < m) {
                                if (grid[ni][nj].equals(MINE_STR)) {
                                    mineCount++;
                                }
                            }
                        }
                    }
                    // Store the count as a String ("0", "1", etc.)
                    grid[i][j] = String.valueOf(mineCount);
                }
            }
        }
    }

    /*
     * Distributes question and surprise tiles onto candidate cells (cells with 0 adjacent mines).
     */
    private void distributeSpecialTiles(String[][] grid) {
        int n = grid.length;
        if (n == 0) return;
        int m = grid[0].length;

        java.util.Random rng = new java.util.Random();

        int placedQuestion = 0;
        int placedSurprise = 0;

        // Collect all coordinates of candidate tiles (grid value is "0")
        List<int[]> candidates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Candidates are cells that have 0 adjacent mines
                if (grid[i][j].equals(EMPTY_STR)) {
                    candidates.add(new int[]{i, j});
                }
            }
        }

        Collections.shuffle(candidates, rng);

        // --- Distribution Phase ---

        // 1. Distribute Question Tiles
        int requiredQuestion = this.numQuestionTiles;
        for (int[] coord : candidates) {
            if (placedQuestion < requiredQuestion) {
                grid[coord[0]][coord[1]] = QUESTION_STR;
                placedQuestion++;
            } else {
                break;
            }
        }

        // 2. Distribute Surprise Tiles
        int requiredSurprise = this.numSurpriseTiles;
        for (int[] coord : candidates) {
            // Only consider cells that were NOT just set to Question Tile
            if (grid[coord[0]][coord[1]].equals(EMPTY_STR)) {
                if (placedSurprise < requiredSurprise) {
                    grid[coord[0]][coord[1]] = SURPRISE_STR;
                    placedSurprise++;
                } else {
                    break;
                }
            }
        }
    }

    private Tile[][] toTileGrid(String[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        Tile[][] tiles = new Tile[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // The logic of "How do I turn 'M' into a MineTile?"
                // is now completely hidden inside the Factory.
                tiles[r][c] = TileFactory.createTile(grid[r][c]);
            }
        }
        return tiles;
    }

    @Override
    public boolean runClassTests() {
        int seed = 20;

        // Helper test method logic would need to access private methods via reflection
        // or be internal. Assuming for this context we just test logic flow.
        String[][] grid = generateTempBoard(seed);

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