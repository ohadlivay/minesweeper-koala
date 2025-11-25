package main.java.view;
/* Interface for listening to game session events in the application.
 * The interface will be implemented by classes that need to respond to logical updates in the game state:
 * score updates, lives updates, game end, turn change
 */
public interface GameSessionListener {
    void update();
}
