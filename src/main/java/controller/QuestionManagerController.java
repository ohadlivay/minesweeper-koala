/*
NYI = NOT YET IMPLEMENTED
 */

package main.java.controller;

import main.java.model.Question;
import main.java.view.QuestionManagerScreen;

import java.util.ArrayList;

public class QuestionManagerController {
    private QuestionManagerScreen qms;
    private ArrayList<Question> questions;
    private static QuestionManagerController instance;

    private QuestionManagerController()
    {

    }

    public static QuestionManagerController getInstance()
    {
        if(instance == null){
            instance = new QuestionManagerController();
        }
        return instance;
    }

    public void refreshQuestionTable() {
        //Ohad please implement this method and send a list of questions to the view -Tali
    }


}
