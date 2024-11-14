package model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LanguageTest {
    @Test
    public void testGetLocale() {
        for (Language lang : Language.values()) {
            Locale expectedLocale = lang.getLocale();
            assertEquals("Locale for " + lang, expectedLocale, lang.getLocale());
        }
    }

    @Test
    public void testGetDisplayName() {
        for (Language lang : Language.values()) {
            String expectedDisplayName = lang.getDisplayName();
            assertEquals("Display name for " + lang, expectedDisplayName, lang.getDisplayName());
        }
    }
}
