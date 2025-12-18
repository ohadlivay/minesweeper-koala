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

    private Question(){
        this.id = SysData.getInstance().getMaxId() + 1; //id will be = max of all ids, +1.
        this.questionText = "Question Text"; // default
        this.difficulty = QuestionDifficulty.EASY; //default
        this.answer1 = "Correct answer";
        this.answer2 = "Wrong answer";
        this.answer3 = "Wrong answer";
        this.answer4 = "Wrong answer";
    }

    public static Question generateBlankQuestion() {
        return new Question();
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getQuestionText() { return questionText; }
    public QuestionDifficulty getDifficulty() { return difficulty; }

    public String getAnswer1() { return answer1; }
    public String getAnswer2() { return answer2; }
    public String getAnswer3() { return answer3; }
    public String getAnswer4() { return answer4; }


    // --- Setters ---
    // these will need to update the csv aswell, so changes are permanent and not just in RAM
    public void setId(int id) { this.id = id; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setDifficulty(QuestionDifficulty difficulty) { this.difficulty = difficulty; }
    public void setAnswer1(String answer1) { this.answer1 = answer1; }
    public void setAnswer2(String answer2) { this.answer2 = answer2; }
    public void setAnswer3(String answer3) { this.answer3 = answer3; }
    public void setAnswer4(String answer4) { this.answer4 = answer4; }
}