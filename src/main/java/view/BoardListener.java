package main.java.view;


/* Interface for listening to board events in the application.
 * The interface will be implemented by classes that need to respond to board related actions:
 * tile revealed, tile flagged, tile activated, cascade, mine revealed, board locked/unlocked
 */
public interface BoardListener {
    void update();
}
