/* Asserted imports for this class:
model.Board
view.BoardLayout
 */

package main.java.controller;

import main.java.model.Board;
import main.java.model.Tile;

public class BoardsController {
    private static BoardsController boardsController; //static cuz shared across all instances
    private Board board;
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
