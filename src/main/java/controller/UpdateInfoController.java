package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import services.Theme;
import view.View;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for handling the update of user information.
 */
public class UpdateInfoController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ProfileController profileController;
    private MainController mainController;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(UpdateInfoController.class.getName());

    @FXML private Label newPassword;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label confirmPasswordLabel;
    @FXML private Button closeButton;
    @FXML private Label passwordStrengthLabel;
    @FXML private View mainView;
    @FXML private Button mainButton;
    @FXML private Label changePasswordLabel;
    @FXML private Label requirement1;
    @FXML private Label requirement2;
    @FXML private Label requirement3;
    @FXML private Label requirement4;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        updateLanguage();
        closeButton.setOnAction(event -> handleClose());

        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                updatePasswordStrength(newValue)
        );
    }

    /**
     * Updates the text of UI elements based on the selected language.
     */
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

    /**
     * Updates the language of the application based on the selected locale.
     */
    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    /**
     * Handles the action of closing the update info window.
     */
    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            ProfileController profileControllerForLoader = loader.getController();
            profileControllerForLoader.setMainView(mainView);
            profileControllerForLoader.setMainController(mainController);

            Stage stage = new Stage();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("profile"));

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning("Theme URL is null");
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
        String medium = labels.getString("medium");

        if (strength < 2) {
            strengthText = labels.getString("weak");
            strengthClass = "weak";
        } else if (strength < 3) {
            strengthText = medium;
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
     * Handles the action of updating the password.
     */
    @FXML
    private void updatePassword() {
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        int strength = calculatePasswordStrength(password);
        if (strength < 2) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("weakErr"), alerts.getString("registerBetterPassword"));
            return;
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("errorTitle"), alerts.getString("fillAllFields"));
        } else if (password.equals(confirmPassword)) {
            try {
                boolean success = controllerForView.updateProfile(null, password, null, null);
                controllerForView.setUserArrays();
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, alerts.getString("infoUpdated"), alerts.getString("passWordUpdated"));
                    handleClose();
                } else {
                    showAlert(Alert.AlertType.ERROR, alerts.getString("failedInfo"), alerts.getString("failedPassword"));
                }
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, alerts.getString("failedInfo"), alerts.getString("errorInfo"));
            }

            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", alerts.getString("passwordsDontMatch"));
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

        if (alertType == Alert.AlertType.CONFIRMATION) {
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        }
        alert.showAndWait();
    }

    /**
     * Sets the profile controller.
     * @param controller the profile controller to be set
     */
    public void setProfileController(ProfileController controller) {
        profileController = controller;
    }

    /**
     * Sets the main view of the application.
     * @param mainView the main view to be set
     */
    public void setMainView(View mainView) { this.mainView = mainView; }

    /**
     * Sets the main controller of the application.
     * @param mainController the main controller to be set
     */
    public void setMainController(MainController mainController) { this.mainController = mainController; }
}