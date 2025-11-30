/*
NYI = NOT YET IMPLEMENTED
 */

package main.java.controller;

import main.java.model.Question;
import main.java.view.QuestionManagerScreen;

import java.util.ArrayList;

public class NYI_QuestionManagerController {
    private QuestionManagerScreen qms;
    private ArrayList<Question> questions;
    private static NYI_QuestionManagerController instance;

    private NYI_QuestionManagerController()
    {

    }

    public static NYI_QuestionManagerController getInstance()
    {
        if(instance == null){
            instance = new NYI_QuestionManagerController();
        }
        return instance;
    }
}
