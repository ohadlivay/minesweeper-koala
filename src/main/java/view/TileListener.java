package main.java.view;


/* Interface for listening to tile events in the application.
 * The interface will be implemented by classes that need to respond to tile related actions:
 * tile revealed, tile flagged
 */
public interface TileListener {
    void update();
}
