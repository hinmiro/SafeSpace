package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Locale;

public class LanguageTest {
    Language language;

    @Before
    public void setUp() {
        language = Language.EN;
    }

    @Test
    public void getLocale() {
        assertEquals("Should return Locale.ENGLISH", Locale.ENGLISH, language.getLocale());
    }

    @Test
    public void getDisplayName() {
        assertEquals("Should return 'English'", "English", language.getDisplayName());
    }

    @After
    public void tearDown() {
        language = null;
    }
}
