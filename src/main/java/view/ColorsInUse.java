package main.java.view;

import java.awt.Color;

public enum ColorsInUse {

    BG_COLOR(new Color(20, 20, 20)),
    BTN_COLOR(new Color(10, 10, 10)),
    TEXT(Color.WHITE),
    INPUT_FIELD(Color.BLACK),
    CONFIRM(new Color(25, 135, 84)),
    DENY(new Color(220, 53, 69)),

    BOARD_BACKGROUND(new Color(32, 32, 32)),
    BOARD_BORDER_DEFAULT(new Color(0, 0, 0, 150)),
    BOARD_ACTIVE_BORDER(new Color(255, 215, 0, 200)),

    TILE_DEFAULT(Color.DARK_GRAY),
    REVEALED_BG(Color.BLACK),

    SURPRISE_TILE(Color.YELLOW), // 'S' tile
    QUESTION_TILE(Color.GREEN),  // 'Q' tile

    NUMBER_1(Color.RED),
    NUMBER_2(new Color(0, 128, 0)),
    NUMBER_3(Color.BLUE),
    NUMBER_4(Color.MAGENTA),
    NUMBER_5(Color.ORANGE),
    NUMBER_6(Color.CYAN),
    NUMBER_7(Color.PINK),
    NUMBER_8(Color.GRAY),

    POINTS(new Color(88, 124, 196));


    private final Color color;

    ColorsInUse(Color color) {
        this.color = color;
    }

    public Color get() {
        return color;
    }

    public static Color numberColor(String n) {
        switch (n) {
            case "1": return NUMBER_1.get();
            case "2": return NUMBER_2.get();
            case "3": return NUMBER_3.get();
            case "4": return NUMBER_4.get();
            case "5": return NUMBER_5.get();
            case "6": return NUMBER_6.get();
            case "7": return NUMBER_7.get();
            case "8": return NUMBER_8.get();
            default: return TILE_DEFAULT.get();
        }
    }

    public static Color forTileType(String type) {
        if (type == null) return TILE_DEFAULT.get();
        switch (type) {
            case "S": return SURPRISE_TILE.get();
            case "Q": return QUESTION_TILE.get();
            case "0": // revealed empty
            case "M": // mine (use revealed bg)
            default:  return REVEALED_BG.get();
        }
    }
}