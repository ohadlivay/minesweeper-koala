package main.java.controller;

import main.java.model.GameData;
import main.java.model.SysData;
import main.java.view.GameHistoryScreen;

import java.util.List;

public class HistoryController {
    private GameHistoryScreen view;
    private SysData sysData;

    private static HistoryController instance;

    public static HistoryController getInstance()
    {
        if(instance == null){
            instance = new HistoryController();
        }
        return instance;
    }

    public void setView(GameHistoryScreen view) {
        this.view = view;
    }

    public void refreshGamesList()
    {
        if (view == null) return;
        List<GameData> games = SysData.getInstance().getGames();
        view.populateHistoryTable(games);
    }

    // clear all games from the list
    public void clearAllHistory()
    {
        try {
            // clear the list of games
            SysData.getInstance().clearGames();
            // clear the CSV file
            main.java.util.GameDataCSVManager.deleteAllGames("GameHistory.csv");
            // refresh the list
            refreshGamesList();
        }catch (Exception e){
            System.err.println("Error clearing history: " + e.getMessage());
        }
    }
}
