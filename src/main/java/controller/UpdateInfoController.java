package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ScreenUtil;
import model.SessionManager;
import view.View;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateInfoController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ProfileController profileController;
    private MainController mainController;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

    @FXML
    private Label newPassword;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Label passwordStrengthLabel;
    @FXML
    private View mainView;
    @FXML
    private Button mainButton;
    @FXML
    private Label changePasswordLabel;
    @FXML
    private Label requirement1;
    @FXML
    private Label requirement2;
    @FXML
    private Label requirement3;
    @FXML
    private Label requirement4;

    @FXML
    private void initialize() {
        updateLanguage();
        closeButton.setOnAction(event -> handleClose());

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordStrength(newValue);
        });
    }

    private void updateTexts() {
        newPassword.setText(labels.getString("passwordNew"));
        passwordField.setPromptText(fields.getString("passwordNewField"));
        confirmPasswordLabel.setText(labels.getString("passwordConfirmLabel"));
        confirmPasswordField.setPromptText(fields.getString("passwordConfirmNew"));
        passwordStrengthLabel.setText(labels.getString("passwordStrengthLabel"));
        mainButton.setText(buttons.getString("updatePassword"));
        changePasswordLabel.setText(labels.getString("changePassword"));
        requirement1.setText(labels.getString("passwordReq1"));
        requirement2.setText(labels.getString("passwordReq2"));
        requirement3.setText(labels.getString("passwordReq3"));
        requirement4.setText(labels.getString("passwordReq4"));
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
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
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("profile"));
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
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

    @FXML
    private void updatePassword() {
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        int strength = calculatePasswordStrength(password);
        if (strength < 3) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("weakErr"), alerts.getString("registerBetterPassword"));
            return;
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("errorTitle"), alerts.getString("fillAllFields"));
        } else if (password.equals(confirmPassword)) {
            try {
                boolean success = controllerForView.updateProfile(null, password, null, null);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, alerts.getString("infoUpdated"), alerts.getString("passWordUpdated"));
                    handleClose();
                } else {
                    showAlert(Alert.AlertType.ERROR, alerts.getString("failedInfo"), alerts.getString("failedPassword"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, alerts.getString("failedInfo"), alerts.getString("errorInfo"));
            }
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", alerts.getString("passwordsDontMatch"));
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
