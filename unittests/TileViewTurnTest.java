import org.junit.Assert;
import org.junit.Test;
import main.java.model.Tile;


public class TileViewTurnTest {

    // Test if the setTileTurn method calls the correct methods:
    // Check if the recolorTile and uncolorTile methods are called correctly
    //  - RecolorTile should be called when the tile is turned on
    //  - UncolorTile should be called when the tile is turned off
    @Test
    public void setTileTurn_true_callsRecolorTile() {

        Tile dummyTile = new Tile();
        DummyTileView tileView = new DummyTileView(dummyTile);

        tileView.setTileTurn(true);

        // Assert that the recolorTile method was called
        Assert.assertTrue(tileView.recolorCalled);

        // Assert that the uncolorTile method was not called
        Assert.assertFalse(tileView.uncolorCalled);
    }


    @Test
    public void setTileTurn_false_callsUncolorTile() {
        Tile dummyTile = new Tile();
        DummyTileView tileView = new DummyTileView(dummyTile);

        tileView.setTileTurn(false);

        // Assert that the uncolorTile method was called
        Assert.assertFalse(tileView.recolorCalled);

        // Assert that the recolorTile method was not called
        Assert.assertTrue(tileView.uncolorCalled);
    }

}
