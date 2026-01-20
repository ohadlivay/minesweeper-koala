package main.java.view;

import java.awt.*;
import java.util.Random;


//color handling is done here
public enum ColorsInUse {

    BG_COLOR(new Color(20, 20, 20)),
    BG_COLOR_TRANSPARENT(new Color(20, 20, 20, 86)),
    BG_BLACK(new Color(4,3,4)),
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
    QUESTION_TILE_BREATH(new Color(4, 65, 5, 255)),


    NUMBER_1(Color.BLUE),
    NUMBER_2(new Color(0, 128, 0)),
    NUMBER_3(Color.RED),
    NUMBER_4(new Color(93, 33, 196)),
    NUMBER_5(new Color(38, 8, 8)),
    NUMBER_6(Color.CYAN),
    NUMBER_7(Color.BLACK),
    NUMBER_8(Color.GRAY),


    // Board colors
    CRIMSON         (new Color(184, 15, 10)),
    SUNSET_ORANGE   (new Color(255, 94, 0)),
    BROWN      (new Color(64, 34, 3)),
    ROSE_GOLD       (new Color(183, 110, 121)),
    TERRA_COTTA     (new Color(204, 78, 92)),
    WINE   (new Color(89, 15, 12)),
    SOFT_MAROON     (new Color(150, 85, 95)),
    WARM_TAUPE      (new Color(150, 130, 110)),
    CLAY_ORANGE     (new Color(190, 135, 85)),
    COPPER_RED      (new Color(180, 105, 85)),
    PEACH           (new Color(235, 92, 52)),

    // --- Cool Colors (Board B) ---
    MIDNIGHT_BLUE   (new Color(25, 25, 112)),
    ARCTIC_CYAN     (new Color(0, 191, 255)),
    EMERALD_GREEN   (new Color(46, 139, 87)),
    ROYAL_PURPLE    (new Color(61, 71, 212)),
    FROST_BLUE      (new Color(83, 245, 237)),
    PINE_GREEN      (new Color(1, 121, 111)),
    SLATE_BLUE      (new Color(90, 110, 160)),
    STEEL_TEAL      (new Color(70, 140, 135)),
    MUTED_CYAN      (new Color(80, 150, 170)),
    DENIM_BLUE      (new Color(75, 120, 180)),
    SAGE_GREEN      (new Color(140, 170, 130)),
    PINE_TEAL       (new Color(80, 130, 120));

    // Warm colors – Board 1
    private static final ColorsInUse[] BOARD_COLORS_WARM = {
            CRIMSON,
            SUNSET_ORANGE,
            BROWN,
            ROSE_GOLD,
            TERRA_COTTA,
            WINE,
            SOFT_MAROON,
            WARM_TAUPE,
            CLAY_ORANGE,
            COPPER_RED,
            PEACH
    };

    // Cold colors – Board 2
    private static final ColorsInUse[] BOARD_COLORS_COLD = {
            MIDNIGHT_BLUE,
            ARCTIC_CYAN,
            EMERALD_GREEN,
            ROYAL_PURPLE,
            FROST_BLUE,
            PINE_GREEN,
            SLATE_BLUE,
            STEEL_TEAL,
            MUTED_CYAN,
            DENIM_BLUE,
            SAGE_GREEN,
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

    public static ColorsInUse[] getWarmBoardColors() {
        return BOARD_COLORS_WARM;
    }
}