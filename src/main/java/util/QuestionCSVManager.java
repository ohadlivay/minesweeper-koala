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

            if ((line = br.readLine()) != null) {
                // Header check (optional)
            } else {
                return;
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);

                if (values.length == 8) {
                    try {
                        int id = Integer.parseInt(values[0].trim());
                        String text = values[1].trim();

                        QuestionDifficulty difficulty = QuestionDifficulty.EASY;
                        String diffField = values[2].trim();
                        if (!diffField.isEmpty()) {
                            difficulty = QuestionDifficulty.valueOf(diffField.toUpperCase());
                        }

                        // Map A, B, C, D directly to variables
                        String ans1 = values[3].trim(); // A -> Answer1 (Correct)
                        String ans2 = values[4].trim(); // B -> Answer2
                        String ans3 = values[5].trim(); // C -> Answer3
                        String ans4 = values[6].trim(); // D -> Answer4

                        // Note: We ignore values[7] (Correct Answer column)
                        // because logic dictates ans1 is always correct.

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