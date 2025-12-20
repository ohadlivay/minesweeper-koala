package main.java.controller;

import main.java.model.*;

import java.util.*;

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
    this will be used by any class that implements displayQuestionListener using the method displayQuestion (for tali)
     */
    public Question pollQuestion() {
        SysData sys = SysData.getInstance();
        List<Question> questions = sys.getQuestions();

        if (questions == null || questions.isEmpty()) return null;

        // Check if we need to reset first
        if (usedQuestions.size() >= questions.size()) {
            restartUsedQuestions();
        }

        // Create a list of available indices
        List<Integer> availableIndices = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            if (!usedQuestions.contains(questions.get(i))) {
                availableIndices.add(i);
            }
        }

        // Pick a random index from the available ones
        int randomIndex = availableIndices.get(new Random().nextInt(availableIndices.size()));
        Question q = questions.get(randomIndex);

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

    public void submitQuestionResult(boolean isCorrect, QuestionDifficulty difficulty, Board board) {
        //tom you can use this method to get the result of the question into GameSession
        GameSession session = GameSessionController.getInstance().getSession();
        session.updateAfterQuestionResult(difficulty, isCorrect, board);
    }
}