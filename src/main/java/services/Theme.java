package services;

public class Theme {
    private static final String DARK = "/stylesdark.css";
    private static final String LIGHT = "/styles.css";
    private static String DEFAULT = LIGHT;

    public static String getTheme() {
        return DEFAULT;
    }

    public static void switchTheme() {
        if (DEFAULT.equals(LIGHT)) {
            DEFAULT = DARK;
        } else {
            DEFAULT = LIGHT;
        }
    }

}
