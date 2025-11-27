package main.java.test;

import main.java.model.*;
import main.java.util.GameDataCSVManager;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

// Test class to verify CSV write/read round-trip using GameDataCSVManager
public class GameDataCSVManagerTest implements Testable
{
    @Override
    public boolean runClassTests()
    {
        try {
            // Prepare a temporary file for the CSV
            File tmp = File.createTempFile("gamedata_test", ".csv");
            tmp.deleteOnExit();
            String path = tmp.getAbsolutePath();
            System.out.println("Created temp CSV file: " + path);

            // Prepare SysData with sample entries
            SysData sys = SysData.getInstance();
            sys.clearGames();
            System.out.println("Cleared SysData, number of games: " + sys.getNumberOfGames());

            LocalDateTime ts1 = LocalDateTime.of(2021, 5, 10, 14, 30, 0);
            GameData g1 = new GameData(ts1, "Alice", "Bob", GameDifficulty.EASY, 10);
            LocalDateTime ts2 = LocalDateTime.of(2022, 12, 1, 9, 15, 5);
            GameData g2 = new GameData(ts2, null, "Charlie", GameDifficulty.HARD, 42);
            GameData g3 = new GameData(null, "Dana", null, null, 0); // test nulls

            sys.addGame(g1);
            sys.addGame(g2);
            sys.addGame(g3);
            System.out.println("Added 3 games to SysData, current count: " + sys.getNumberOfGames());

            // Write to CSV
            GameDataCSVManager.writeGameDataListToCSV(path);
            System.out.println("Wrote games to CSV: " + path);

            // Clear and read back
            sys.clearGames();
            System.out.println("After clearing, number of games: " + sys.getNumberOfGames());
            if (sys.getNumberOfGames() != 0) {
                System.out.println("Failed: SysData not empty after clear. Expected 0, got " + sys.getNumberOfGames());
                return false;
            }
            System.out.println("SysData cleared successfully.");

            GameDataCSVManager.readGameDataListFromCSV(path);
            System.out.println("Read games from CSV.");

            // Validate
            System.out.println("Validating read data...");
            if (sys.getNumberOfGames() != 3) {
                System.out.println("Failed: Expected 3 games after read, found " + sys.getNumberOfGames());
                return false;
            }
            System.out.println("Game count validation OK.");

            List<GameData> read = sys.getGames();

            GameData r1 = read.get(0);
            if (!ts1.equals(r1.getTimeStamp())) {
                System.out.println("Failed: r1 timestamp mismatch. Expected " + ts1 + " got " + r1.getTimeStamp());
                return false;
            }
            System.out.println("r1 timestamp OK.");
            if (!"Alice".equals(r1.getLeftPlayerName())) {
                System.out.println("Failed: r1 left player mismatch. Expected 'Alice' got '" + r1.getLeftPlayerName() + "'");
                return false;
            }
            System.out.println("r1 left player OK.");
            if (!"Bob".equals(r1.getRightPlayerName())) {
                System.out.println("Failed: r1 right player mismatch. Expected 'Bob' got '" + r1.getRightPlayerName() + "'");
                return false;
            }
            System.out.println("r1 right player OK.");
            if (r1.getGameDifficulty() != GameDifficulty.EASY) {
                System.out.println("Failed: r1 difficulty mismatch. Expected EASY got " + r1.getGameDifficulty());
                return false;
            }
            System.out.println("r1 difficulty OK.");
            if (r1.getPoints() != 10) {
                System.out.println("Failed: r1 points mismatch. Expected 10 got " + r1.getPoints());
                return false;
            }
            System.out.println("r1 points OK.");

            GameData r2 = read.get(1);
            if (!ts2.equals(r2.getTimeStamp())) {
                System.out.println("Failed: r2 timestamp mismatch. Expected " + ts2 + " got " + r2.getTimeStamp());
                return false;
            }
            System.out.println("r2 timestamp OK.");
            if (r2.getLeftPlayerName() != null) {
                System.out.println("Failed: r2 left player expected null but got '" + r2.getLeftPlayerName() + "'");
                return false;
            }
            System.out.println("r2 left player OK (null).");
            if (!"Charlie".equals(r2.getRightPlayerName())) {
                System.out.println("Failed: r2 right player mismatch. Expected 'Charlie' got '" + r2.getRightPlayerName() + "'");
                return false;
            }
            System.out.println("r2 right player OK.");
            if (r2.getGameDifficulty() != GameDifficulty.HARD) {
                System.out.println("Failed: r2 difficulty mismatch. Expected HARD got " + r2.getGameDifficulty());
                return false;
            }
            System.out.println("r2 difficulty OK.");
            if (r2.getPoints() != 42) {
                System.out.println("Failed: r2 points mismatch. Expected 42 got " + r2.getPoints());
                return false;
            }
            System.out.println("r2 points OK.");

            GameData r3 = read.get(2);
            if (r3.getTimeStamp() != null) {
                System.out.println("Failed: r3 timestamp expected null but got " + r3.getTimeStamp());
                return false;
            }
            System.out.println("r3 timestamp OK (null).");
            if (!"Dana".equals(r3.getLeftPlayerName())) {
                System.out.println("Failed: r3 left player mismatch. Expected 'Dana' got '" + r3.getLeftPlayerName() + "'");
                return false;
            }
            System.out.println("r3 left player OK.");
            if (r3.getRightPlayerName() != null) {
                System.out.println("Failed: r3 right player expected null but got '" + r3.getRightPlayerName() + "'");
                return false;
            }
            System.out.println("r3 right player OK (null).");
            if (r3.getGameDifficulty() != null) {
                System.out.println("Failed: r3 difficulty expected null but got " + r3.getGameDifficulty());
                return false;
            }
            System.out.println("r3 difficulty OK (null).");
            if (r3.getPoints() != 0) {
                System.out.println("Failed: r3 points mismatch. Expected 0 got " + r3.getPoints());
                return false;
            }
            System.out.println("r3 points OK.");

            // All checks passed
            System.out.println("All checks passed.");
            return true;
        } catch (Exception ex) {
            System.out.println("Exception during test: " + ex);
            ex.printStackTrace();
            return false;
        }
    }
    // Main entry to run this test standalone (placed inside the test class)
    public static void main(String[] args)
    {
        GameDataCSVManagerTest t = new GameDataCSVManagerTest();
        boolean ok = t.runClassTests();
        System.out.println(ok ? "PASS" : "FAIL");
        System.exit(ok ? 0 : 1);
    }
}
