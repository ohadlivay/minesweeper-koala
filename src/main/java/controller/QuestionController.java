package main.java.controller;

import main.java.model.Question;
import main.java.model.SysData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QuestionController {
    private static QuestionController instance;
    private Set<Question> usedQuestions; // Changed visibility to private for encapsulation

    private QuestionController(){
        // CRITICAL FIX: You must initialize the Set, otherwise you get a NullPointerException
        this.usedQuestions = new HashSet<>();
    }

    public static QuestionController getInstance(){
        if(instance == null){
            instance = new QuestionController();
        }
        return instance;
    }

    /*
    Gets a question from SysData.
    Guarantees that the question was not asked in this game session.
     */
    public Question pollQuestion(){
        SysData sys = SysData.getInstance();
        ArrayList<Question> questions = (ArrayList<Question>) sys.getQuestions();

        // Safety check: Avoid crash if SysData is empty
        if (questions == null || questions.isEmpty()) {
            return null;
        }

        // Iterate to find the first question NOT in the used set
        for (Question question : questions){
            // FIX: Java uses .contains(), not "not in"
            if(!usedQuestions.contains(question)){
                usedQuestions.add(question);
                return question;
            }
        }

        /* If loop finishes, all questions were used.
        Refresh and repeat!
         */
        restartUsedQuestions();

        // FIX: Use .get(0) to grab the first question after reset
        Question q = questions.get(0);
        usedQuestions.add(q);
        return q;
    }

    public boolean setUsedQuestions(Set<Question> questions){
        this.usedQuestions = questions;
        return true;
    }

    /*
    Clears the history of asked questions
     */
    public boolean restartUsedQuestions(){
        // FIX: "new Set" is invalid because Set is an interface. 
        // We can simply clear the existing HashSet.
        if (this.usedQuestions != null) {
            this.usedQuestions.clear();
        } else {
            this.usedQuestions = new HashSet<>();
        }
        return true;
    }
}