package main.java;

//SMOKE TESTS FOR MAIN FUNCTIONALITIES
public class SmokeTest {

    public static void main(String[] args) {
        boolean testsPassed = true;

        // TEST 1: System startup
        try {
            Main.main(args);
            System.out.println("TEST 1: System startup || PASSED");
        } catch (Exception e) {
            System.out.println("TEST 1: System startup || FAILED");
            testsPassed = false;
        }

        if (testsPassed) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println("TESTS FAILED"); //need to add specification
        }
    }
}
