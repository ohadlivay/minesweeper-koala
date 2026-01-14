import main.java.model.Question;
import main.java.model.QuestionDifficulty;
import main.java.model.SysData;
import main.java.util.QuestionCSVManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class QuestionCSVManagerTest {

    private static final String TEST_FILE_PATH = "test_questions.csv";
    private SysData sysData;

    @Before
    public void setUp() {
        // Get the singleton instance and clear it before every test
        sysData = SysData.getInstance();
        sysData.clearQuestions();
    }

    @After
    public void tearDown() {
        // Clean up the temporary test file after every test
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        sysData.clearQuestions();
    }

    @Test
    public void testWriteAndReadHappyPath() throws IOException {
        // 1. Prepare Data
        Question q1 = new Question(1, "What is 1+1?", QuestionDifficulty.EASY, "2", "3", "4", "5");
        Question q2 = new Question(2, "Capital of France?", QuestionDifficulty.MEDIUM, "Paris", "London", "Berlin", "Madrid");

        sysData.addQuestion(q1);
        sysData.addQuestion(q2);

        // 2. Write to CSV
        QuestionCSVManager.writeQuestionsToCSV(TEST_FILE_PATH);

        // 3. Clear Memory
        sysData.clearQuestions();
        Assert.assertEquals("SysData should be empty before reading.", 0, sysData.getQuestions().size());

        // 4. Read from CSV
        QuestionCSVManager.readQuestionsFromCSV(TEST_FILE_PATH);

        // 5. Verify Data
        List<Question> loadedQuestions = sysData.getQuestions();
        Assert.assertEquals("Should have loaded exactly 2 questions.", 2, loadedQuestions.size());

        // Verify Question 1
        Question loadedQ1 = loadedQuestions.get(0);
        Assert.assertEquals(1, loadedQ1.getId());
        Assert.assertEquals("What is 1+1?", loadedQ1.getQuestionText());
        Assert.assertEquals(QuestionDifficulty.EASY, loadedQ1.getDifficulty());
        Assert.assertEquals("2", loadedQ1.getAnswer1()); // A (Correct)

        // Verify Question 2
        Question loadedQ2 = loadedQuestions.get(1);
        Assert.assertEquals("Paris", loadedQ2.getAnswer1()); // A (Correct)
    }

    @Test
    public void testReadWithMalformedLine() throws IOException {
        // 1. Create a CSV file manually with one valid line and one broken line
        FileWriter fw = new FileWriter(TEST_FILE_PATH);
        fw.write("ID,Question,Difficulty,A,B,C,D,Correct Answer\n"); // Header
        fw.write("1,Valid Q,EASY,A,B,C,D,1\n"); // Valid
        fw.write("BrokenLine,With,Missing,Columns\n"); // Malformed
        fw.write("2,Another Valid,HARD,X,Y,Z,W,1\n"); // Valid
        fw.close();

        // 2. Read
        QuestionCSVManager.readQuestionsFromCSV(TEST_FILE_PATH);

        // 3. Verify
        // The manager should skip the broken line but load the valid ones.
        Assert.assertEquals("Should skip malformed lines and load valid ones.", 2, sysData.getQuestions().size());
        Assert.assertEquals(1, sysData.getQuestions().get(0).getId());
        Assert.assertEquals(2, sysData.getQuestions().get(1).getId());
    }

    @Test
    public void testFileNotFound() throws IOException {
        // Ensure the file does not exist
        File file = new File("non_existent_file.csv");
        if(file.exists()) file.delete();

        // Execution should proceed without throwing an exception
        QuestionCSVManager.readQuestionsFromCSV("non_existent_file.csv");

        Assert.assertTrue(sysData.getQuestions().isEmpty());
    }

    //deprecated: new logic was implemented: nulls are not allowed. so this test is gone.
//    @Test
//    public void testWriteHandlesNullsGracefully() throws IOException {
//        // Create a question with null fields
//        Question qNulls = new Question(10, null, null, null, null, null, null);
//
//        sysData.addQuestion(qNulls);
//
//        // Write (Implicit check: if this throws an exception, the test fails)
//        QuestionCSVManager.writeQuestionsToCSV(TEST_FILE_PATH);
//
//        // Read back to verify it became empty strings
//        sysData.clearQuestions();
//        QuestionCSVManager.readQuestionsFromCSV(TEST_FILE_PATH);
//
//        Question loaded = sysData.getQuestions().get(0);
//        Assert.assertEquals("", loaded.getQuestionText());
//        Assert.assertEquals("", loaded.getAnswer1());
//    }

    @Test
    public void testReadRealDataFormat() throws IOException {
        // 1. Create a file mimicking REAL data (Tab-separated, Difficulty "3")
        // Data: 1 [TAB] Question [TAB] 3 [TAB] Ans1 [TAB] Ans2 [TAB] Ans3 [TAB] Ans4 [TAB] A
        FileWriter fw = new FileWriter(TEST_FILE_PATH);
        fw.write("ID\tQuestion\tDifficulty\tA\tB\tC\tD\tCorrect Answer\n"); // Header with tabs
        fw.write("1\tHow do software maintenance costs compare to development?\t3\tOften higher than development\tNegligible\tAlways equal\tAbout half\tA\n");
        fw.close();

        // 2. Read
        QuestionCSVManager.readQuestionsFromCSV(TEST_FILE_PATH);

        // 3. Verify
        List<Question> questions = sysData.getQuestions();
        Assert.assertEquals("Should load 1 question", 1, questions.size());

        Question q = questions.get(0);

        // Verify ID
        Assert.assertEquals(1, q.getId());

        // Verify Text
        Assert.assertEquals("How do software maintenance costs compare to development?", q.getQuestionText());

        // Verify Difficulty Conversion (3 -> HARD)
        Assert.assertEquals("Difficulty '3' should be parsed as HARD", QuestionDifficulty.HARD, q.getDifficulty());

        // Verify Answers
        Assert.assertEquals("Often higher than development", q.getAnswer1()); // Answer A
        Assert.assertEquals("Negligible", q.getAnswer2());               // Answer B
    }
}