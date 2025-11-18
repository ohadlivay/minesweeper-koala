import main.java.view.DummyScreenForJar;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
        System.out.println("Bye!");
        System.out.println("Yes");
        System.out.println("No");
        System.out.println("ohad was here");
        System.out.println("Hola mundo, mi nombre es Tom");
        System.out.println("Liran");

        DummyScreenForJar ds4j = new DummyScreenForJar();
        ds4j.setVisible(true);
    }
}
