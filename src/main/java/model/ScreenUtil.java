package model;

import javafx.stage.Screen;

public class ScreenUtil {

    public static double getScaledHeight() {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        return screenHeight * 0.9;
    }
}
