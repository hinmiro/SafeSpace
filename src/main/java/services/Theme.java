package services;

public class Theme {
    private static final String DARK = "/stylesdark.css";
    private static final String LIGHT = "/styles.css";
    private static String THEME = LIGHT;

    public static String getTheme() {
        return THEME;
    }

    public static void switchTheme() {
        if (THEME.equals(LIGHT)) {
            THEME = DARK;
        } else {
            THEME = LIGHT;
        }
    }
}
