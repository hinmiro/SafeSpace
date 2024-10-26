package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Language;
import model.ScreenUtil;
import model.SessionManager;
import model.UserModel;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label confirmPasswordLabel;
    @FXML
    public Label alreadyLabel;
    @FXML
    public Label registerHero;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private Label passwordStrengthLabel;
    @FXML
    private ComboBox<String> languageBox;

    private ResourceBundle labels;
    private ResourceBundle buttons;
    private ResourceBundle alerts;
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

    @FXML
    public void initialize() {
        languageBox.getItems().setAll(
                Language.EN.getDisplayName(),
                Language.FI.getDisplayName()
        );

        Language currentLanguage = SessionManager.getInstance().getSelectedLanguage();
        languageBox.setValue(currentLanguage == Language.FI ? Language.FI.getDisplayName() : Language.EN.getDisplayName());

        languageBox.setOnAction(event -> changeLanguage());

        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> backLogin());
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));
        confirmPasswordField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });
        updateLanguage();
    }

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

    @FXML
    private void changeLanguage() {
        String selectedLanguage = languageBox.getValue();

        if (selectedLanguage.equals(Language.FI.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.FI);
        } else {
            SessionManager.getInstance().setLanguage(Language.EN);
        }

        updateLanguage();
    }

    private void updateLanguage() {
        Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        labels = ResourceBundle.getBundle("Labels", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        alerts = ResourceBundle.getBundle("Alerts", locale);
        updateTexts();
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRegister();
        }
    }

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

    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 6) strength++;
        if (password.chars().anyMatch(Character::isUpperCase)) strength++;
        if (password.chars().anyMatch(Character::isLowerCase)) strength++;
        if (password.chars().anyMatch(Character::isDigit)) strength++;
        return strength;
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("registrationFailed"), alerts.getString("registerFill"));
            return;
        }

        int strength = calculatePasswordStrength(password);
        if (strength < 3) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("weakErr"), alerts.getString("registerBetterPassword"));
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", alerts.getString("passwordsDontMatch"));
            return;
        }

        UserModel user = controllerForView.register(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.INFORMATION, alerts.getString("registrationFailed"), alerts.getString("usernameTaken"));
            return;
        }

        if (user.getJwt() != null) {
            showAlert(Alert.AlertType.INFORMATION, alerts.getString("registrationSuccess"), alerts.getString("userCreated"));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();

                Stage stage = (Stage) registerButton.getScene().getWindow();
                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
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

    private void backLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("login"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

