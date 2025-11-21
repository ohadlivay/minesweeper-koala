package main.java.util;
import java.util.List;
import main.java.model.*;
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
                String timeStampStr = data.getTimeStamp().format(formatter);

                // Create a comma-separated line for the record
                String line = String.format("%s,%s,%s,%s,%d",
                        timeStampStr,
                        data.getLeftPlayerName(),
                        data.getRightPlayerName(),
                        data.getGameDifficulty().toString(),
                        data.getPoints());

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
                String[] values = line.split(",");


                if (values.length == 5) {
                    try {

                        LocalDateTime timeStamp = LocalDateTime.parse(values[0], formatter);

                        String leftPlayerName = values[1].trim();
                        String rightPlayerName = values[2].trim();

                        Difficulty gameDifficulty = Difficulty.valueOf(values[3].trim().toUpperCase());

                        int points = Integer.parseInt(values[4].trim());
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
