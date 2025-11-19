package main.java;

import main.java.view.DummyScreenForJar;
import main.java.SmokeTest;

public class Main {
    public static void main(String[] args) {
        try {
            SmokeTest.runAllTests(args);
        } catch (Exception e) {
            System.out.println("Tests failed:");
            e.printStackTrace();
            return;
        }
        launchSystem();
    }

        public static void launchSystem() {
            DummyScreenForJar ds4j = new DummyScreenForJar();
            ds4j.setVisible(true);
        }
    }
