package controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import model.*;
import services.Theme;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for handling user registration.
 */
public class RegisterController {

    private ResourceBundle labels;
    private ResourceBundle buttons;
    private ResourceBundle alerts;
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());

    @FXML public Label usernameLabel;
    @FXML public Label passwordLabel;
    @FXML public Label confirmPasswordLabel;
    @FXML public Label alreadyLabel;
    @FXML public Label registerHero;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label passwordStrengthLabel;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> backLogin());
        usernameField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);
        confirmPasswordField.setOnKeyPressed(this::handleEnterKey);
        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                updatePasswordStrength(newValue));

        updateLanguage();
    }

    /**
     * Updates the text of UI elements based on the selected language.
     */
    private void updateTexts() {
        registerHero.setText(labels.getString("registerHero"));
        usernameLabel.setText(labels.getString("username"));
        passwordLabel.setText(labels.getString("password"));
        confirmPasswordLabel.setText(labels.getString("passwordConfirm"));
        confirmPasswordField.setPromptText(labels.getString("passwordConfirm"));
        passwordStrengthLabel.setText(labels.getString("passwordStrengthLabel"));
        usernameField.setPromptText(labels.getString("username"));
        passwordField.setPromptText(labels.getString("password"));
        alreadyLabel.setText(labels.getString("alreadyAccount"));
        registerButton.setText(buttons.getString("registerButton"));
        backButton.setText(buttons.getString("backToLogin"));
    }

    /**
     * Updates the language of the application based on the selected locale.
     */
    private void updateLanguage() {
        Locale localeLanguage = SessionManager.getInstance().getSelectedLanguage().getLocale();
        labels = ResourceBundle.getBundle("Labels", localeLanguage);
        buttons = ResourceBundle.getBundle("Buttons", localeLanguage);
        alerts = ResourceBundle.getBundle("Alerts", localeLanguage);
        updateTexts();
    }

    /**
     * Handles the Enter key press event to trigger registration.
     * @param event the key event
     */
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRegister();
        }
    }

    /**
     * Updates the password strength label based on the entered password.
     * @param password the entered password
     */
    private void updatePasswordStrength(String password) {
        int strength = calculatePasswordStrength(password);
        String strengthText;
        String strengthClass;

        if (strength < 2) {
            strengthText = labels.getString("weak");
            strengthClass = "weak";
        } else if (strength < 3) {
            strengthText = labels.getString("medium");
            strengthClass = "medium";
        } else {
            strengthText = labels.getString("strong");
            strengthClass = "strong";
        }

        passwordStrengthLabel.setText(labels.getString("passwordStrength") + strengthText);
        passwordStrengthLabel.setText(labels.getString("passwordStrDiffer") + " " + strengthText);
        passwordField.getStyleClass().removeAll("weak", "medium", "strong");
        passwordField.getStyleClass().add(strengthClass);
    }

    /**
     * Calculates the strength of the entered password.
     * @param password the entered password
     * @return the strength of the password
     */
    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 6) strength++;
        if (password.chars().anyMatch(Character::isUpperCase)) strength++;
        if (password.chars().anyMatch(Character::isLowerCase)) strength++;
        if (password.chars().anyMatch(Character::isDigit)) strength++;
        return strength;
    }

    /**
     * Handles the registration process.
     */
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String registrationFailed = alerts.getString("registrationFailed");

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, registrationFailed, alerts.getString("registerFill"));
            return;
        }

        int strength = calculatePasswordStrength(password);
        if (strength < 2) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("weakErr"), alerts.getString("registerBetterPassword"));
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", alerts.getString("passwordsDontMatch"));
            return;
        }

        UserModel user = controllerForView.register(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.INFORMATION, registrationFailed, alerts.getString("usernameTaken"));
            return;
        }

        if (user.getJwt() != null) {
            showAlert(Alert.AlertType.INFORMATION, alerts.getString("registrationSuccess"), alerts.getString("userCreated"));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) registerButton.getScene().getWindow();

                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

                URL themeUrl = getClass().getResource(Theme.getTheme());
                if (themeUrl != null) {
                    scene.getStylesheets().add(themeUrl.toExternalForm());
                } else {
                    logger.warning("Theme URL is null");
                }

                stage.setScene(scene);
                ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
                stage.setTitle(pageTitle.getString("main"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, alerts.getString("registrationFailed"), alerts.getString("serverErr"));
        }
    }

    /**
     * Navigates back to the login screen.
     */
    private void backLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning("Theme URL is null");
            }

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("login"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert with the specified message.
     * @param alertType the type of alert
     * @param title the title of the alert
     * @param message the message to be displayed in the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}