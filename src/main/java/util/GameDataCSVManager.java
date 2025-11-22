package main.java.util;
import java.util.List;
import main.java.model.*;
import main.java.test.Testable;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

//Class that handles the CSV file for storing the game data
public class GameDataCSVManager
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


}
