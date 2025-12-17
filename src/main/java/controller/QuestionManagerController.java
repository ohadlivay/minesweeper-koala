/*
NYI = NOT YET IMPLEMENTED
 */

package main.java.controller;

import main.java.model.Question;
import main.java.model.SysData;
import main.java.view.QuestionManagerScreen;

import java.util.ArrayList;
import java.util.List;

public class QuestionManagerController {
    private static QuestionManagerController instance;
    private QuestionManagerScreen view;


    public static QuestionManagerController getInstance()
    {
        if(instance == null){
            instance = new QuestionManagerController();
        }
        return instance;
    }

    public void setView(QuestionManagerScreen view) {
        this.view = view;
    }

    public void refreshQuestionList() {
        if (view == null)
            return;
        List<Question> questions = SysData.getInstance().getQuestions();
        view.populateTable(questions);
    }



}
