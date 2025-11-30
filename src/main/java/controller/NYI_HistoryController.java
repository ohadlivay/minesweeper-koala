package main.java.controller;

import main.java.model.SysData;
import main.java.view.GameHistoryScreen;
/*
NYI = NOT YET IMPLEMENTED
 */
public class NYI_HistoryController {
    private GameHistoryScreen gameHistoryScreen;
    private SysData sysData;

    private static NYI_HistoryController instance;

    private NYI_HistoryController()
    {

    }

    public static NYI_HistoryController getInstance()
    {
        if(instance == null){
            instance = new NYI_HistoryController();
        }
        return instance;
    }
}
