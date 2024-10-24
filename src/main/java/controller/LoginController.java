package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.ScreenUtil;
import model.SessionManager;
import model.UserModel;
import services.ApiClient;
import view.View;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    public Label serverError;
    public Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label dontHaveLabel;

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private View mainView;
    private Locale locale;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;


    @FXML
    public void initialize() throws IOException, InterruptedException {
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        loginButton.setOnMouseClicked(event -> handleLogin());
        registerButton.setOnMouseClicked(event -> handleRegisterLink(event));
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));

        loginButton.setText(buttons.getString("login"));
        registerButton.setText(buttons.getString("register"));

        serverError.setText(labels.getString("serverErr"));
        usernameLabel.setText(labels.getString("username"));
        passwordLabel.setText(labels.getString("password"));
        usernameField.setPromptText(labels.getString("username"));
        passwordField.setPromptText(labels.getString("password"));
        dontHaveLabel.setText(labels.getString("dontHave"));

        serverError.setVisible(!ApiClient.isServerAvailable());

    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("failedTitle"), alerts.getString("failedMessage"));
            return;
        }

        UserModel user = controllerForView.login(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, alerts.getString("failedTitle"), alerts.getString("incorrectCredentials"));
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
                stage.setScene(scene);
                stage.setTitle("Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, alerts.getString("failedTitle"), alerts.getString("incorrectCredentials"));
        }
    }

    private void handleRegisterLink(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();

            RegisterController registerController = loader.getController();

            Stage stage = (Stage) registerButton.getScene().getWindow();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
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

    public void setMainView(View view) {
        mainView = view;
    }

}

