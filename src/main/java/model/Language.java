package model;

import java.util.Locale;

public enum Language {
    FI("Finnish", new Locale("fi", "FI")),
    EN("English", new Locale("en", "US")),
    JP("Japanese", new Locale("ja", "JP"));

    private final String displayName;
    private final Locale locale;

    Language(String displayName, Locale locale) {
        this.displayName = displayName;
        this.locale = locale;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Locale getLocale() {
        return locale;
    }
}
