package main.java.controller;

import main.java.model.GameData;
import main.java.model.SysData;
import main.java.view.GameHistoryScreen;

import java.util.List;

/**
 * Controller for the Game History screen.
 * Follows the MVC pattern to mediate between the GameHistoryScreen view and the
 * data model (SysData).
 * Manages the retrieval and display of past game records.
 */
public class HistoryController {
    private GameHistoryScreen view;
    private SysData sysData;

    private static HistoryController instance;

    /**
     * Retrieves the singleton instance of the HistoryController.
     * 
     * @return The single instance of HistoryController.
     */
    public static HistoryController getInstance() {
        if (instance == null) {
            instance = new HistoryController();
        }
        return instance;
    }

    /**
     * Sets the view associated with this controller.
     * 
     * @param view The GameHistoryScreen instance.
     */
    public void setView(GameHistoryScreen view) {
        this.view = view;
    }

    /**
     * Fetches the list of past games from the system data and updates the view
     * table.
     * safely handles cases where the view might not be initialized.
     */
    public void refreshGamesList() {
        if (view == null)
            return;
        List<GameData> games = SysData.getInstance().getGames();
        view.populateHistoryTable(games);
    }
}
