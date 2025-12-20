import main.java.model.Tile;
import main.java.view.TileView;

import java.awt.*;

public class DummyTileView extends TileView{
    boolean recolorCalled = false;
    boolean uncolorCalled = false;

    public DummyTileView(Tile tile, int dynamicSize) {
        super(tile, dynamicSize, Color.black);
    }

    @Override
    protected void recolorTile() {
        recolorCalled = true;
    }

    @Override
    protected void uncolorTile() {
        uncolorCalled = true;
    }
}
