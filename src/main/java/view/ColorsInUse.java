package main.java.view;

import java.awt.*;
import java.util.Random;


//color handling is done here
public enum ColorsInUse {

    BG_COLOR(new Color(20, 20, 20)),
    BG_COLOR_TRANSPARENT(new Color(20, 20, 20, 86)),
    TABLE_BG_COLOR(Color.BLACK),
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
    BOARD_ACTIVE_BORDER2(new Color(142, 110, 157)),

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
    NUMBER_4(Color.MAGENTA),
    NUMBER_5(new Color(255, 94, 0)),
    NUMBER_6(new Color(93, 33, 196)),
    NUMBER_7(new Color(38, 8, 8)),
    NUMBER_8(Color.BLACK),


    // Board colors
    CRIMSON         (new Color(184, 15, 10)),
    SUNSET_ORANGE   (new Color(255, 94, 0)),
    WINE   (new Color(89, 15, 12)),
    CLAY_ORANGE     (new Color(190, 135, 85)),
    PEACH           (new Color(235, 92, 52)),

    MIDNIGHT_BLUE   (new Color(25, 25, 112)),
    ARCTIC_CYAN     (new Color(0, 191, 255)),
    EMERALD_GREEN   (new Color(46, 139, 87)),
    ROYAL_PURPLE    (new Color(61, 71, 212));

    // Outline colors for difficulty (reusable static Colors so enum ordering is unaffected)
    public static final Color DIFFICULTY_EASY_OUTLINE   = new Color(25, 135, 84);   // green
    public static final Color DIFFICULTY_MEDIUM_OUTLINE = new Color(162, 93, 0);  // muted yellow-orange
    public static final Color DIFFICULTY_HARD_OUTLINE   = new Color(220, 53, 69);   // red
    public static final Color DIFFICULTY_MASTER_OUTLINE   = new Color(77, 4, 94);   // dark purple

    // Single mixed board palette (up to 8 muted, medium-toned colors)
    private static final ColorsInUse[] BOARD_COLORS_MIXED = {
            ARCTIC_CYAN,
            EMERALD_GREEN,
            CRIMSON,
            ROYAL_PURPLE,
            WINE,
            CLAY_ORANGE,
            PEACH,
            MIDNIGHT_BLUE
    };

    private final Color color;

    ColorsInUse(Color color) {
        this.color = color;
    }

    public Color get() {
        return color;
    }

    // Primary API: random color from the single mixed palette
    public static ColorsInUse getRandomBoardColor() {
        return BOARD_COLORS_MIXED[new Random().nextInt(BOARD_COLORS_MIXED.length)];
    }

    // Backwards-compatible methods mapped to the mixed palette
    public static ColorsInUse getRandomWarmBoardColor() {
        return getRandomBoardColor();
    }

    public static ColorsInUse getRandomColdBoardColor() {
        return getRandomBoardColor();
    }

    public static ColorsInUse[] getBoardColors() {
        return BOARD_COLORS_MIXED;
    }

    /**
     * Return an outline color for a difficulty level.
     * level: 0 = easy (green), 1 = medium (yellow-orange), 2 = hard (red).
     * Any other value returns the medium color as default.
     */
    public static Color getDifficultyOutlineColor(int level) {
        switch (level) {
            case 0: return DIFFICULTY_EASY_OUTLINE;
            case 1: return DIFFICULTY_MEDIUM_OUTLINE;
            case 2: return DIFFICULTY_HARD_OUTLINE;
            default: return DIFFICULTY_MEDIUM_OUTLINE;
        }
    }

    public static Color getBoardBorderColor(boolean turn) {
        if (turn)
            return BOARD_ACTIVE_BORDER.get();
        else
            return BOARD_BORDER_DEFAULT.get();
    }
}