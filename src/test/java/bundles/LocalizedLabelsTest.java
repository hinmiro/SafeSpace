package bundles;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocalizedLabelsTest {
    private final Locale localeEN = new Locale("en", "US");
    private final Locale localeFI = new Locale("fi", "FI");
    private final Locale localeJA = new Locale("ja", "JP");
    private final Locale localeRU = new Locale("ru", "RU");

    private final ResourceBundle labelsEN = ResourceBundle.getBundle("Labels", localeEN);
    private final ResourceBundle labelsFI = ResourceBundle.getBundle("Labels", localeFI);
    private final ResourceBundle labelsJA = ResourceBundle.getBundle("Labels", localeJA);
    private final ResourceBundle labelsRU = ResourceBundle.getBundle("Labels", localeRU);
    
    @Test
    public void testLabelsEnglish() {
        String[] labels = {
                "serverErr", "username", "password", "dontHave", "alreadyAccount",
                "registerHero", "passwordStrengthLabel", "noResults", "noPosts",
                "followers", "following", "noMessages", "picPost", "captionLabel",
                "needInspo", "whatOnMind", "logOutLabel", "passwordNew",
                "passwordConfirmLabel", "changePassword", "passwordReq1", "passwordStrength",
                "bio", "noComments", "postDetailTitle"
        };

        String[] expectedValues = {
                "Could not connect to server...",
                "Username",
                "Password",
                "Don't have an account?",
                "Already have an account?",
                "Create Your SafeSpace Account",
                "Password Strength: Weak",
                "No results found",
                "No posts yet...",
                "Followers",
                "Following",
                "No messages yet...",
                "Capture & Share",
                "Add a Caption (optional):",
                "Need inspiration?",
                "What's on your mind?",
                "Are you sure you want to log out?",
                "New Password:",
                "Confirm Password:",
                "Change Your Password",
                "At least 8 characters long",
                "Password Strength: ",
                "Bio",
                "No comments",
                "Post Details"
        };

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            assertTrue("Missing required key in English: " + label, labelsEN.containsKey(label));
            assertEquals("Value for key '" + label + "' does not match the expected value.", expectedValues[i], labelsEN.getString(label));
        }
    }

    @Test
    public void testLabelsFinnish() {
        String[] labels = {
                "serverErr", "username", "password", "dontHave", "alreadyAccount",
                "registerHero", "passwordStrengthLabel", "noResults", "noPosts",
                "followers", "following", "noMessages", "picPost", "captionLabel",
                "needInspo", "whatOnMind", "logOutLabel", "passwordNew",
                "passwordConfirmLabel", "changePassword", "passwordReq1", "passwordStrength",
                "bio", "noComments", "postDetailTitle"
        };

        String[] expectedValues = {
                "Yhteys palvelimeen epäonnistui",
                "Käyttäjätunnus",
                "Salasana",
                "Puuttuuko tunnus?",
                "Onko sinulla jo tunnus?",
                "Luo oma SafeSpace käyttäjätilisi",
                "Salasanan vahvuus: Heikko",
                "Ei tuloksia",
                "Ei vielä postauksia...",
                "Seuraajat",
                "Seurattavat",
                "Ei vielä viestejä...",
                "Ota kuva ja jaa",
                "Lisää kuvateksti (valinnainen):",
                "Kaipaatko inspiraatiota?",
                "Mitä on mielessäsi?",
                "Oletko varma, että haluat kirjautua ulos?",
                "Uusi salasana:",
                "Vahvista salasana:",
                "Vaihda salasanasi",
                "Vähintään 8 merkkiä pitkä",
                "Salasanan vahvuus: ",
                "Bio",
                "Ei kommentteja",
                "Postauksen tiedot"
        };

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            assertTrue("Missing required key in Finnish: " + label, labelsFI.containsKey(label));
            assertEquals("Value for key '" + label + "' does not match the expected value.", expectedValues[i], labelsFI.getString(label));
        }
    }

    @Test
    public void testLabelsJapanese() {
        String[] labels = {
                "serverErr", "username", "password", "dontHave", "alreadyAccount",
                "registerHero", "passwordStrengthLabel", "noResults", "noPosts",
                "followers", "following", "noMessages", "picPost", "captionLabel",
                "needInspo", "whatOnMind", "logOutLabel", "passwordNew",
                "passwordConfirmLabel", "changePassword", "passwordReq1", "passwordStrength",
                "bio", "noComments", "postDetailTitle"
        };

        String[] expectedValues = {
                "サーバーエラー",
                "ユーザー名",
                "パスワード",
                "アカウントを持っていませんか？",
                "すでにアカウントをお持ちですか？",
                "「Safespaceアカウント作成」",
                "パスワードの強度：弱い",
                "結果が見つかりません",
                "まだ投稿がありません...",
                "フォロワー",
                "フォロー中",
                "まだメッセージがありません...",
                "キャプチャ＆共有",
                "キャプションを追加（オプション）：",
                "インスピレーションが必要ですか？",
                "あなたの心に何がありますか？",
                "本当にログアウトしますか？",
                "新しいパスワード：",
                "パスワードを確認：",
                "パスワードを変更",
                "少なくとも8文字",
                "パスワードの強度： ",
                "バイオ",
                "コメントなし",
                "投稿の詳細"
        };

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            assertTrue("Missing required key in Japanese: " + label, labelsJA.containsKey(label));
            assertEquals("Value for key '" + label + "' does not match the expected value.", expectedValues[i], labelsJA.getString(label));
        }
    }

    @Test
    public void testLabelsRussian() {
        String[] labels = {
                "serverErr", "username", "password", "dontHave", "alreadyAccount",
                "registerHero", "passwordStrengthLabel", "noResults", "noPosts",
                "followers", "following", "noMessages", "picPost", "captionLabel",
                "needInspo", "whatOnMind", "logOutLabel", "passwordNew",
                "passwordConfirmLabel", "changePassword", "passwordReq1", "passwordStrength",
                "bio", "noComments", "postDetailTitle"
        };

        String[] expectedValues = {
                "Не удалось подключиться к серверу",
                "Имя пользователя",
                "Пароль",
                "Нет аккаунта?",
                "Уже есть аккаунт?",
                "Создайте аккаунт SafeSpace",
                "Сложность пароля: Слабый",
                "Ничего не найдено",
                "Пока нет постов...",
                "Подписчики",
                "Подписки",
                "Пока нет сообщений...",
                "Сделать фото и поделиться",
                "Добавьте подпись (необязательно):",
                "Нужна вдохновляющая идея?",
                "Что у вас на уме?",
                "Вы уверены, что хотите выйти?",
                "Новый пароль:",
                "Подтвердите пароль:",
                "Изменить пароль",
                "Не менее 8 символов",
                "Сложность пароля: ",
                "Биография",
                "Нет комментариев",
                "Детали поста"
        };

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            assertTrue("Missing required key in Russian: " + label, labelsRU.containsKey(label));
            assertEquals("Value for key '" + label + "' does not match the expected value.", expectedValues[i], labelsRU.getString(label));
        }
    }
}
