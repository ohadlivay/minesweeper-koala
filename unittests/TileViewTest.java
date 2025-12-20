import org.junit.Assert;
import org.junit.Test;
import main.java.model.Tile;


public class TileViewTest {

    // Check if the recolorTile and uncolorTile methods are called correctly
    // Test if the setTileTurn method calls the correct methods
    // RecolorTile should be called when the tile is turned on
    // UncolorTile should be called when the tile is turned off
    @Test
    public void setTileTurn_true_callsRecolorTile() {

        Tile dummyTile = new Tile();
        DummyTileView tileView = new DummyTileView(dummyTile, 40);

        tileView.setTileTurn(true);

        Assert.assertTrue(tileView.recolorCalled);
        Assert.assertFalse(tileView.uncolorCalled);
    }


    @Test
    public void setTileTurn_false_callsUncolorTile() {
        Tile dummyTile = new Tile();
        DummyTileView tileView = new DummyTileView(dummyTile, 40);

        tileView.setTileTurn(false);

        Assert.assertFalse(tileView.recolorCalled);
        Assert.assertTrue(tileView.uncolorCalled);
    }

}
