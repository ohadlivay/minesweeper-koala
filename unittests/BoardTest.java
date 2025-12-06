import static org.junit.Assert.assertEquals;

import main.java.model.Board;
import main.java.model.GameDifficulty;
import main.java.model.Tile;
import org.junit.Test;

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
}