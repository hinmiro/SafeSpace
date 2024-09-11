package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private void updateInfo() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password.equals(confirmPassword)) {
            System.out.println("Info updated: " + username);
        } else {
            System.out.println("Passwords do not match!");
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
            stage.setTitle("Main Page");
            Scene scene = new Scene(root, 350, 550);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

