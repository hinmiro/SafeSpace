package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import view.View;

import java.io.IOException;

public class UpdateInfoController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ProfileController profileController;
    private MainController mainController;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button closeButton;
    @FXML
    private Label passwordStrengthLabel;
    @FXML
    private View mainView;
    @FXML
    private Button mainButton;

    @FXML
    private void initialize() {
        closeButton.setOnAction(event -> handleClose());
        //mainButton.setOnAction(event -> updatePassword());

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });
    }

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setMainView(mainView);
            profileController.setMainController(mainController);

            Stage stage = new Stage();
            stage.setTitle("Profile Page");
            Scene scene = new Scene(root, 360, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    private void updatePassword() {
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password fields cannot be empty.");
        } else if (password.equals(confirmPassword)) {
            try {
                boolean success = controllerForView.updateProfile(null, password, null, null);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Info Updated", "Password updated successfully.");
                    handleClose();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update password.");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Error updating password.");
            }
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match!");
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


    public void setProfileController(ProfileController controller) {
        profileController = controller;
    }

    public void setMainView(View mainView) { this.mainView = mainView; }

    public void setMainController(MainController mainController) { this.mainController = mainController; }

}

