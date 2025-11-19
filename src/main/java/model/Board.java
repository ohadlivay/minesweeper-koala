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

    private Board(){
        this.PK = RANDOM.nextInt(99999999);

        this.tiles = new Tile[10][10];
    }


}
