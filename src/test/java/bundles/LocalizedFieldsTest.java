package bundles;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocalizedFieldsTest {
    private final Locale localeEN = new Locale("en", "US");
    private final Locale localeFI = new Locale("fi", "FI");
    private final Locale localeJA = new Locale("ja", "JP");
    private final Locale localeRU = new Locale("ru", "RU");

    private final ResourceBundle fieldsFI = ResourceBundle.getBundle("Fields", localeFI);
    private final ResourceBundle fieldsEN = ResourceBundle.getBundle("Fields", localeEN);
    private final ResourceBundle fieldsJA = ResourceBundle.getBundle("Fields", localeJA);
    private final ResourceBundle fieldsRU = ResourceBundle.getBundle("Fields", localeRU);

    @Test
    public void englishFieldsTest() {
        String[] fields = {
                "searchUsername", "sendMessage", "caption", "enterText", "usernameNew",
                "bioNew", "passwordConfirmNew", "passwordNewField", "commentHere"
        };

        String[] expected = {
                "Search for a user", "Write a message...", "Write a caption...", "Enter text here",
                "Enter new username", "Enter your bio", "Confirm new password", "Enter new password", "Comment here..."
        };

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            assertTrue("Missing required key in English: " + field, fieldsEN.containsKey(field));
            assertEquals("Value for key '" + field + "' does not match the expected value.", expected[i], fieldsEN.getString(field));
        }
    }

    @Test
    public void finnishFieldsTest() {
        String[] fields = {
                "searchUsername", "sendMessage", "caption", "enterText", "usernameNew",
                "bioNew", "passwordConfirmNew", "passwordNewField", "commentHere"
        };

        String[] expected = {
                "Etsi käyttäjää", "Kirjoita viesti...", "Kirjoita kuvateksti...", "Syötä teksti tähän",
                "Syötä uusi käyttäjänimi", "Syötä oma bio", "Vahvista uusi salasana", "Syötä uusi salasana", "Kommentoi tähän..."
        };

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            assertTrue("Missing required key in Finnish: " + field, fieldsFI.containsKey(field));
            assertEquals("Value for key '" + field + "' does not match the expected value.", expected[i], fieldsFI.getString(field));
        }
    }

    @Test
    public void japaneseFieldsTest() {
        String[] fields = {
                "searchUsername", "sendMessage", "usernameNew",
                "bioNew", "passwordConfirmNew", "passwordNewField",
                "caption", "enterText", "commentHere"
        };

        String[] expected = {
                "ユーザーを検索", "メッセージを書く...", "新しいユーザー名を入力", "自己紹介を入力", "新しいパスワードを確認",
                "新しいパスワードを入力", "キャプションを書く...", "ここにテキストを入力", "ここにコメント..."
        };

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            assertTrue("Missing required key in Japanese: " + field, fieldsJA.containsKey(field));
            assertEquals("Value for key '" + field + "' does not match the expected value.", expected[i], fieldsJA.getString(field));
        }
    }

    @Test
    public void russianFieldsTest() {
        String[] fields = {
                "searchUsername", "sendMessage", "caption", "enterText", "usernameNew",
                "bioNew", "passwordConfirmNew", "passwordNewField", "commentHere"
        };

        String[] expected = {
                "Поиск пользователя", "Написать сообщение...", "Написать подпись...", "Введите текст здесь",
                "Введите новое имя пользователя", "Введите свою биографию", "Подтвердите новый пароль", "Введите новый пароль",
                "Комментировать здесь..."
        };

        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            assertTrue("Missing required key in Russian: " + field, fieldsRU.containsKey(field));
            assertEquals("Value for key '" + field + "' does not match the expected value.", expected[i], fieldsRU.getString(field));
        }
    }
}
