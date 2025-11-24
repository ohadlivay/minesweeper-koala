package main.java.view;
/* Interface for listening to system data events in the application.
 * The interface will be implemented by classes that need to respond to system data related actions:
 * game session entry added, history loaded, question management
 */
public interface SysDataListener {
    void update();
}
