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
import static javafx.scene.control.ButtonType.OK;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

public class RegisterControllerTest extends ApplicationTest {
    private Button registerButton;
    private Button backButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label passwordStrengthLabel;

    @Before
    public void initializeSessionManager() {
        SessionManager.setLanguage(Language.EN);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        registerButton = lookup("#registerButton").queryAs(Button.class);
        backButton = lookup("#backButton").queryAs(Button.class);
        usernameField = lookup("#usernameField").queryAs(TextField.class);
        passwordField = lookup("#passwordField").queryAs(PasswordField.class);
        confirmPasswordField = lookup("#confirmPasswordField").queryAs(PasswordField.class);
        passwordStrengthLabel = lookup("#passwordStrengthLabel").queryAs(Label.class);
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#backButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#registerButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#passwordStrengthLabel", NodeMatchers.isVisible());
        sleep(1000);
    }

    @Test
    public void testRegisterButtonSuccess() {
        clickOn(usernameField).write("username10");
        clickOn(passwordField).write("password");
        clickOn(confirmPasswordField).write("password");
        sleep(1000);
        verifyThat(passwordStrengthLabel, NodeMatchers.isVisible());
        assertThat(passwordStrengthLabel.getText()).isEqualTo("Password strength: Medium");
        sleep(1000);
        clickOn(registerButton);
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("User created successfully.");
        sleep(500);
        clickOn(dialogPane.lookupButton(OK));
        sleep(500);
    }

    @Test
    public void testUsernameTaken() {
        clickOn(usernameField).write("username1");
        clickOn(passwordField).write("password");
        clickOn(confirmPasswordField).write("password");
        sleep(1000);
        verifyThat(passwordStrengthLabel, NodeMatchers.isVisible());
        assertThat(passwordStrengthLabel.getText()).isEqualTo("Password strength: Medium");
        sleep(1000);
        clickOn(registerButton);
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("Username is already taken.");
        sleep(500);
    }

    @Test
    public void testPasswordMismatch() {
        clickOn(usernameField).write("username2");
        clickOn(passwordField).write("password");
        clickOn(confirmPasswordField).write("password1");
        sleep(1000);
        assertThat(passwordStrengthLabel.getText()).isEqualTo("Password strength: Medium");
        sleep(1000);
        clickOn(registerButton);
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("Passwords do not match.");
        sleep(500);
    }

    @Test
    public void testPasswordStrength() {
        verifyThat(passwordStrengthLabel, NodeMatchers.isVisible());
        clickOn(passwordField).write("123");
        sleep(500);
        assertThat(passwordStrengthLabel.getText()).isEqualTo("Password strength: Weak");
        clickOn(passwordField).write("Password123");
        sleep(500);
        assertThat(passwordStrengthLabel.getText()).isEqualTo("Password strength: Strong");
        sleep(1000);
    }

    @Test
    public void testEmptyFieldsShowError() {
        clickOn(registerButton);
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane).isNotNull();
        assertThat(dialogPane.getContentText()).isEqualTo("Fill all the fields.");
        sleep(1000);
    }

    @Test
    public void testBetterPassword() {
        clickOn(usernameField).write("username5");
        clickOn(passwordField).write("123");
        clickOn(confirmPasswordField).write("123");
        sleep(500);
        assertThat(passwordStrengthLabel.getText()).isEqualTo("Password strength: Weak");
        sleep(1000);
        clickOn(registerButton);
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("Please choose a stronger password.");
        sleep(500);
    }

    @Test
    public void testBackButton() {
        clickOn(backButton);
        verifyThat("#loginButton", NodeMatchers.isVisible());
        sleep(1000);
    }
}
