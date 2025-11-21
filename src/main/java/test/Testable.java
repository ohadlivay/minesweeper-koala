package main.java.test;

public interface Testable {
    /*
    each class will implement this interace such that we can test them all at startup.
    if we made changes to our code and we broke something elsewhere, this will be a line of defence.
    it doesnt require inputs , returns if ALL have succeded.
     */
    boolean runClassTests();
}
