import main.java.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
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
        board.reveal(tile);
        assertEquals(0, board.reveal(tile));
    }

    @Test
    public void boardFlagFlaggedTile() {
        Board board = Board.createNewBoard(GameDifficulty.HARD);
        Tile tile = new Tile();
        tile.setIsFlagged(true);
        board.reveal(tile);
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

        // Ensure we found a mine before trying to reveal it as we're testing specific functionality
        if (mineTile != null) {
            board.reveal(mineTile);
            // Hard difficulty starts with 44 mines, so 43 is the correct expectation
            assertEquals(43, board.getMinesLeft());
        }
    }

    @Test
    public void leftPlayerTurnFirst(){
        GameSession gameSession = GameSession.getTestInstance();
        gameSession.initializeBoards();
        assertTrue(gameSession.getLeftBoard().getTurn());
    }

    public void leftPlayerTurnFirst(){
        GameSession gameSession = GameSession.getTestInstance();
        gameSession.initializeBoards();
        gameSession.getHealthPool()
    }

    @Test
    public void tileGridInit() {
        Board board = Board.createNewBoard(GameDifficulty.EASY);
        board.reveal(new MineTile());
        assertEquals(9, board.getMinesLeft());
    }

    /*
    helper methods
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
}