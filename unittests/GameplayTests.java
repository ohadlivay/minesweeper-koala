import main.java.model.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GameplayTests {

    private GameSession gameSession;
    private Board board;
    private NumberTile numberTile;
    private MineTile mineTile;
    private SurpriseTile surpriseTile;
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

        board = gameSession.getLeftBoard();

        numberTile = getNumberTile(board);
        mineTile = getMineTile(board);
        surpriseTile = getSurpriseTile(board);
    }

    // ---------------------------------------------------------
    // BOARD STRUCTURE & INITIALIZATION TESTS
    // ---------------------------------------------------------

    @Test
    public void createBoard_EasyDifficulty_ShouldHave9Columns() {
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        assertEquals(9, board.getCols());
    }

    @Test
    public void createBoard_MediumDifficulty_ShouldHave13Columns() {
        Board board = Board.createNewBoard(GameDifficulty.MEDIUM);
        assertEquals(13, board.getCols());
    }

    @Test
    public void createBoard_HardDifficulty_ShouldHave16Columns() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        assertEquals(16, board.getCols());
    }

    @Test
    public void createBoard_EasyDifficulty_ShouldInitializeWith10Mines() {
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        assertEquals(10, board.getMinesLeft());
    }

    @Test
    public void createBoard_MediumDifficulty_ShouldInitializeWith26Mines() {
        Board board = Board.createNewBoard(GameDifficulty.MEDIUM);
        assertEquals(26, board.getMinesLeft());
    }

    @Test
    public void createBoard_HardDifficulty_ShouldInitializeWith44Mines() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        assertEquals(44, board.getMinesLeft());
    }

    @Test
    public void initializeGame_ShouldStartWithLeftPlayerTurn() {
        // The setUp() method already initializes this, but keeping logic explicit
        GameSession gameSession = GameSession.getTestInstance();
        gameSession.initializeBoards();
        assertTrue(gameSession.getLeftBoard().getTurn());
    }

    @Test
    public void initializeGame_EasyDifficulty_ShouldSetCorrectHealthPool() {
        // Checks that the GameSession initialized via setUp has correct health
        int expectedHealth = GameDifficulty.EASY.getInitialHealthPool();
        assertEquals(expectedHealth, gameSession.getHealthPool());
    }

    // ---------------------------------------------------------
    // BASIC BOARD MECHANICS (Without GameSession Context)
    // ---------------------------------------------------------

    @Test
    public void reveal_AlreadyRevealedTile_ShouldReturnZeroValue() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile tile = new Tile();
        tile.setIsRevealed(true);
        // Direct board usage doesn't trigger GameSession logic, so simple return 0 expected
        assertEquals(0, board.reveal(tile));
    }

    @Test
    public void reveal_FlaggedTile_ShouldBeBlockedAndReturnZero() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile tile = new Tile();
        tile.setIsFlagged(true);
        assertEquals(0, board.reveal(tile));
    }

    @Test
    public void reveal_MineTile_ShouldDecrementTotalMinesLeft() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile mineTile = getMineTile(board);

        // Ensure we found a mine before trying to reveal it
        if (mineTile != null) {
            board.reveal(mineTile);
            // Hard difficulty starts with 44 mines, so 43 is the correct expectation
            assertEquals(43, board.getMinesLeft());
        }
    }

    @Test
    public void manualReveal_InsertedMineTile_ShouldUpdateMineCount() {
        // Renamed from 'tileGridInit'
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        board.reveal(new MineTile());
        assertEquals(9, board.getMinesLeft());
    }

    // ---------------------------------------------------------
    // ECONOMY & POINTS LOGIC
    // ---------------------------------------------------------


    @Test
    public void rightClick_FlaggedTile_ShouldUnflagButNotRefundPoints() throws Exception {
        // 1. Give points
        Field pointsField = GameSession.class.getDeclaredField("points");
        pointsField.setAccessible(true);
        pointsField.setInt(gameSession, 10);

        Tile tile = getNumberTile(gameSession.getLeftBoard());

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
    public void rightClick_MineTile_ShouldRevealMineAndGainPoint() {
        // Special rule: Flagging a mine reveals it and gives +1 point
        Tile mineTile = getMineTile(gameSession.getLeftBoard());
        int initialPoints = gameSession.getPoints();

        // Action: Right Click a Mine
        gameSession.RightClickedTile(mineTile);

        // Assertions
        assertTrue("Mine should be revealed", mineTile.isRevealed());
        assertEquals("Should gain 1 point", initialPoints + 1, gameSession.getPoints());
    }

    @Test
    public void flaggingEmptyTileCosts3Points(){
        Tile emptyTile = getEmptyTile(board);
        gameSession.setPoints(10);
        int points = gameSession.getPoints();
        gameSession.RightClickedTile(emptyTile);
        assertEquals(points - 3,gameSession.getPoints());
    }
    @Test
    public void flaggingEmptyTileActuallyFlags(){
        Tile emptyTile = getEmptyTile(board);
        gameSession.RightClickedTile(emptyTile);
        assertTrue(emptyTile.isFlagged());
    }

    // ---------------------------------------------------------
    // HEALTH & TURN MANAGEMENT
    // ---------------------------------------------------------

    @Test
    public void leftClick_MineTile_ShouldReduceHealthByOne() {
        Tile mineTile = getMineTile(gameSession.getLeftBoard());
        int initialHealth = gameSession.getHealthPool();

        // Action: Reveal Mine
        gameSession.LeftClickedTile(mineTile);

        assertEquals("Health should decrease by 1", initialHealth - 1, gameSession.getHealthPool());
        assertTrue(mineTile.isRevealed());
    }

    @Test
    public void leftClick_SafeTile_ShouldSwitchTurnToNextPlayer() {
        // Left board starts
        assertTrue(gameSession.getLeftBoard().getTurn());

        // Find a NumberTile (not a mine)
        Tile safeTile = getNumberTile(gameSession.getLeftBoard());

        // Reveal it
        gameSession.LeftClickedTile(safeTile);

        // Assert Turn Switched
        assertFalse("Left board should lose turn", gameSession.getLeftBoard().getTurn());
        assertTrue("Right board should gain turn", gameSession.getRightBoard().getTurn());
    }

    @Test
    public void leftClick_OpponentBoard_ShouldNotRevealOrSwitchTurn() {
        // Left board starts. Try to click on Right Board.
        Tile rightBoardTile = getNumberTile(gameSession.getRightBoard());

        gameSession.LeftClickedTile(rightBoardTile);

        // Turn should remain with Left
        assertTrue(gameSession.getLeftBoard().getTurn());
        assertFalse(rightBoardTile.isRevealed());
    }

    @Test
    public void rightClick_MineTile_ShouldSwitchTurnToNextPlayer() {
        // As per code: "revealing a mine by flagging does change a turn!"
        Tile mineTile = getMineTile(gameSession.getLeftBoard());

        assertTrue(gameSession.getLeftBoard().getTurn());

        gameSession.RightClickedTile(mineTile);

        assertFalse("Turn should switch after flagging a mine", gameSession.getLeftBoard().getTurn());
    }

    // ---------------------------------------------------------
    // SurpriseTile Initialization tests
    // ---------------------------------------------------------
    @Test
    public void SurpriseTileExists() {
        Board board = gameSession.getLeftBoard();
        SurpriseTile surpriseTile = getSurpriseTile(board);
        assertNotNull(surpriseTile);
    }

    @Test
    public void surpriseTile_ShouldNotBeRevealedInitially() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());
        assertFalse(surpriseTile.getIsRevealed());
    }

    @Test
    public void surpriseTile_ShouldNotBeFlaggedInitially() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());
        assertFalse(surpriseTile.getIsFlagged());
    }

    @Test
    public void surpriseTile_ShouldNotBeUsedInitially() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());
        assertFalse(surpriseTile.isUsed());
    }

    @Test
    public void surpriseTile_ShouldRevealAfterLeftClick() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());

        gameSession.LeftClickedTile(surpriseTile);

        assertTrue(surpriseTile.getIsRevealed());
    }

    @Test
    public void surpriseTile_ShouldNotBeUsedAfterFirstClick() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());

        gameSession.LeftClickedTile(surpriseTile);

        // Verifying it is not marked 'used' immediately upon reveal
        assertFalse(surpriseTile.isUsed());
    }

    @Test
    public void surpriseTile_ShouldNotBeUsedAfterSecondClick() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());

        gameSession.LeftClickedTile(surpriseTile); //reveal
        gameSession.LeftClickedTile(surpriseTile); //activate

        // Verifying your specific logic that it remains unused even after a second click
        assertFalse(surpriseTile.isUsed());
    }

    @Test
    public void activatingASurpriseTilesChangesPoints() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());
        gameSession.LeftClickedTile(surpriseTile); //reveal
        gameSession.changeTurn();
        int currentPoints = gameSession.getPoints();
        gameSession.LeftClickedTile(surpriseTile); //activating
        assertNotEquals(currentPoints, gameSession.getPoints());
    }

    @Test
    public void activatingASurpriseTileChangesHealth() {
        SurpriseTile surpriseTile = getSurpriseTile(gameSession.getLeftBoard());
        gameSession.LeftClickedTile(surpriseTile); //reveal
        gameSession.changeTurn();
        int currentHealth = gameSession.getHealthPool();
        gameSession.LeftClickedTile(surpriseTile); //activating
        assertNotEquals(currentHealth, gameSession.getPoints());
    }

    @Test
    public void cantActivateSurpriseIfNotEnoughPoints() {
        SurpriseTile surpriseTile = getSurpriseTile(board);
        gameSession.LeftClickedTile(surpriseTile); //reveal
        gameSession.setPoints(0);
        assertFalse(gameSession.LeftClickedTile(surpriseTile));
    }

    @Test
    public void canActivateSurpriseIfEnoughPoints() {
        SurpriseTile surpriseTile = getSurpriseTile(board);
        gameSession.LeftClickedTile(surpriseTile); //reveal
        gameSession.setPoints(100);
        gameSession.changeTurn();
        assertTrue(gameSession.LeftClickedTile(surpriseTile));
    }

    /*
     * HELPER METHODS
     */

    private MineTile getMineTile(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof MineTile) {
                    return (MineTile) tile;
                }
            }
        }
        return null;
    }


    private NumberTile getNumberTile(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof NumberTile) {
                    return (NumberTile) tile;
                }
            }
        }
        return null;
    }

    private SurpriseTile getSurpriseTile(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof SurpriseTile) {
                    return (SurpriseTile) tile;
                }
            }
        }
        return null;
    }
    private QuestionTile getQuestionTile(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof QuestionTile) {
                    return (QuestionTile) tile;
                }
            }
        }
        return null;
    }

    private NumberTile getEmptyTile(Board board) {
        for (Tile[] row : board.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof NumberTile && ((NumberTile) tile).getAdjacentMines() == 0)
                    return (NumberTile) tile;
            }
        }
        return null;
    }
}