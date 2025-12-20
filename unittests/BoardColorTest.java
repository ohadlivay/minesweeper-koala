import main.java.controller.OverlayController;
import main.java.model.SysData;
import main.java.view.ColorsInUse;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
this test checks the following:
- that the returned Color is from the predefined set of board colors
- that multiple calls to randomBoardColor produce different colors, indicating randomness
 */
public class BoardColorTest {

    @Test
    public void randomBoardColor() throws NoSuchFieldException, IllegalAccessException {
        //access the private list of board colors
        Field boardColors = ColorsInUse.class.getDeclaredField("BOARD_COLORS");
        boardColors.setAccessible(true);
        ColorsInUse[] allowedValues = (ColorsInUse[]) boardColors.get(null);
        //build a set of colors from the enum
        Set<Color> allowedColors = new HashSet<>();
        for (ColorsInUse colorsInUse : allowedValues)
            allowedColors.add(colorsInUse.get());
        Set<Color> returnedColors = new HashSet<>();
        //we run the method multiple times to ensure randomness
        for (int i = 0; i < 50; i++) {
            Color color = ColorsInUse.randomBoardColor();
            assertTrue("Color should be from the allowed set", allowedColors.contains(color));
            returnedColors.add(color);
        }
        assertTrue("the method returns different colors over multiple calls", returnedColors.size() > 1);

    }

}

