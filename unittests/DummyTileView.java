import main.java.model.Tile;
import main.java.view.TileView;

import java.awt.*;

// Dummy TileView class for testing purposes
public class DummyTileView extends TileView{
    boolean recolorCalled = false;
    boolean uncolorCalled = false;

    public DummyTileView(Tile tile) {
        super(tile, 40, Color.black); // Using arbitrary size and color for testing
    }

    // overrides to make methods testable
    @Override
    protected void recolorTile() {
        recolorCalled = true;
    }

    @Override
    protected void uncolorTile() {
        uncolorCalled = true;
    }
}
