package main.java.model;

/**
 * SpecialTile serves as an abstract base class for number tiles that have special properties or effects.
 * It manages the "used" state and notifies an activation listener when triggered.
 */
public abstract class SpecialTile extends NumberTile {

    // ==================================================================================
    // FIELDS
    // ==================================================================================

    // Indicates whether the tile has been initiated or not
    protected boolean isUsed;
    
    // Listener for special tile activation events
    private SpecialTileActivationListener listener;


    // ==================================================================================
    // CONSTRUCTOR
    // ==================================================================================

    /**
     * Constructs a SpecialTile.
     * Initializes the tile as unused.
     */
    public SpecialTile() {
        super();
        this.isUsed = false;
    }

    // ==================================================================================
    // ACCESSORS (GETTERS & SETTERS)
    // ==================================================================================

    /**
     * Checks if the special effect has already been triggered.
     * @return true if used, false otherwise.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Marks the special tile as used and triggers the activation listener.
     */
    protected void setUsed() {
        isUsed = true;
        if (this.listener != null) {
            listener.onSpecialTileActivated();
        }
    }

    /**
     * Registries a listener to handle the activation event of this special tile.
     * @param specialTileActivationListener The listener to add.
     */
    public void setSpecialTileActivationListener(SpecialTileActivationListener specialTileActivationListener) {
        listener = specialTileActivationListener;
    }

    // ==================================================================================
    // OVERRIDES (TESTABLE)
    // ==================================================================================

    // Class tests
    public boolean runClassTests() {
        return true;
    }
}
