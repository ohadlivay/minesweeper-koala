package main.java.model;
/* this is an Observer design pattern. its like client-server, where the server is the Observer.
the Observer gets notified when the Subject (the client) performs any state change.

we will use this pattern in usecases such as: boards give updates to gamesession.
 */
public interface Observable {
    void update(int newValue);
}
