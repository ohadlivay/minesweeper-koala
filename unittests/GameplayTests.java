import main.java.model.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GameplayTests {

    private GameSession gameSession;

    /*
     * SETUP: Resets the singleton instance before every test.
     * This ensures that one test changing health/points doesn't break the next test.
     */
    @Before
    public void setUp() throws Exception {
        gameSession = GameSession.getTestInstance();

        // Reset internal fields to default using Reflection
        Field pointsField = GameSession.class.getDeclaredField("points");
        pointsField.setAccessible(true);
        pointsField.setInt(gameSession, 0);

        Field healthField = GameSession.class.getDeclaredField("healthPool");
        healthField.setAccessible(true);
        healthField.setInt(gameSession, GameDifficulty.EASY.getInitialHealthPool()); // Default to Easy health

        gameSession.initializeBoards();
    }

    // ---------------------------------------------------------
    // YOUR ORIGINAL TESTS (Board Structure & Basic Logic)
    // ---------------------------------------------------------

    @Test
    public void boardRowsEasy() {
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        assertEquals(9, board.getCols());
    }

    @Test
    public void boardRowsMedium() {
        Board board = Board.createNewBoard(GameDifficulty.MEDIUM);
        assertEquals(13, board.getCols());
    }

    @Test
    public void boardRowsHard() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        assertEquals(16, board.getCols());
    }

    @Test
    public void boardRevealRevealedTile() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile tile = new Tile();
        tile.setIsRevealed(true);
        // Direct board usage doesn't trigger GameSession logic, so simple return 0 expected
        assertEquals(0, board.reveal(tile));
    }

    @Test
    public void boardFlagFlaggedTile() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile tile = new Tile();
        tile.setIsFlagged(true);
        assertEquals(0, board.reveal(tile));
    }

    @Test
    public void boardMineCountEasy() {
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        assertEquals(10, board.getMinesLeft());
    }

    @Test
    public void boardMineCountMedium() {
        Board board = Board.createNewBoard(GameDifficulty.MEDIUM);
        assertEquals(26, board.getMinesLeft());
    }

    @Test
    public void boardMineCountHard() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        assertEquals(44, board.getMinesLeft());
    }

    @Test
    public void boardMineCountReduces() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile mineTile = getMineTileInBoard(board);

        // Ensure we found a mine before trying to reveal it
        if (mineTile != null) {
            board.reveal(mineTile);
            // Hard difficulty starts with 44 mines, so 43 is the correct expectation
            assertEquals(43, board.getMinesLeft());
        }
    }

    @Test
    public void leftPlayerTurnFirst(){
        // The setUp() method already initializes this, but keeping your logic is fine
        GameSession gameSession = GameSession.getTestInstance();
        gameSession.initializeBoards();
        assertTrue(gameSession.getLeftBoard().getTurn());
    }

    @Test
    public void tileGridInit() {
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        board.reveal(new MineTile());
        assertEquals(9, board.getMinesLeft());
    }

    // ---------------------------------------------------------
    // NEW TESTS (Economy, Turns, Interaction Logic)
    // ---------------------------------------------------------

    @Test
    public void initialHealthIsCorrectForEasy() {
        // Checks that the GameSession initialized via setUp has correct health
        int expectedHealth = GameDifficulty.EASY.getInitialHealthPool();
        assertEquals(expectedHealth, gameSession.getHealthPool());
    }

    @Test
    public void pointsCannotGoBelowZero() {
        // Try to flag a tile (costs 3 points) while having 0 points
        Tile tile = getSafeTile(gameSession.getLeftBoard());

        // Pre-condition
        assertEquals(0, gameSession.getPoints());

        // Action: Flag (-3 points logic)
        gameSession.RightClickedTile(tile);

        // Post-condition: Should stay at 0, not -3
        assertEquals(0, gameSession.getPoints());
    }

    @Test
    public void rightClickFlaggingCostsPoints() throws Exception {
        // 1. Give the player some points first using Reflection
        Field pointsField = GameSession.class.getDeclaredField("points");
        pointsField.setAccessible(true);
        pointsField.setInt(gameSession, 10);

        // 2. Find a non-revealed tile
        Tile tile = getSafeTile(gameSession.getLeftBoard());

        // 3. Right click (Flag)
        gameSession.RightClickedTile(tile);

        // 4. Assert points decreased by 3
        assertEquals(7, gameSession.getPoints());
        assertTrue(tile.isFlagged());
    }

    @Test
    public void rightClickUnflaggingDoesNotRefundPoints() throws Exception {
        // 1. Give points
        Field pointsField = GameSession.class.getDeclaredField("points");
        pointsField.setAccessible(true);
        pointsField.setInt(gameSession, 10);

        Tile tile = getSafeTile(gameSession.getLeftBoard());

        // 2. Flag it (-3 points -> 7)
        gameSession.RightClickedTile(tile);
        assertEquals(7, gameSession.getPoints());

        // 3. Unflag it
        gameSession.RightClickedTile(tile);

        // 4. Assert points did not go back up
        assertEquals(7, gameSession.getPoints());
        assertFalse(tile.isFlagged());
    }

    @Test
    public void rightClickingMineRevealsItAndGainsPoint() {
        // Special rule: Flagging a mine reveals it and gives +1 point
        Tile mineTile = getMineTileInBoard(gameSession.getLeftBoard());
        int initialPoints = gameSession.getPoints();

        // Action: Right Click a Mine
        gameSession.RightClickedTile(mineTile);

        // Assertions
        assertTrue("Mine should be revealed", mineTile.isRevealed());
        assertEquals("Should gain 1 point", initialPoints + 1, gameSession.getPoints());
    }

    @Test
    public void leftClickingMineReducesHealth() {
        Tile mineTile = getMineTileInBoard(gameSession.getLeftBoard());
        int initialHealth = gameSession.getHealthPool();

        // Action: Reveal Mine
        gameSession.LeftClickedTile(mineTile);

        assertEquals("Health should decrease by 1", initialHealth - 1, gameSession.getHealthPool());
        assertTrue(mineTile.isRevealed());
    }

    @Test
    public void turnChangesAfterRevealingNumberTile() {
        // Left board starts
        assertTrue(gameSession.getLeftBoard().getTurn());

        // Find a NumberTile (not a mine)
        Tile safeTile = getSafeTile(gameSession.getLeftBoard());

        // Reveal it
        gameSession.LeftClickedTile(safeTile);

        // Assert Turn Switched
        assertFalse("Left board should lose turn", gameSession.getLeftBoard().getTurn());
        assertTrue("Right board should gain turn", gameSession.getRightBoard().getTurn());
    }

    @Test
    public void turnDoesNotChangeIfWrongPlayerClicks() {
        // Left board starts. Try to click on Right Board.
        Tile rightBoardTile = getSafeTile(gameSession.getRightBoard());

        gameSession.LeftClickedTile(rightBoardTile);

        // Turn should remain with Left
        assertTrue(gameSession.getLeftBoard().getTurn());
        assertFalse(rightBoardTile.isRevealed());
    }

    @Test
    public void rightClickingMineChangesTurn() {
        // As per code: "revealing a mine by flagging does change a turn!"
        Tile mineTile = getMineTileInBoard(gameSession.getLeftBoard());

        assertTrue(gameSession.getLeftBoard().getTurn());

        gameSession.RightClickedTile(mineTile);

        assertFalse("Turn should switch after flagging a mine", gameSession.getLeftBoard().getTurn());
    }

    /*
     * HELPER METHODS
     */

    private MineTile getMineTileInBoard(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof MineTile) {
                    return (MineTile) tile;
                }
            }
        }
        return null;
    }

    private Tile getSafeTile(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (!(tile instanceof MineTile) && !tile.isRevealed()) {
                    return tile;
                }
            }
        }
        return null;
    }
}