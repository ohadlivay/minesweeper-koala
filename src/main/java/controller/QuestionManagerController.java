package main.java.controller;

import main.java.model.Question;
import main.java.model.QuestionDifficulty;
import main.java.model.SysData;
import main.java.util.QuestionCSVManager;
import main.java.view.QuestionManagerScreen;

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

    // jumps to last page after refresh
    public void refreshAndJumpToLastPage() {
        if (view == null)
            return;
        List<Question> questions = SysData.getInstance().getQuestions();
        view.populateTable(questions);
        view.jumpToLastPageAndPopulate(questions);
    }
    /*
    delete/edit/add questions.
     */
    public boolean userDeletedQuestion(Question question) {
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

    /*
    assuming when a user presses 'Add new question', a new empty line will be shown to him and a new Question object
    will be created (and returned by this method)
    the user can then edit the Question and save it, changing the Question object.
     */
    public Question userAddedQuestion() {
        return Question.generateBlankQuestion();
    }

    public boolean userSavedEditedQuestion(int id, String questionText, QuestionDifficulty difficulty, String answer1, String answer2, String answer3, String answer4) {
        /*
        assuming the user pressed 'Edit', a window to edit has opened, user edited the fields to his liking, pressed 'Save' and then this was activated.
        so this will take the user input, delete the old question, create a new Question (same id) and save it to csv.
         */
        try
        {
            Question temp = new Question(id, questionText, difficulty, answer1, answer2, answer3, answer4);
            Question.validateUniqueQuestionText(questionText, id);
        }catch(IllegalArgumentException e)
        {
            return false;
        }
        SysData sys = SysData.getInstance();
        Question ogQuestion = sys.getQuestionByID(id);
        int ogId = sys.getQuestions().indexOf(ogQuestion);

        boolean success = sys.deleteQuestion(sys.getQuestionByID(id));
        if(!success) {return false;}

        Question newQuestion = new Question(id,  questionText, difficulty, answer1, answer2, answer3, answer4);
        if (ogId!=-1 && ogId <= sys.getQuestions().size()) {
            sys.getQuestions().add(ogId, newQuestion);
        }
        else
        {
            sys.addQuestion(newQuestion);
        }
        try{
            QuestionCSVManager.rewriteQuestionsToCSVFromSysData();
        } catch(Exception e){
            System.err.println("Error while rewriting questions to csv: " + e.getMessage());
        }
        return true;
    }
}
