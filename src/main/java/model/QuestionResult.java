package main.java.model;

public class QuestionResult {
    private QuestionDifficulty difficulty;
    private boolean correct;
    private static QuestionResult instance;

    private QuestionResult(QuestionDifficulty difficulty, boolean correct) {
        this.difficulty = difficulty;
        this.correct = correct;
    }
    public static QuestionResult getInstance() {
        if (instance == null) {
            instance = new QuestionResult(QuestionDifficulty.EASY, false);
        }
        return instance;
    }

    public QuestionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuestionDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
