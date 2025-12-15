package main.java.util;

import main.java.model.*;
import java.io.*;
import java.util.List;

public class QuestionCSVManager {

    private static final String CSV_HEADER = "ID,Question,Difficulty,A,B,C,D,Correct Answer";

    public static void writeQuestionsToCSV(String filePath) throws IOException {
        SysData sys = SysData.getInstance();
        List<Question> questionsList = sys.getQuestions();

        try (FileWriter fw = new FileWriter(filePath);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(CSV_HEADER);

            for (Question q : questionsList) {
                String id = String.valueOf(q.getId());
                String text = q.getQuestionText() == null ? "" : q.getQuestionText();
                String diff = q.getDifficulty() == null ? "EASY" : q.getDifficulty().toString();

                // Fetch the 4 attributes directly
                String a = q.getAnswer1() == null ? "" : q.getAnswer1();
                String b = q.getAnswer2() == null ? "" : q.getAnswer2();
                String c = q.getAnswer3() == null ? "" : q.getAnswer3();
                String d = q.getAnswer4() == null ? "" : q.getAnswer4();

                // Since A is always correct, we hardcode "1" into the CSV's last column
                // to keep the file schema consistent.
                String correctIndex = "1";

                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        id, text, diff, a, b, c, d, correctIndex);

                pw.println(line);
            }
            pw.flush();
        }
    }

    public static void readQuestionsFromCSV(String filePath) throws IOException {
        SysData sys = SysData.getInstance();
        sys.clearQuestions();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read first line. If it's a header, skip it.
            // If your file DOESN'T have a header, remove this block!
            if ((line = br.readLine()) != null) {
                // optional: check if line starts with "ID"
            }

            while ((line = br.readLine()) != null) {
                // FIX 1: Split by Tab (\t) OR Comma (,) to be safe
                String[] values = line.split("[\t,]", -1);

                // We verify we have enough columns (at least 7 or 8)
                if (values.length >= 7) {
                    try {
                        int id = Integer.parseInt(values[0].trim());
                        String text = values[1].trim();

                        // FIX 2: Handle "1", "2", "3" for Difficulty
                        QuestionDifficulty difficulty = QuestionDifficulty.EASY; // Default
                        String diffStr = values[2].trim();

                        if (diffStr.equals("1")) difficulty = QuestionDifficulty.EASY;
                        else if (diffStr.equals("2")) difficulty = QuestionDifficulty.MEDIUM;
                        else if (diffStr.equals("3")) difficulty = QuestionDifficulty.HARD;
                        else if (diffStr.equals("4")) difficulty = QuestionDifficulty.MASTER;
                        else {
                            // Try parsing text (e.g. "HARD") if it's not a number
                            try {
                                difficulty = QuestionDifficulty.valueOf(diffStr.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                System.err.println("Unknown difficulty: " + diffStr + ". Defaulting to EASY.");
                            }
                        }

                        // Map Answers
                        String ans1 = values[3].trim(); // A
                        String ans2 = values[4].trim(); // B
                        String ans3 = values[5].trim(); // C
                        String ans4 = values[6].trim(); // D

                        Question q = new Question(id, text, difficulty, ans1, ans2, ans3, ans4);
                        sys.addQuestion(q);

                    } catch (Exception e) {
                        System.err.println("Skipping malformed line: " + line + " Error: " + e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
    }
}