package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.UserModel;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private ControllerForView controllerForView = new ControllerForView();


    @FXML
    public void initialize() {
        loginButton.setOnMouseClicked(event -> handleLogin());
        registerButton.setOnMouseClicked(event -> handleRegisterLink(event));
    }



    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserModel user = controllerForView.login(username, password);

        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Front Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect username or password.");
        }
    }


    private void handleRegisterLink(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();

            RegisterController registerController = loader.getController();
            registerController.setControllerForView(controllerForView);

            Stage stage = (Stage) registerButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Register Page");
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

