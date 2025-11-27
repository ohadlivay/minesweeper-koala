package main.java.controller;

import main.java.view.TileListener;
import java.util.ArrayList;


/*waht does the list of listeners do here, is it listening to the model changes?
also i changed name from TileController to TilesController to imply singleton
 */

public class TileController implements TileListener{
    private ArrayList<TileListener> tileListeners = new ArrayList<>();
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

    @Override
    public void update() {

    }
}
