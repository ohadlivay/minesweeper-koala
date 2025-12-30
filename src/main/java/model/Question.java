package main.java.model;

public class Question {

    private int id;
    private String questionText;
    private QuestionDifficulty difficulty;

    private String answer1; // always correct
    private String answer2;
    private String answer3;
    private String answer4;

    private static int maxQuestionLength = 200; //in chars
    private static int maxAnswerLength = 70;

    public Question(int id, String questionText, QuestionDifficulty difficulty, String answer1, String answer2, String answer3, String answer4) {
        this.id = id;
        this.questionText = questionText;
        this.difficulty = difficulty;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        validateUniqueAnswers(answer1, answer2, answer3, answer4);
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

    public static int getMaxQuestionLength() { return maxQuestionLength; }
    public static int getMaxAnswerLength() { return maxAnswerLength; }


    // --- Setters ---
    // these will need to update the csv aswell, so changes are permanent and not just in RAM
    public void setId(int id) { this.id = id; }
    public void setQuestionText(String questionText) {
        if (questionText.length() > maxQuestionLength) throw new IllegalArgumentException("Question text too long");
        this.questionText = questionText;
    }

    public void setDifficulty(QuestionDifficulty difficulty) { this.difficulty = difficulty; }
    public void setAnswer1(String answer1) {
        if (answer1.length() > maxAnswerLength) throw new IllegalArgumentException("Answer 1 text too long");
        this.answer1 = answer1;
    }

    public void setAnswer2(String answer2) {
        if (answer2.length() > maxAnswerLength) throw new IllegalArgumentException("Answer 2 text too long");
        this.answer2 = answer2;
    }

    public void setAnswer3(String answer3) {
        if (answer3.length() > maxAnswerLength) throw new IllegalArgumentException("Answer 3 text too long");
        this.answer3 = answer3;
    }
    public void setAnswer4(String answer4) {
        if (answer4.length() > maxAnswerLength) throw new IllegalArgumentException("Answer 4 text too long");
        this.answer4 = answer4;
    }

    private void validateUniqueAnswers(String a1, String a2, String a3, String a4)
    {
        java.util.Set<String> uniqueAnswers = new java.util.HashSet<>();
        uniqueAnswers.add(a1.trim().toLowerCase());
        uniqueAnswers.add(a2.trim().toLowerCase());
        uniqueAnswers.add(a3.trim().toLowerCase());
        uniqueAnswers.add(a4.trim().toLowerCase());
        if (uniqueAnswers.size() != 4) throw new IllegalArgumentException("Answers must be unique");
    }
}