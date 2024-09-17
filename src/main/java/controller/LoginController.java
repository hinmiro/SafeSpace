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
import model.UserModel;
import services.ApiClient;
import view.View;

import java.io.IOException;

public class LoginController {

    @FXML
    public Label serverError;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private ControllerForView controllerForView = new ControllerForView();
    private View mainView;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        serverError.setVisible(!ApiClient.isServerAvailable());

        loginButton.setOnMouseClicked(event -> handleLogin());
        registerButton.setOnMouseClicked(event -> handleRegisterLink(event));
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));

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
            showAlert(Alert.AlertType.ERROR, "Login failed", "Username or password is missing.");
            return;
        }

        UserModel user = controllerForView.login(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect username or password.");
            return;
        }

        if (user.getJwt() != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setControllerForView(controllerForView);
                mainController.setMainView(mainView);


                Stage stage = (Stage) loginButton.getScene().getWindow();

                Scene scene = new Scene(root, 360, 800);
                stage.setScene(scene);
                stage.setTitle("Main Page");
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

            Scene scene = new Scene(root, 360, 800);
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

