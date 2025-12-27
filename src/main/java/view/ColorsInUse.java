package main.java.view;

import java.awt.Color;
import java.util.Random;


//color handling is done here
public enum ColorsInUse {

    BG_COLOR(new Color(20, 20, 20)),
    BTN_COLOR(new Color(10, 10, 10)),
    TEXT(Color.WHITE),
    ALT_TEXT(Color.BLACK),
    INPUT_FIELD(Color.BLACK),
    CONFIRM(new Color(25, 135, 84)),
    DENY(new Color(220, 53, 69)),
    TEXT_BOX_BORDER(new Color(91, 90, 90, 200)),
    PLACEHOLDER_TEXT(new Color(255, 255, 255, 137)),

    BOARD_BACKGROUND(new Color(32, 32, 32)),
    BOARD_BORDER_DEFAULT(new Color(0, 0, 0, 150)),
    BOARD_ACTIVE_BORDER(new Color(255, 215, 0, 200)),
    BOARD_ACTIVE_BORDER2(new Color(133,235,217)),

    FEEDBACK_GOOD_COLOR(new Color(46, 204, 113)),
    FEEDBACK_BAD_COLOR(new Color(231, 76, 60)),

    TILE_DEFAULT(Color.DARK_GRAY),
    REVEALED_BG(Color.BLACK),

    SURPRISE_TILE(Color.YELLOW), // 'S' tile
    QUESTION_TILE(Color.GREEN),  // 'Q' tile

    NUMBER_1(Color.BLUE),
    NUMBER_2(new Color(0, 128, 0)),
    NUMBER_3(Color.RED),
    NUMBER_4(new Color(0, 0, 128)),
    NUMBER_5(new Color(128, 0, 0)),
    NUMBER_6(Color.CYAN),
    NUMBER_7(Color.BLACK),
    NUMBER_8(Color.GRAY),

    POINTS(new Color(88, 124, 196)),

    // Board colors
    SLATE_BLUE      (new Color(90, 110, 160)),
    STEEL_TEAL      (new Color(70, 140, 135)),
    DUSTY_PURPLE    (new Color(140, 110, 155)),
    MUTED_CYAN      (new Color(80, 150, 170)),
    OLIVE_GREEN     (new Color(125, 150, 85)),
    SOFT_MAROON     (new Color(150, 85, 95)),
    WARM_TAUPE      (new Color(150, 130, 110)),
    DENIM_BLUE      (new Color(75, 120, 180)),
    SAGE_GREEN      (new Color(140, 170, 130)),
    CLAY_ORANGE     (new Color(190, 135, 85)),

    DESAT_TEAL      (new Color(90, 160, 150)),
    ASH_PURPLE      (new Color(155, 140, 170)),
    SMOKY_BLUE      (new Color(110, 135, 170)),
    MOSS_GREEN      (new Color(115, 155, 100)),
    COPPER_RED      (new Color(180, 105, 85)),
    SANDSTONE       (new Color(189, 160, 100)),
    BLUE_GRAY       (new Color(110, 130, 150)),
    ROSEWOOD        (new Color(165, 100, 110)),
    MUTED_INDIGO    (new Color(100, 110, 165)),
    PINE_TEAL       (new Color(80, 130, 120));

    // Warm colors – Board 1
    private static final ColorsInUse[] BOARD_COLORS_WARM = {
            SOFT_MAROON,
            WARM_TAUPE,
            CLAY_ORANGE,
            COPPER_RED,
            SANDSTONE,
            ROSEWOOD,
            OLIVE_GREEN,
            MOSS_GREEN
    };

    // Cold colors – Board 2
    private static final ColorsInUse[] BOARD_COLORS_COLD = {
            SLATE_BLUE,
            STEEL_TEAL,
            MUTED_CYAN,
            DENIM_BLUE,
            SAGE_GREEN,
            DESAT_TEAL,
            ASH_PURPLE,
            SMOKY_BLUE,
            BLUE_GRAY,
            MUTED_INDIGO,
            PINE_TEAL
    };

    private final Color color;

    ColorsInUse(Color color) {
        this.color = color;
    }

    public Color get() {
        return color;
    }

    public static ColorsInUse getRandomWarmBoardColor() {
        return BOARD_COLORS_WARM[
                new Random().nextInt(BOARD_COLORS_WARM.length)
                ];
    }

    public static ColorsInUse getRandomColdBoardColor() {
        return BOARD_COLORS_COLD[
                new Random().nextInt(BOARD_COLORS_COLD.length)
                ];
    }


    public static Color getBoardBorderColor(boolean turn) {
        if (turn)
            return BOARD_ACTIVE_BORDER.get();
        else
            return BOARD_BORDER_DEFAULT.get();
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