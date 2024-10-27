package model;

import java.util.Locale;

public enum Language {
    FI(new Locale("fi", "FI")),
    EN(new Locale("en", "US")),
    JP(new Locale("ja", "JP"));

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getDisplayName() {
        return locale.getDisplayLanguage(locale);
    }
}
