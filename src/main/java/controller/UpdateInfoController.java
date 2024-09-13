package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateInfoController {

    private ControllerForView controllerForView;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        closeButton.setOnAction(event -> handleClose());
    }

    @FXML
    private void updateUsername() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Info Updated", "Username updated successfully: " + username);
            usernameField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Username cannot be empty.");
        }
    }

    @FXML
    private void updatePassword() {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password fields cannot be empty.");
        } else if (password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Info Updated", "Password updated successfully.");
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match!");
        }
    }

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setControllerForView(controllerForView);

            Stage stage = new Stage();
            stage.setTitle("Profile Page");
            Scene scene = new Scene(root, 360, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (alertType == Alert.AlertType.CONFIRMATION) {
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        }
        alert.showAndWait();
    }

}

