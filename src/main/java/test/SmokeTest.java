package main.java.test;

import main.java.controller.NavigationController;

import javax.swing.*;

//SMOKE TESTS FOR MAIN FUNCTIONALITIES
public class SmokeTest {

    public static boolean runAllTests(String[] args) {
        boolean testsPassed = true;

        /* TEST 1: System startup
           What we actually test is the initialization of NavigationController and the StartScreen
           as these are the main components that need to work for the system to start up correctly.
         */
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
        JFrame testScreen = null;
        try {
            testScreen = new JFrame("Test");
            NavigationController nav = new NavigationController(testScreen);
            nav.goToHome();
            testScreen.dispose();
            return true;
        } catch (Exception e) {
            System.err.println("Startup Test Failed: " + e.getMessage());
            if (testScreen != null) {
                testScreen.dispose();
            }
            return false;
        }
    }

    //ADD TEST FOR BOARD INITIALIZATION
    //ADD TEST FOR TILE DISTRIBUTION
    //ADD TEST FOR CSV
}
