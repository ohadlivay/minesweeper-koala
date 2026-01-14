package main.java.util;

import main.java.model.Question;
import main.java.model.QuestionDifficulty;
import main.java.model.SysData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionCSVManager {

    private static final String CSV_HEADER = "ID,Question,Difficulty,A,B,C,D,Correct Answer";
    private static final String FILE_PATH = "Questions.csv";
    // ==========================================
    // WRITING LOGIC
    // ==========================================

    /*
    this is sort of a 'refresh' method.
    it will fetch all current questions from SysData and write them into a fresh csv (clean slate).
     */
    public static void rewriteQuestionsToCSVFromSysData() throws IOException {
        writeQuestionsToCSV(FILE_PATH);
    }

    public static void writeQuestionsToCSV(String filePath) throws IOException {
        SysData sys = SysData.getInstance();
        List<Question> questionsList = sys.getQuestions();

        try (FileWriter fw = new FileWriter(filePath);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println(CSV_HEADER);

            for (Question q : questionsList) {
                // HELPER 1: Delegate the formatting logic to a helper
                String line = convertQuestionToLine(q);
                pw.println(line);
            }
            pw.flush();
        }
    }

    /**
     * Helper: Converts a single Question object into a CSV formatted string.
     * Handles null checks and string formatting.
     * Now properly escapes/quotes fields that contain commas or quotes.
     */
    private static String convertQuestionToLine(Question q) {
        String id = String.valueOf(q.getId());
        String text = q.getQuestionText() == null ? "" : q.getQuestionText();
        String diff = q.getDifficulty() == null ? "EASY" : q.getDifficulty().toString();

        String a = q.getAnswer1() == null ? "" : q.getAnswer1();
        String b = q.getAnswer2() == null ? "" : q.getAnswer2();
        String c = q.getAnswer3() == null ? "" : q.getAnswer3();
        String d = q.getAnswer4() == null ? "" : q.getAnswer4();

        // Hardcoded "1" for correct index as per original logic
        String correctIndex = "1";

        // Escape/quote each field as needed
        return String.join(",",
                escapeCsvField(id),
                escapeCsvField(text),
                escapeCsvField(diff),
                escapeCsvField(a),
                escapeCsvField(b),
                escapeCsvField(c),
                escapeCsvField(d),
                escapeCsvField(correctIndex)
        );
    }

    /**
     * Escapes a single CSV field: doubles internal quotes and wraps in quotes
     * if the field contains comma, quote or newline.
     */
    private static String escapeCsvField(String field) {
        if (field == null) return "";
        boolean containsSpecial = field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r");
        if (containsSpecial) {
            String escaped = field.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
        } else {
            return field;
        }
    }

    // ==========================================
    // READING LOGIC
    // ==========================================

    public static void readQuestionsFromCSV(String filePath) throws IOException {
        SysData sys = SysData.getInstance();
        sys.clearQuestions();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip header if exists
            if ((line = br.readLine()) != null) {
                // Header skipped
            }

            while ((line = br.readLine()) != null) {
                try {
                    String[] values = parseCsvLine(line);

                    if (values.length >= 7) {
                        // HELPER 2: Delegate object creation to a helper
                        Question q = parseQuestionFromRow(values);
                        sys.addQuestion(q);
                    }
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line + " Error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
    }
    /**
     * Helper: Parses a raw string array into a Question object.
     * Includes safety checks for empty IDs.
     */
    private static Question parseQuestionFromRow(String[] values) {
        String idStr = values[0].trim();

        // FIX: Check if ID is empty before parsing
        if (idStr.isEmpty()) {
            throw new IllegalArgumentException("Row has missing ID");
        }

        int id = Integer.parseInt(idStr);
        String text = values[1].trim();

        QuestionDifficulty difficulty = parseDifficulty(values[2].trim());

        String ans1 = values[3].trim();
        String ans2 = values[4].trim();
        String ans3 = values[5].trim();
        String ans4 = values[6].trim();

        // Note: We ignore the 8th column (Correct Answer) for now
        // because the Question constructor doesn't take it.

        return new Question(id, text, difficulty, ans1, ans2, ans3, ans4);
    }

    /**
     * Helper: Handles the logic of converting numeric strings ("1", "2")
     * or text strings ("HARD") into the Enum.
     */
    private static QuestionDifficulty parseDifficulty(String diffStr) {
        switch (diffStr) {
            case "1": return QuestionDifficulty.EASY;
            case "2": return QuestionDifficulty.MEDIUM;
            case "3": return QuestionDifficulty.HARD;
            case "4": return QuestionDifficulty.MASTER;
            default:
                try {
                    return QuestionDifficulty.valueOf(diffStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown difficulty: " + diffStr + ". Defaulting to EASY.");
                    return QuestionDifficulty.EASY;
                }
        }
    }

    /**
     * Lightweight CSV parser for a single line. Supports quoted fields with commas and double-quote escaping.
     * Returns array of fields (may include empty strings).
     */
    private static String[] parseCsvLine(String line) {
        if (line == null || line.isEmpty()) return new String[0];
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    // look ahead for escaped quote
                    if (i + 1 < len && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip next quote
                    } else {
                        inQuotes = false; // end quote
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    fields.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        fields.add(cur.toString());
        return fields.toArray(new String[0]);
    }
}