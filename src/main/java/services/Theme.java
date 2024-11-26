package services;

/**
 * The Theme class provides methods to manage the application's theme.
 */
public class Theme {
    private static final String DARK = "/stylesdark.css";
    private static final String LIGHT = "/styles.css";
    private static String themeOfApp = LIGHT;

    /**
     * Gets the current theme of the application.
     *
     * @return the current theme as a string
     */
    public static String getTheme() {
        return themeOfApp;
    }

    /**
     * Switches the theme of the application between light and dark modes.
     */
    public static void switchTheme() {
        if (themeOfApp.equals(LIGHT)) {
            themeOfApp = DARK;
        } else {
            themeOfApp = LIGHT;
        }
    }
}