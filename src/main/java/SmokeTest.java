package main.java;

//SMOKE TESTS FOR MAIN FUNCTIONALITIES
public class SmokeTest {

    public static boolean runAllTests(String[] args) {
        boolean testsPassed = true;

        // TEST 1: System startup
        try {
            boolean startUpCheck = testSystemStartup();
            if (startUpCheck) {
                System.out.println("TEST 1: System startup || PASSED");
            } else {
                System.out.println("TEST 1: System startup || FAILED");
                testsPassed = false;
            }
        } catch (Exception e){
            System.out.println("Exception: ");
            e.printStackTrace(System.out);
            testsPassed = false;
        }

        System.out.println("COMPLETED ALL TESTS");
        return testsPassed;
        }
        //ACTUAL TESTS HERE
        private static boolean testSystemStartup() {
            try {
                Main.launchSystem();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        //ADD TEST FOR BOARD INITIALIZATION
        //ADD TEST FOR TILE DISTRIBUTION
        //ADD TEST FOR CSV
}
