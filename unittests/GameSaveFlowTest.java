import main.java.controller.GameSessionController;
import main.java.controller.NavigationController;
import main.java.model.GameData;
import main.java.model.GameDifficulty;
import main.java.model.GameSession;
import main.java.model.SysData;
import main.java.util.GameDataCSVManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameSaveFlowTest {

    private SysData sys;
    private GameSessionController controller;
    private NavigationController navStub;
    private GameSession initializedSession;
    private final String path = "GameHistory.csv";

    /**
     * @Before
     */
    @Before
    public void setUp() {
        try {
            // 1. קבלת מופעי סינגלטון ואיפוס
            sys = SysData.getInstance();
            sys.clearGames();

            // 2. אתחול GameSession בצורה מבוקרת
            initializedSession = GameSession.getInstance();
            // מאתחל את פרטי המשחק כמו ש-startNewGame עושה
            initializedSession.setLeftPlayerName("George");
            initializedSession.setRightPlayerName("Ali");
            initializedSession.setGameDifficulty(GameDifficulty.HARD);
            initializedSession.initializeBoards(); // מאתחל את leftBoard ו-rightBoard

            controller = GameSessionController.getInstance();

            // 3. יצירת NavigationController דמה (Stub)
            // יצירת JFrame דמה היא הכרחית לאתחול תקין של NavigationController Singleton
            navStub = NavigationController.getInstance(new JFrame());

            // 4. ניקוי קובץ ה-CSV מהרצות קודמות
            File tempFile = new File(path);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            Assert.fail("Setup failed: " + e.getMessage());
        }
    }

    /**
     * @After
     */
    @After
    public void tearDown() {
        sys.clearGames();
        File tempFile = new File(path);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    // ---------------------------------------------------------
    // Tests split into focused methods (format like GameplayTests)
    // ---------------------------------------------------------

    @Test
    public void endGame_ShouldIncreaseSysDataGameCount() throws IOException {
        int initialGameCount = sys.getNumberOfGames();

        // ACT
        controller.endGame(initializedSession, navStub);

        // ASSERT
        Assert.assertEquals("Game count in SysData should have increased by one.",
                initialGameCount + 1, sys.getNumberOfGames());
    }

    @Test
    public void endGame_SavedGameInSysData_ShouldHaveCorrectFields() throws IOException {
        // ARRANGE expected values
        String expectedLeftName = "George";
        String expectedRightName = "Ali";
        GameDifficulty expectedDifficulty = GameDifficulty.HARD;

        // ACT
        controller.endGame(initializedSession, navStub);

        // ASSERT - verify SysData contains the saved game with correct fields
        List<GameData> savedGames = sys.getGames();
        Assert.assertFalse("SysData should contain at least one saved game.", savedGames.isEmpty());
        GameData savedGame = savedGames.get(0);

        Assert.assertEquals("Saved game - Left player name mismatch.", expectedLeftName, savedGame.getLeftPlayerName());
        Assert.assertEquals("Saved game - Right player name mismatch.", expectedRightName, savedGame.getRightPlayerName());
        Assert.assertEquals("Saved game - Difficulty mismatch.", expectedDifficulty, savedGame.getGameDifficulty());
        // The test setup expects points to be 0 after forceGameOver in endGame flow
        Assert.assertEquals("Saved game - Points mismatch (should be 0 after forceGameOver).", 0, savedGame.getPoints());
    }

    @Test
    public void csvWriteAndRead_ShouldRestoreSavedGame() throws IOException {
        // ARRANGE
        String expectedLeftName = "George";
        int expectedPoints = 0;

        // ACT - end game which should write to CSV
        controller.endGame(initializedSession, navStub);

        // Clear in-memory SysData then read back from CSV
        sys.clearGames();
        GameDataCSVManager.readGameDataListFromCSV(path);

        // ASSERT - CSV read restored one game with correct fields
        Assert.assertEquals("Game count after reading from CSV should be 1.", 1, sys.getNumberOfGames());
        GameData readGame = sys.getGame(0);
        Assert.assertEquals("Read game - Left player name mismatch.", expectedLeftName, readGame.getLeftPlayerName());
        Assert.assertEquals("Read game - Points mismatch.", expectedPoints, readGame.getPoints());
    }
}