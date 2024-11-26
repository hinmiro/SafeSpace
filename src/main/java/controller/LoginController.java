package controller;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.fxml.*;
import model.*;
import services.*;
import view.View;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for handling user login.
 */
public class LoginController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private View mainView;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML public Label serverError;
    @FXML public Label usernameLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label passwordLabel;
    @FXML private Label dontHaveLabel;
    @FXML private ComboBox<String> languageBox;

    /**
     * Initializes the controller. Sets up event handlers and updates UI texts.
     */
    @FXML
    public void initialize() {
        languageBox.getItems().setAll(
                Language.EN.getDisplayName(),
                Language.FI.getDisplayName(),
                Language.JP.getDisplayName(),
                Language.RU.getDisplayName()
        );

        Language currentLanguage = SessionManager.getInstance().getSelectedLanguage();
        languageBox.setValue(currentLanguage.getDisplayName());

        languageBox.setOnAction(event -> changeLanguage());

        loginButton.setOnMouseClicked(event -> handleLogin());
        registerButton.setOnMouseClicked(this::handleRegisterLink);
        usernameField.setOnKeyPressed(this::handleEnterKey);
        passwordField.setOnKeyPressed(this::handleEnterKey);

        serverError.setVisible(!ApiClient.isServerAvailable());
        updateLanguage();
    }

    /**
     * Updates the texts of UI elements based on the selected language.
     */
    private void updateTexts() {
        loginButton.setText(buttons.getString("login"));
        registerButton.setText(buttons.getString("register"));
        serverError.setText(labels.getString("serverErr"));
        usernameLabel.setText(labels.getString("username"));
        passwordLabel.setText(labels.getString("password"));
        usernameField.setPromptText(labels.getString("username"));
        passwordField.setPromptText(labels.getString("password"));
        dontHaveLabel.setText(labels.getString("dontHave"));
    }

    /**
     * Changes the application language based on the selected value in the language combo box.
     */
    @FXML
    private void changeLanguage() {
        String selectedLanguage = languageBox.getValue();

        if (selectedLanguage.equals(Language.FI.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.FI);
        } else if (selectedLanguage.equals(Language.JP.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.JP);
        } else if (selectedLanguage.equals(Language.RU.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.RU);
        } else {
            SessionManager.getInstance().setLanguage(Language.EN);
        }

        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        updateLanguage();
    }

    /**
     * Updates the language resources and UI texts.
     */
    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        updateTexts();
    }

    /**
     * Handles the Enter key press event to trigger login.
     *
     * @param event the key event
     */
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    /**
     * Handles the login process. Validates user credentials and navigates to the main view if successful.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String failedTitle = alerts.getString("failedTitle");

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, failedTitle, alerts.getString("failedMessage"));
            return;
        }

        UserModel user = controllerForView.login(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, failedTitle, alerts.getString("incorrectCredentials"));
            return;
        }

        if (user.getJwt() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setMainView(mainView);

                Stage stage = (Stage) loginButton.getScene().getWindow();

                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

                URL themeUrl = getClass().getResource(Theme.getTheme());
                if (themeUrl != null) {
                    scene.getStylesheets().add(themeUrl.toExternalForm());
                } else {
                    logger.warning("Theme URL is null");
                }

                stage.setScene(scene);
                ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
                stage.setTitle(pageTitle.getString("main"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, alerts.getString("failedTitle"), alerts.getString("incorrectCredentials"));
        }
    }

    /**
     * Handles the register link click event to navigate to the register view.
     *
     * @param event the mouse event
     */
    private void handleRegisterLink(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning("Theme URL is null");
            }

            stage.setScene(scene);
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("register"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert dialog with the specified type, title, and message.
     *
     * @param alertType the type of alert
     * @param title the title of the alert
     * @param message the message of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the main view for this controller.
     *
     * @param view the main view to set
     */
    public void setMainView(View view) {
        mainView = view;
    }
}