package controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.ScreenUtil;
import model.UserModel;
import java.io.IOException;

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

    private ControllerForView controllerForView = ControllerForView.getInstance();

    @FXML
    public void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> backLogin());
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));
        confirmPasswordField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });
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
            strengthText = "Weak";
            strengthClass = "weak";
        } else if (strength < 3) {
            strengthText = "Medium";
            strengthClass = "medium";
        } else {
            strengthText = "Strong";
            strengthClass = "strong";
        }

        passwordStrengthLabel.setText("Password Strength: " + strengthText);
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
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Fill all the fields.");
            return;
        }

        int strength = calculatePasswordStrength(password);
        if (strength < 3) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Please choose a stronger password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
            return;
        }

        UserModel user = controllerForView.register(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.INFORMATION, "Register failed", "Username is already taken.");
            return;
        }

        if (user.getJwt() != null) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User created successfully.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) registerButton.getScene().getWindow();
                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
                stage.setScene(scene);
                stage.setTitle("Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Server error");
        }
    }

    private void backLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login Page");
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
}

