package main.java.model;

public class TileFactory {

    /**
     * FACTORY Design pattern
     * The client (BoardGenerator) provides a blueprint symbol, and this
     * factory transforms it into the correct object type.
     */
    public static Tile createTile(String blueprintValue) {
        switch (blueprintValue) {
            case "MINE":
            case "M":
                return new MineTile();

            case "QUESTION":
            case "Q":
                return new QuestionTile();

            case "SURPRISE":
            case "S":
                return new SurpriseTile();

            default:
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