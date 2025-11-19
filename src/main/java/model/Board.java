/* Asserted imports for this class:
java.util.Random
 */

package main.java.model;
import java.util.Random;

public class Board {

    private final int PK;
    private int minesLeft;
    private Tile[][] tiles;
    private static final Random RANDOM = new Random();

    Board(){
        this.PK = RANDOM.nextInt(99999999);
        this.tiles = new Tile[10][10];
    }

    void populateBoard(int gridSize, int numMines){
        //need GameSession to send me the gridSize and number of mines


        /*
        this create a temporary, low weight and high performance board generator.
        since some generations can be faulty, this keeps that process light weight before we actually make the board.
         */
/*
        int[][] tempTiles = new int[gridSize][gridSize];

        int placed = 0 ;
        while (placed < numMines) {
            int r = RANDOM.nextInt(gridSize);
            int c = RANDOM.nextInt(gridSize);

            if (tempTiles[r][c] == 0) {   // only place if empty
                tempTiles[r][c] = 1;
                placed++;
            }
        }
        int countNoMineNeighborTiles = 0;
        for (int i = 0; i < gridSize; i++){
            for (int j = 0; j < gridSize; j++){
                if(tempTiles[i][j] == 1);
                    continue;

                if (!hasMineNeighbors(tempTiles, i, j)){
                    countNoMineNeighborTiles++;
                }
            }
        }

        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                 tiles[i][j] = new Tile(i, j,false, false);
            }
        }

 */
    }


}
