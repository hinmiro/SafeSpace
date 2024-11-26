package model;

import javafx.stage.Screen;

public class ScreenUtil {

    /**
     * Gets the scaled height of the primary screen.
     *
     * @return the scaled height, which is 90% of the primary screen height
     */
    public static double getScaledHeight() {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        return screenHeight * 0.9;
    }
}