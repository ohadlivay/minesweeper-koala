package main.java.model;

public class TileFactory {

    /**
     * FACTORY MAGIC: This method acts as the single point of creation.
     * The client (BoardGenerator) provides a blueprint symbol, and this
     * factory "magically" transforms it into the correct object type.
     */
    public static Tile createTile(String blueprintValue) {
        // MAGIC STEP: Mapping a String representation to a Concrete Class
        switch (blueprintValue) {
            case "MINE":
            case "M": // Supporting both full names and shorthand from your blueprint
                return new MineTile();

            case "QUESTION":
            case "Q":
                return new QuestionTile();

            case "SURPRISE":
            case "S":
                return new SurpriseTile();

            default:
                // MAGIC STEP: Handling dynamic data (Numbers 0-8)
                // The factory handles the internal configuration (setAdjacentMines)
                // so the caller doesn't have to worry about NumberTile's specific methods.
                try {
                    int mineCount = Integer.parseInt(blueprintValue);
                    NumberTile nt = new NumberTile();
                    nt.setAdjacentMines(mineCount);
                    return nt;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Factory Error: Unknown blueprint value " + blueprintValue);
                }
        }
    }
}