/* Asserted imports for this class:
model.Board
view.BoardLayout
 */

package main.java.controller;

import main.java.model.Board;
import main.java.model.GameDifficulty;
import main.java.model.Tile;

public class BoardsController {
    private static BoardsController boardsController; //static cuz shared across all instances
    private Board board1;
    private Board board2;
    private BoardsController(){
    }

    public static BoardsController getInstance() {
        if (BoardsController.boardsController == null) {
            BoardsController.boardsController = new BoardsController();
            return BoardsController.boardsController;
        }
        return BoardsController.boardsController;
    }

    //Ohad please implement the following methods
    //Done. im assuming your view has a board instance, need to check if this is ok
    public static Tile[][] getTiles(Board board) {     //will this method be here?
        return board.getTiles();
    }

    public int getRows(Board board) {
        return board.getRows();
    }

    public int getCols(Board board) {
        return board.getCols();
    }

    /* *******TEMPORARY METHOD*********
     * This is just to initialize two HARD boards for iteration 1
     * Later on will need to see where the boards are created with the passed difficulty
     */
    public void initBoards() {
        board1  = Board.createNewBoard(GameDifficulty.HARD);
        board2 = Board.createNewBoard(GameDifficulty.HARD);
    }
    public Board getBoard1() {
        return board1;
    }
    public Board getBoard2() {
        return board2;
    }

    public void tileRightClick(Tile tile) {
        if (tile.isFlagged()) {
            tile.unflag();
        }
        else {
            tile.flag();
        }
    }

    public void tileLeftClick(Tile tile) {
        tile.reveal();
    }


}
