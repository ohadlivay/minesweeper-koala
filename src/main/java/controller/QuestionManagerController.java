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
        assert questions != null;
        view.populateTable(questions);
    }
/*
tali's delete/edit/add questions.
 */
    public boolean deleteQuestion(Question question) {
        try {
            // attempt to delete (this calls the list removal and CSV update)
            boolean wasRemoved = SysData.getInstance().deleteQuestion(question);

            if (wasRemoved) {
                System.out.println("Question deleted successfully.");
                return true;
            } else {
                System.err.println("Question not found.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error during deletion: " + e.getMessage());
            return false;
        }
    }


}
