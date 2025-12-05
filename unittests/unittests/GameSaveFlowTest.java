package unittests;

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

// מבחן אינטגרציה לבדיקת תהליך שמירת המשחק המלא
public class GameSaveFlowTest {

    private SysData sys;
    private GameSessionController controller;
    private NavigationController navStub; // נשאר דמה, נמנע מתלויות ב-UI
    private GameSession initializedSession; // GameSession מאותחל מראש
    private final String path = "GameHistory.csv";

    /**
     * @Before: מכין את סביבת הבדיקה לפני כל מבחן.
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
            initializedSession.setDifficulty(GameDifficulty.HARD);
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
     * @After: מנקה את סביבת הבדיקה לאחר כל מבחן.
     */
    @After
    public void tearDown() {
        sys.clearGames();
        File tempFile = new File(path);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        // אין צורך לנקות את ה-Singletons של GameSession ו-GameSessionController
        // אלא אם כן יש בעיות עקביות בין מבחנים, אבל נסמוך על ה-setUp שיאתחל נכון.
    }

    /**
     * בודק את זרימת סיום המשחק ושמירת הנתונים.
     * אנו משתמשים ב-GameSession שכבר אומת ואותחל ב-setUp כדי לעקוף את תלות ה-UI.
     */
    @Test
    public void testEndGameAndSaveFlow() throws IOException {
        // ARRANGE - נתונים צפויים
        String expectedLeftName = "George";
        String expectedRightName = "Ali";
        GameDifficulty expectedDifficulty = GameDifficulty.HARD;
        int initialGameCount = sys.getNumberOfGames();

        // ACT 1 - סיום המשחק ושמירת הנתונים
        // אנו משתמשים ב-initializedSession שיצרנו ב-setUp, המהווה את המודל האמיתי.
        controller.endGame(initializedSession, navStub);

        // ASSERT 1 - אימות הנתונים ב-SysData
        Assert.assertEquals("Game count in SysData should have increased by one.", initialGameCount + 1, sys.getNumberOfGames());

        List<GameData> savedGames = sys.getGames();
        GameData savedGame = savedGames.get(0);

        // אימות שפרטי המשחק נשמרו נכון
        Assert.assertEquals("Saved game - Left player name mismatch.", expectedLeftName, savedGame.getLeftPlayerName());
        Assert.assertEquals("Saved game - Right player name mismatch.", expectedRightName, savedGame.getRightPlayerName());
        Assert.assertEquals("Saved game - Difficulty mismatch.", expectedDifficulty, savedGame.getGameDifficulty());
        // הנקודות הסופיות הן 0 לאחר forceGameOver
        Assert.assertEquals("Saved game - Points mismatch (should be 0 after forceGameOver).", 0, savedGame.getPoints());

        // ASSERT 2 - אימות שהנתונים נכתבו לקובץ (ע"י קריאה חוזרת)

        // נקה את SysData ובצע קריאה חוזרת מהקובץ
        sys.clearGames();
        GameDataCSVManager.readGameDataListFromCSV(path);

        // אימות שהקריאה מהקובץ החזירה נתונים
        Assert.assertEquals("Game count after reading from CSV should be 1.", 1, sys.getNumberOfGames());

        GameData readGame = sys.getGame(0);
        Assert.assertEquals("Read game - Left player name mismatch.", expectedLeftName, readGame.getLeftPlayerName());
        Assert.assertEquals("Read game - Points mismatch.", 0, readGame.getPoints());
    }
}