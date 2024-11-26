package model;

import java.util.Locale;

/**
 * The Language enum represents different languages supported by the application.
 * Each language is associated with a specific locale.
 */
public enum Language {
    FI(new Locale("fi", "FI")),
    EN(new Locale("en", "US")),
    JP(new Locale("ja", "JP")),
    RU(new Locale("ru", "RU"));

    private final Locale locale;

    /**
     * Constructs a Language enum with the specified locale.
     *
     * @param locale the locale associated with the language
     */
    Language(Locale locale) {
        this.locale = locale;
    }

    /**
     * Gets the locale associated with the language.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets the display name of the language in its own locale.
     *
     * @return the display name of the language
     */
    public String getDisplayName() {
        return locale.getDisplayLanguage(locale);
    }
}