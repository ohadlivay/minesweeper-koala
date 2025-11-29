package main.java.controller;


/*waht does the list of listeners do here, is it listening to the model changes?
also i changed name from TileController to TilesController to imply singleton
 */

public class TileController{
    private static TileController controller;

    private TileController(){
        TileController controller = new TileController();
    }

    public static TileController getInstance(){
        if (TileController.controller == null){
            TileController.controller = new TileController();
        }
        return TileController.controller;
    }
}
