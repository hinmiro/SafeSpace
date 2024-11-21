package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Language;
import model.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

public class LoginControllerTest extends ApplicationTest {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private ComboBox<String> languageBox;
    private Label serverError;

    @Before
    public void initializeSessionManager() {
        SessionManager.getInstance().setLanguage(Language.EN);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        usernameField = lookup("#usernameField").queryAs(TextField.class);
        passwordField = lookup("#passwordField").queryAs(PasswordField.class);
        loginButton = lookup("#loginButton").queryAs(Button.class);
        registerButton = lookup("#registerButton").queryAs(Button.class);
        languageBox = lookup("#languageBox").queryAs(ComboBox.class);
        serverError = lookup("#serverError").queryAs(Label.class);
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#loginButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#registerButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#languageBox", NodeMatchers.isVisible());
        sleep(1000);
    }

    @Test
    public void testLoginButtonServerError() {
        clickOn(usernameField).write("username");
        clickOn(passwordField).write("password");
        clickOn(loginButton);
        verifyThat(serverError, NodeMatchers.isInvisible());
        assertThat(serverError.getText()).isEqualTo("Could not connect to server...");
        sleep(1000);
    }

    @Test
    public void testSuccessfulLoginNavigatesToMainPage() {
        verifyThat("#loginButton", NodeMatchers.isVisible());
        clickOn("#usernameField").write("sara1");
        clickOn("#passwordField").write("123ssa");
        clickOn("#loginButton");
        sleep(1000);
    }

    @Test
    public void testEmptyFieldsShowError() {
        clickOn("#loginButton");
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane).isNotNull();
        assertThat(dialogPane.getContentText()).isEqualTo("Username or password is missing.");
        sleep(1000);
    }

    @Test
    public void testInvalidCredentialsShowError() {
        clickOn("#usernameField").write("wrongUser");
        clickOn("#passwordField").write("wrongPassword");
        clickOn("#loginButton");
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane).isNotNull();
        assertThat(dialogPane.getContentText()).isEqualTo("Incorrect username or password.");
        sleep(1000);
    }

    @Test
    public void testRegisterButtonOpensRegisterPage() {
        clickOn("#registerButton");
        verifyThat("#registerButton", NodeMatchers.isVisible());
        sleep(1000);
    }

    // language

    @Test
    public void testChangeLanguageToFinnish() {
        clickOn("#languageBox");
        clickOn("suomi");
        assertThat(languageBox.getValue()).isEqualTo("suomi");
        assertThat(loginButton.getText()).isEqualTo("Kirjaudu");
    }

    @Test
    public void testChangeLanguageToJapanese() {
        clickOn("#languageBox");
        clickOn("日本語");
        assertThat(languageBox.getValue()).isEqualTo("日本語");
        assertThat(loginButton.getText()).isEqualTo("ログイン");
    }

    @Test
    public void testChangeLanguageToRussian() {
        clickOn("#languageBox");
        clickOn("русский");
        assertThat(languageBox.getValue()).isEqualTo("русский");
        assertThat(loginButton.getText()).isEqualTo("Войти");
        clickOn("#languageBox");
        clickOn("English");
    }
}

