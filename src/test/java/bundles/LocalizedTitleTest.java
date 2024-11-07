package bundles;

import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalizedTitleTest {
    private final Locale localeEN = new Locale("en", "US");
    private final Locale localeFI = new Locale("fi", "FI");
    private final Locale localeJA = new Locale("ja", "JP");
    private final Locale localeRU = new Locale("ru", "RU");

    private final ResourceBundle titlesFI = ResourceBundle.getBundle("PageTitles", localeFI);
    private final ResourceBundle titlesEN = ResourceBundle.getBundle("PageTitles", localeEN);
    private final ResourceBundle titlesJA = ResourceBundle.getBundle("PageTitles", localeJA);
    private final ResourceBundle titlesRU = ResourceBundle.getBundle("PageTitles", localeRU);

    @Test
    public void testEnglishTitles() {
        String[] titles = {
                "login", "register", "main", "profile", "messages", "editprofile", "updateinfo", "userProfile"
        };

        String[] expected = {
                "Login", "Register", "Main Page", "Profile Page", "Messages", "Edit Profile", "Update Password", "User Profile Page"
        };

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            assertTrue("Missing required key in English: " + title, titlesEN.containsKey(title));
            assertEquals("Value for key '" + title + "' does not match the expected value.", expected[i], titlesEN.getString(title));
        }

    }

    @Test
    public void testFinnishTitles() {
        String[] titles = {
                "login", "register", "main", "profile", "messages", "editprofile", "updateinfo", "userProfile"
        };

        String[] expected = {
                "Kirjaudu", "Rekisteröidy", "Etusivu", "Profiili", "Viestit", "Muokkaa profiilia", "Muokkaa salasanaa", "Käyttäjän profiili"
        };

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            assertTrue("Missing required key in Finnish: " + title, titlesFI.containsKey(title));
            assertEquals("Value for key '" + title + "' does not match the expected value.", expected[i], titlesFI.getString(title));
        }
    }

    @Test
    public void testJapaneseTitles() {
        String[] titles = {
                "login", "register", "main", "profile", "messages", "editprofile", "updateinfo", "userProfile"
        };

        String[] expected = {
                "ログイン", "登録はこちら", "ホーム", "プロフィール", "メッセージ", "プロフィールを編集", "パスワードを変更", "ユーザープロフィール"
        };

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            assertTrue("Missing required key in Japanese: " + title, titlesJA.containsKey(title));
            assertEquals("Value for key '" + title + "' does not match the expected value.", expected[i], titlesJA.getString(title));
        }
    }

    @Test
    public void testRussianTitles() {
        String[] titles = {
                "login", "register", "main", "profile", "messages", "editprofile", "updateinfo", "userProfile"
        };

        String[] expected = {
                "Войти", "Зарегистрироваться здесь", "Главная", "Профиль", "Сообщения", "Редактировать профиль", "Изменить пароль", "Профиль пользователя"
        };

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            assertTrue("Missing required key in Russian: " + title, titlesRU.containsKey(title));
            assertEquals("Value for key '" + title + "' does not match the expected value.", expected[i], titlesRU.getString(title));
        }
    }

}
