package main.java.controller;


/*waht does the list of listeners do here, is it listening to the model changes?
also i changed name from TileController to TilesController to imply singleton
 */

/*
TBD = TO BE DELETED
this class should not exist, we changed the way logic works.
need to delete it, just making sure we're on the same page.
30/11/25
please approve:
[V] ohad
[] tom
[] tali
[] liran
 */

public class TBD_TileController {
    private static TBD_TileController controller;

    private TBD_TileController(){
        TBD_TileController controller = new TBD_TileController();
    }

    public static TBD_TileController getInstance(){
        if (TBD_TileController.controller == null){
            TBD_TileController.controller = new TBD_TileController();
        }
        return TBD_TileController.controller;
    }
}
