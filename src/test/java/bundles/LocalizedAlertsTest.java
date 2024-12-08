package bundles;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocalizedAlertsTest {
    private final Locale localeEN = new Locale("en", "US");
    private final Locale localeFI = new Locale("fi", "FI");
    private final Locale localeJA = new Locale("ja", "JP");
    private final Locale localeRU = new Locale("ru", "RU");

    private final ResourceBundle alertsEN = ResourceBundle.getBundle("Alerts", localeEN);
    private final ResourceBundle alertsFI = ResourceBundle.getBundle("Alerts", localeFI);
    private final ResourceBundle alertsJA = ResourceBundle.getBundle("Alerts", localeJA);
    private final ResourceBundle alertsRU = ResourceBundle.getBundle("Alerts", localeRU);

    @Test
    public void testAlertsEnglish() {
        String[] keys = {
                "failedTitle", "registrationSuccess", "registrationFailed", "failedMessage", "incorrectCredentials",
                "registerFill", "registerBetterPassword", "passwordsDontMatch", "usernameTaken", "userCreated",
                "serverErr", "weakErr", "error.message", "error.title", "post.success", "post.error", "image.required",
                "post.information", "post.empty", "infoUpdated", "passWordUpdated", "failedInfo", "failedPassword",
                "errorInfo", "errorTitle", "fillAllFields", "update.title", "changesSaved", "failedSave", "fileSize"
        };

        String[] expectedValues = {
                "Login failed", "Registration Successful", "Registration Failed", "Username or password is missing.",
                "Incorrect username or password.", "Fill all the fields.", "Please choose a stronger password.",
                "Passwords do not match.", "Username is already taken.", "User created successfully.", "Server error",
                "Weak password", "An error occurred while sending the message.", "Error", "New post sent successfully!",
                "An error occurred while sending the post.", "Please add an image to your post.", "Information",
                "Please enter some text before posting.", "Information updated", "Password updated successfully.",
                "Update failed", "Failed to update password.", "Error updating password.", "Error",
                "Password fields cannot be empty.", "Update", "Changes saved successfully.", "An error occurred while saving changes.",
                "File size is too large. Please upload a file less than 5MB."
        };

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            assertTrue("Missing required key in English: " + key, alertsEN.containsKey(key));
            assertEquals("Value for key '" + key + "' does not match the expected value.", expectedValues[i], alertsEN.getString(key));
        }
    }

    @Test
    public void testAlertsFinnish() {
        String[] keys = {
                "failedTitle", "registrationSuccess", "registrationFailed", "failedMessage", "incorrectCredentials",
                "registerFill", "registerBetterPassword", "passwordsDontMatch", "usernameTaken", "userCreated",
                "serverErr", "weakErr", "error.message", "error.title", "post.success", "post.error", "image.required",
                "post.information", "post.empty", "infoUpdated", "passWordUpdated", "failedInfo", "failedPassword",
                "errorInfo", "errorTitle", "fillAllFields", "update.title", "changesSaved", "failedSave", "fileSize"
        };

        String[] expectedValues = {
                "Kirjautuminen epäonnistui", "Rekisteröityminen onnistui", "Rekisteröityminen epäonnistui",
                "Käyttäjätunnus tai salasana puuttuu.", "Virheellinen käyttäjänimi tai salasana.", "Täytä kaikki kentät.",
                "Valitse vahvempi salasana.", "Salasanat eivät täsmää.", "Käyttäjänimi on jo varattu.",
                "Käyttäjä luotu onnistuneesti.", "Palvelinvirhe", "Heikko salasana", "Viestin lähettäminen epäonnistui.",
                "Virhe", "Uusi julkaisu lähetettiin onnistuneesti!", "Postauksen tekemisessä tapahtui virhe.",
                "Lisää kuva postaukseesi.", "Tiedot", "Kirjoita jotain ennen julkaisua.", "Tiedot päivitetty",
                "Salasana päivitetty onnistuneesti.", "Päivitys epäonnistui", "Salasanan päivitys epäonnistui.",
                "Virhe päivitettäessä salasanaa.", "Virhe", "Salasanakentät eivät voi olla tyhjiä.", "Päivitä",
                "Muutokset tallennettu onnistuneesti.", "Muutosten tallentaminen epäonnistui.",
                "Tiedoston koko on liian suuri. Lataa tiedosto, joka on alle 5MB."
        };

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            assertTrue("Missing required key in Finnish: " + key, alertsFI.containsKey(key));
            assertEquals("Value for key '" + key + "' does not match the expected value.", expectedValues[i], alertsFI.getString(key));
        }
    }

    @Test
    public void testAlertsJapanese() {
        String[] keys = {
                "failedTitle", "registrationSuccess", "registrationFailed", "failedMessage", "incorrectCredentials",
                "registerFill", "registerBetterPassword", "passwordsDontMatch", "usernameTaken", "userCreated",
                "serverErr", "weakErr", "error.message", "error.title", "post.success", "post.error", "image.required",
                "post.information", "post.empty", "infoUpdated", "passWordUpdated", "failedInfo", "failedPassword",
                "errorInfo", "errorTitle", "fillAllFields", "update.title", "changesSaved", "failedSave", "fileSize"
        };

        String[] expectedValues = {
                "ログインに失敗しました", "登録が完了しました", "登録に失敗しました", "ユーザー名またはパスワードが入力されていません。",
                "ユーザー名またはパスワードが正しくありません。", "すべての項目を入力してください。", "より強力なパスワードを選択してください。",
                "パスワードが一致しません。", "このユーザー名は既に使用されています。", "ユーザーが正常に作成されました。",
                "サーバーエラー", "弱いパスワード", "メッセージの送信中にエラーが発生しました。", "エラー", "新しい投稿が正常に送信されました！",
                "投稿の送信中にエラーが発生しました。", "投稿に画像を追加してください。", "情報", "投稿する前にテキストを入力してください。",
                "情報が更新されました", "パスワードが正常に更新されました。", "更新に失敗しました", "パスワードの更新に失敗しました。",
                "パスワードの更新中にエラーが発生しました。", "エラー", "パスワードフィールドは空にできません。", "更新",
                "変更が正常に保存されました。", "変更の保存中にエラーが発生しました。", "ファイルサイズが大きすぎます。5MB未満のファイルをアップロードしてください。"
        };

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            assertTrue("Missing required key in Japanese: " + key, alertsJA.containsKey(key));
            assertEquals("Value for key '" + key + "' does not match the expected value.", expectedValues[i], alertsJA.getString(key));
        }
    }

    @Test
    public void testAlertsRussian() {
        String[] keys = {
                "failedTitle", "registrationSuccess", "registrationFailed", "failedMessage", "incorrectCredentials",
                "registerFill", "registerBetterPassword", "passwordsDontMatch", "usernameTaken", "userCreated",
                "serverErr", "weakErr", "error.message", "error.title", "post.success", "post.error", "image.required",
                "post.information", "post.empty", "infoUpdated", "passWordUpdated", "failedInfo", "failedPassword",
                "errorInfo", "errorTitle", "fillAllFields", "update.title", "changesSaved", "failedSave", "fileSize"
        };

        String[] expectedValues = {
                "Ошибка входа", "Регистрация прошла успешно", "Ошибка регистрации", "Имя пользователя или пароль отсутствуют.",
                "Неверное имя пользователя или пароль.", "Заполните все поля.", "Пожалуйста, выберите более надежный пароль.",
                "Пароли не совпадают.", "Имя пользователя уже занято.", "Пользователь успешно создан.", "Ошибка сервера",
                "Слабый пароль", "Ошибка при отправке сообщения.", "Ошибка", "Новый пост успешно отправлен!",
                "Ошибка при отправке поста.", "Пожалуйста, добавьте изображение к вашему посту.", "Информация",
                "Пожалуйста, введите текст перед отправкой.", "Информация обновлена", "Пароль успешно обновлен.",
                "Ошибка обновления", "Не удалось обновить пароль.", "Ошибка обновления пароля.", "Ошибка",
                "Поля пароля не могут быть пустыми.", "Обновить", "Изменения успешно сохранены.", "Ошибка при сохранении изменений.",
                "Размер файла слишком велик. Пожалуйста, загрузите файл размером менее 5 МБ."
        };

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            assertTrue("Missing required key in Russian: " + key, alertsRU.containsKey(key));
            assertEquals("Value for key '" + key + "' does not match the expected value.", expectedValues[i], alertsRU.getString(key));
        }
    }
}
