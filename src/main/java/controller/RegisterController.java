package controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import model.LoginResponse;
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
    private TextField contactField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;

    private ControllerForView controllerForView;

    public void setControllerForView(ControllerForView controllerForView) {
        this.controllerForView = controllerForView;
    }


    @FXML
    public void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> backLogin());
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRegister();
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String contactInfo = contactField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Fill all the fields.");
        } else if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
        }

        UserModel user = controllerForView.register(username, password);
        System.out.println(user);
        if (user.getJwt() != null) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User created successfully.");
            backLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Server error");
        }
    }


    private void backLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login Page");
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

