package main.java.model;

// --- FACTORY DESIGN PATTERN ---

public class TileFactory {
    public static Tile createTile(String type) {
        switch (type) {
            case "M":
                return new MineTile();
            case "Q":
                return new QuestionTile();
            case "S":
                return new SurpriseTile();
            default:
                NumberTile nt = new NumberTile();
                nt.setAdjacentMines(Integer.parseInt(type));
                return nt;
        }
    }
}