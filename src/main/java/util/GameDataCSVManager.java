package main.java.util;

import main.java.model.GameData;
import main.java.model.GameDifficulty;
import main.java.model.SysData;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//Class that handles the CSV file for storing the game data
public class GameDataCSVManager
{

    //Formatter of Timestamps
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    //Header of the CSV file
    private static final String CSV_HEADER = "Timestamp,LeftPlayerName,RightPlayerName,GameDifficulty,Points,IsWin";

    //Write the game data list to a CSV file using the SysData singleton
    public static void writeGameDataListToCSV(String filePath) throws IOException {
        SysData sys = SysData.getInstance();
        List<GameData> gameDataList = sys.getGames();

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
                String isWinStr = Boolean.toString(data.isWin());

                // Create a comma-separated line for the record
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        timeStampStr,
                        left,
                        right,
                        difficulty,
                        pointsStr,
                        isWinStr);

                pw.println(line);
            }
            // Ensure data is written to the disk
            pw.flush();
        }
    }

    //Read the game data list from a CSV file and populate the SysData singleton
    public static void readGameDataListFromCSV(String filePath) throws IOException
    {
        SysData sys = SysData.getInstance();
        sys.clearGames();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            if ((line = br.readLine()) != null) {
                //Check if the header matches
                if (!line.trim().equals(CSV_HEADER)) {
                    System.err.println("Warning: CSV header mismatch. Expected: " + CSV_HEADER);
                }
            } else {
                return;
            }


            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // preserve empty fields

                if (values.length == 6) {
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
                        boolean isWin = false;
                        if (values.length > 5 && !values[5].trim().isEmpty()) {
                            isWin = Boolean.parseBoolean(values[5].trim());
                        }

                        GameData gd = new GameData(timeStamp, leftPlayerName, rightPlayerName, gameDifficulty, points, isWin);
                        sys.addGame(gd);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping malformed line in CSV: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        } catch (java.io.FileNotFoundException e) {
            // File does not exist; treat as empty data set
            System.err.println("CSV file not found: " + filePath + ". Starting with empty game data.");
        }
    }


}
