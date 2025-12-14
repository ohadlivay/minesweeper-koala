package main.java.model;

public class Question {

    private int id;
    private String questionText;
    private QuestionDifficulty difficulty;

    private String answer1; // always correct
    private String answer2;
    private String answer3;
    private String answer4;

    public Question(int id, String questionText, QuestionDifficulty difficulty, String answer1, String answer2, String answer3, String answer4) {
        this.id = id;
        this.questionText = questionText;
        this.difficulty = difficulty;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getQuestionText() { return questionText; }
    public QuestionDifficulty getDifficulty() { return difficulty; }

    public String getAnswer1() { return answer1; }
    public String getAnswer2() { return answer2; }
    public String getAnswer3() { return answer3; }
    public String getAnswer4() { return answer4; }

    // Helper to specifically get the correct answer text
    public String getCorrectAnswer() {
        return answer1;
    }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setDifficulty(QuestionDifficulty difficulty) { this.difficulty = difficulty; }
    public void setAnswer1(String answer1) { this.answer1 = answer1; }
    public void setAnswer2(String answer2) { this.answer2 = answer2; }
    public void setAnswer3(String answer3) { this.answer3 = answer3; }
    public void setAnswer4(String answer4) { this.answer4 = answer4; }
}