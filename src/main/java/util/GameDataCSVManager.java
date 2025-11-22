package main.java.util;
import java.util.List;
import main.java.model.*;
import main.java.test.Testable;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

//Class that handles the CSV file for storing the game data
public class GameDataCSVManager implements Testable
{

    //Formatter of Timestamps
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    //Header of the CSV file
    private static final String CSV_HEADER = "Timestamp,LeftPlayerName,RightPlayerName,GameDifficulty,Points";

    //Write the game data list to a CSV file
    public static void writeGameDataListToCSV(List<GameData> gameDataList, String filePath) throws IOException {
        try (FileWriter fw = new FileWriter(filePath);
             PrintWriter pw = new PrintWriter(fw))
        {
            pw.println(CSV_HEADER);
            for (GameData data : gameDataList)
            {
                // handle nulls safely
                String timeStampStr = data.getTimeStamp() == null ? "" : data.getTimeStamp().format(formatter);
                String left = data.getLeftPlayerName() == null ? "" : data.getLeftPlayerName();
                String right = data.getRightPlayerName() == null ? "" : data.getRightPlayerName();
                String difficulty = data.getGameDifficulty() == null ? "" : data.getGameDifficulty().toString();
                String pointsStr = Integer.toString(data.getPoints());

                // Create a comma-separated line for the record
                String line = String.format("%s,%s,%s,%s,%s",
                        timeStampStr,
                        left,
                        right,
                        difficulty,
                        pointsStr);

                pw.println(line);
            }
            // Ensure data is written to the disk
            pw.flush();
        }
    }

    //Read the game data list from a CSV file
    public static List<GameData> readGameDataListFromCSV(String filePath) throws IOException
    {
        List<GameData> gameDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            if ((line = br.readLine()) != null) {
                //Check if the header matches
                if (!line.trim().equals(CSV_HEADER)) {
                    System.err.println("Warning: CSV header mismatch. Expected: " + CSV_HEADER);
                }
            } else {
                return gameDataList;
            }


            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // preserve empty fields

                if (values.length == 5) {
                    try {
                        // empty fields => nulls where appropriate
                        LocalDateTime timeStamp = null;
                        String tsField = values[0].trim();
                        if (!tsField.isEmpty()) {
                            timeStamp = LocalDateTime.parse(tsField, formatter);
                        }

                        String leftPlayerName = values[1].trim();
                        if (leftPlayerName.isEmpty()) leftPlayerName = null;

                        String rightPlayerName = values[2].trim();
                        if (rightPlayerName.isEmpty()) rightPlayerName = null;

                        GameDifficulty gameDifficulty = null;
                        String diffField = values[3].trim();
                        if (!diffField.isEmpty()) {
                            // valueOf expects proper enum name
                            gameDifficulty = GameDifficulty.valueOf(diffField.toUpperCase());
                        }

                        int points = 0;
                        String pointsField = values[4].trim();
                        if (!pointsField.isEmpty()) {
                            points = Integer.parseInt(pointsField);
                        }

                        GameData gd = new GameData(timeStamp, leftPlayerName, rightPlayerName, gameDifficulty, points);
                        gameDataList.add(gd);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping malformed line in CSV: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (java.io.FileNotFoundException e) {
            return gameDataList;
        }
        return gameDataList;
    }

    //Test the class
    @Override
    public boolean runClassTests()
    {
        try {
            // create temporary file
            File tmp = File.createTempFile("gamedata_test_", ".csv");
            String path = tmp.getAbsolutePath();
            tmp.deleteOnExit();

            // prepare test data
            LocalDateTime ts1 = LocalDateTime.of(2021, 1, 2, 3, 4, 5);
            LocalDateTime ts2 = LocalDateTime.of(2022, 6, 7, 8, 9, 10);
            GameData g1 = new GameData(ts1, "LeftOne", "RightOne", GameDifficulty.EASY, 10);
            GameData g2 = new GameData(ts2, "LeftTwo", "RightTwo", GameDifficulty.HARD, 20);

            List<GameData> writeList = new ArrayList<>();
            writeList.add(g1);
            writeList.add(g2);

            // write and read back
            writeGameDataListToCSV(writeList, path);
            List<GameData> readList = readGameDataListFromCSV(path);

            if (readList.size() != 2) return false;

            GameData r1 = readList.get(0);
            GameData r2 = readList.get(1);

            if (!ts1.equals(r1.getTimeStamp())) return false;
            if (!"LeftOne".equals(r1.getLeftPlayerName())) return false;
            if (!"RightOne".equals(r1.getRightPlayerName())) return false;
            if (r1.getGameDifficulty() != GameDifficulty.EASY) return false;
            if (r1.getPoints() != 10) return false;

            if (!ts2.equals(r2.getTimeStamp())) return false;
            if (!"LeftTwo".equals(r2.getLeftPlayerName())) return false;
            if (!"RightTwo".equals(r2.getRightPlayerName())) return false;
            if (r2.getGameDifficulty() != GameDifficulty.HARD) return false;
            if (r2.getPoints() != 20) return false;

            // reading non-existing file should return empty list (FileNotFound handled)
            List<GameData> missing = readGameDataListFromCSV(path + "_does_not_exist_1234");
            if (missing == null) return false;
            if (!missing.isEmpty()) return false;

            // clean up
            if (!tmp.delete())
                System.err.println("Warning: Could not delete temporary file " + tmp.getAbsolutePath());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
