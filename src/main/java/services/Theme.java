package services;

// Theme class

public class Theme {
    private static final String DARK = "/stylesdark.css";
    private static final String LIGHT = "/styles.css";
    private static String themeOfApp = LIGHT;

    public static String getTheme() {
        return themeOfApp;
    }

    public static void switchTheme() {
        if (themeOfApp.equals(LIGHT)) {
            themeOfApp = DARK;
        } else {
            themeOfApp = LIGHT;
        }
    }
}
