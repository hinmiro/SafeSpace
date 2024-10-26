package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.ScreenUtil;
import model.SharedData;
import model.UserModel;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterController {

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
    private Label confirmPasswordLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label createAccount;
    @FXML
    private ComboBox<String> languageBox;

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private SharedData language = SharedData.getInstance();
    private ResourceBundle bundle;
    private Locale currentLocale;

    @FXML
    public void initialize() {
        currentLocale = SharedData.getInstance().getCurrentLocale();
        bundle = SharedData.getInstance().getBundle();
        updateTexts();

        languageBox.getItems().setAll(bundle.getString("language.fi"), bundle.getString("language.en"));
        languageBox.setValue(currentLocale.getLanguage().equals("fi") ? bundle.getString("language.fi") : bundle.getString("language.en"));

        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> backLogin());
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));
        confirmPasswordField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });
    }

    private void updateTexts() {
        createAccount.setText(bundle.getString("createAccount"));
        usernameLabel.setText(bundle.getString("username"));
        usernameField.setText(bundle.getString("username"));
        passwordLabel.setText(bundle.getString("password"));
        infoLabel.setText(bundle.getString("registerInfo"));
        passwordField.setPromptText(bundle.getString("password"));
        passwordStrengthLabel.setText(bundle.getString("passwordStrength"));
        confirmPasswordField.setPromptText(bundle.getString("confirmPassword"));
        confirmPasswordLabel.setText(bundle.getString("confirmPassword"));
        registerButton.setText(bundle.getString("register"));
        backButton.setText(bundle.getString("back"));
        languageBox.getItems().setAll(bundle.getString("finnish"), bundle.getString("english"));
    }

    @FXML
    private void changeLanguage() {
        if (currentLocale.getLanguage().equals("en")) {
            currentLocale = Locale.forLanguageTag("fi");
        } else {
            currentLocale = Locale.forLanguageTag("en");
        }
        language.setCurrentLocale(currentLocale);
        bundle = ResourceBundle.getBundle("Messages", currentLocale);
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
            strengthText = bundle.getString("passwordStrengthWeak");
            strengthClass = "weak";
        } else if (strength < 3) {
            strengthText = bundle.getString("passwordStrengthMedium");
            strengthClass = "medium";
        } else {
            strengthText = bundle.getString("passwordStrengthStrong");
            strengthClass = "strong";
        }

        passwordStrengthLabel.setText(bundle.getString("passwordStrength") + strengthText);
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
            showAlert(Alert.AlertType.ERROR, bundle.getString("registrationFailed"), bundle.getString("fillAllFields"));
            return;
        }

        int strength = calculatePasswordStrength(password);
        if (strength < 3) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("registrationFailed"), bundle.getString("chooseStrongerPassword"));
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("registrationFailed"), bundle.getString("passwordsDoNotMatch"));
            return;
        }

        UserModel user = controllerForView.register(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("registrationFailed"), bundle.getString("usernameTaken"));
            return;
        }

        if (user.getJwt() != null) {
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("registrationSuccess"), bundle.getString("registrationSuccessMessage"));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setBundle(bundle);

                Stage stage = (Stage) registerButton.getScene().getWindow();
                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
                stage.setScene(scene);
                stage.setTitle(bundle.getString("mainPageTitle"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, bundle.getString("registrationFailed"), bundle.getString("serverError"));
        }
    }

    private void backLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setBundle(bundle);

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(bundle.getString("loginPageTitle"));
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
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

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}

