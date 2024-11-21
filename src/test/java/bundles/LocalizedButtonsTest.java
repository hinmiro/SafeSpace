package bundles;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocalizedButtonsTest {
    private final Locale localeEN = new Locale("en", "US");
    private final Locale localeFI = new Locale("fi", "FI");
    private final Locale localeJA = new Locale("ja", "JP");
    private final Locale localeRU = new Locale("ru", "RU");

    private final ResourceBundle buttonsEN = ResourceBundle.getBundle("Buttons", localeEN);
    private final ResourceBundle buttonsFI = ResourceBundle.getBundle("Buttons", localeFI);
    private final ResourceBundle buttonsJA = ResourceBundle.getBundle("Buttons", localeJA);
    private final ResourceBundle buttonsRU = ResourceBundle.getBundle("Buttons", localeRU);

    private final String[] buttons = {
            "login", "register", "registerButton", "backToLogin",
            "home", "profile", "createPicPost", "createTextPost",
            "editProfile", "editInfo", "logOut",
            "saveChanges", "uploadImage", "cancel", "updatePassword", "post",
            "chooseImage", "follow", "message", "addComment", "friendsPosts", "allPosts", "menuButton0", "menuButton1",
            "menuButton2", "menuButton3", "menuButton4"
    };


    @Test
    public void testButtonsEnglish() {

        String[] expectedValues = {
                "Login",
                "Register here",
                "Register",
                "Back to Login",
                "Home",
                "Profile",
                "Share an image",
                "Share your thoughts",
                "Edit Profile",
                "Change Password",
                "Log Out",
                "Save Changes",
                "Upload Image",
                "Cancel",
                "Update Password",
                "Post",
                "Choose Image",
                "Follow",
                "Message",
                "Add a comment",
                "Following",
                "All",
                "Change Language",
                "Edit Profile",
                "Change Password",
                "Switch Theme",
                "Log Out"
        };

        for (int i = 0; i < buttons.length; i++) {
            String button = buttons[i];
            assertTrue("Missing required key in English: " + button, buttonsEN.containsKey(button));
            assertEquals("Incorrect value for key in English: " + button, expectedValues[i], buttonsEN.getString(button));
        }
    }

    @Test
    public void testButtonsFinnish() {
        String[] expectedValues = {
                "Kirjaudu",
                "Rekisteröidy täältä",
                "Rekisteröidy",
                "Takaisin kirjautumiseen",
                "Koti",
                "Profiili",
                "Jaa kuva",
                "Jaa ajatuksesi",
                "Muokkaa profiilia",
                "Vaihda salasana",
                "Kirjaudu ulos",
                "Tallenna muutokset",
                "Lataa kuva",
                "Peruuta",
                "Päivitä salasana",
                "Julkaise",
                "Valitse kuva",
                "Seuraa",
                "Lähetä viesti",
                "Lisää kommentti",
                "Seuraamasi",
                "Kaikki",
                "Vaihda kieltä",
                "Muokkaa profiilia",
                "Vaihda salasana",
                "Vaihda teemaa",
                "Kirjaudu ulos"
        };

        for (int i = 0; i < buttons.length; i++) {
            String button = buttons[i];
            assertTrue("Missing required key in Finnish: " + button, buttonsFI.containsKey(button));
            assertEquals("Incorrect value for key in Finnish: " + button, expectedValues[i], buttonsFI.getString(button));
        }
    }

    @Test
    public void testButtonsJapanese() {
        String[] expectedValues = {
                "ログイン",
                "登録はこちら",
                "登録",
                "ログインに戻る",
                "ホーム",
                "プロフィール",
                "画像を共有",
                "考えを共有",
                "プロフィールを編集",
                "パスワードを変更",
                "ログアウト",
                "変更を保存",
                "画像をアップロード",
                "キャンセル",
                "パスワードを更新",
                "投稿",
                "画像を共有",
                "フォロー",
                "メッセージ",
                "コメントを追加",
                "フォローしている投稿",
                "全ての投稿",
                "言語を変更",
                "プロフィールを編集",
                "パスワードを変更",
                "テーマを変更",
                "ログアウト"
        };

        for (int i = 0; i < buttons.length; i++) {
            String button = buttons[i];
            assertTrue("Missing required key in Japanese: " + button, buttonsJA.containsKey(button));
            assertEquals("Incorrect value for key in Japanese: " + button, expectedValues[i], buttonsJA.getString(button));
        }
    }

    @Test
    public void testButtonsRussian() {
        String[] expectedValues = {
                "Войти",
                "Зарегистрируйтесь здесь",
                "Зарегистрироваться",
                "Вернуться к входу",
                "Главная",
                "Профиль",
                "Поделиться изображением",
                "Поделиться своими мыслями",
                "Редактировать профиль",
                "Изменить пароль",
                "Выйти",
                "Сохранить изменения",
                "Загрузить изображение",
                "Отмена",
                "Обновить пароль",
                "Пост",
                "Выбрать изображение",
                "Подписаться",
                "Сообщение",
                "Добавить комментарий",
                "Подписки",
                "Все",
                "Сменить язык",
                "Редактировать профиль",
                "Изменить пароль",
                "Сменить тему",
                "Выйти"
        };

        for (int i = 0; i < buttons.length; i++) {
            String button = buttons[i];
            assertTrue("Missing required key in Russian: " + button, buttonsRU.containsKey(button));
            assertEquals("Incorrect value for key in Russian: " + button, expectedValues[i], buttonsRU.getString(button));
        }
    }
}
