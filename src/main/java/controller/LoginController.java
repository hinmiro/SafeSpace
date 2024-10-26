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
import model.SharedData;
import model.UserModel;
import services.ApiClient;
import view.View;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

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
    @FXML
    private ComboBox<String> languageBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label infoLabel;

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private SharedData language = SharedData.getInstance();
    private View mainView;
    private ResourceBundle bundle;
    private Locale currentLocale;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        currentLocale = SharedData.getInstance().getCurrentLocale();
        bundle = SharedData.getInstance().getBundle();
        updateTexts();

        languageBox.getItems().setAll(bundle.getString("language.fi"), bundle.getString("language.en"));
        languageBox.setValue(currentLocale.getLanguage().equals("fi") ? bundle.getString("language.fi") : bundle.getString("language.en"));

        serverError.setVisible(!ApiClient.isServerAvailable());

        loginButton.setOnMouseClicked(event -> handleLogin());
        registerButton.setOnMouseClicked(event -> handleRegisterLink(event));
        usernameField.setOnKeyPressed(event -> handleEnterKey(event));
        passwordField.setOnKeyPressed(event -> handleEnterKey(event));
    }

    private void updateTexts() {
        usernameLabel.setText(bundle.getString("username"));
        passwordLabel.setText(bundle.getString("password"));
        infoLabel.setText(bundle.getString("loginInfo"));
        usernameField.setPromptText(bundle.getString("username"));
        passwordField.setPromptText(bundle.getString("password"));
        loginButton.setText(bundle.getString("login"));
        registerButton.setText(bundle.getString("registerHere"));
        serverError.setText(bundle.getString("serverError"));
        }

    @FXML
    private void changeLanguage() {
        if (currentLocale.getLanguage().equals("en")) {
            currentLocale = Locale.forLanguageTag("fi");
        } else {
            currentLocale = Locale.forLanguageTag("en");
        }
        language.setCurrentLocale(currentLocale);
        bundle = ResourceBundle.getBundle("Messages", currentLocale);
        updateTexts();
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
            showAlert(Alert.AlertType.ERROR, bundle.getString("loginFailed"), bundle.getString("usernameOrPasswordMissing"));
            return;
        }

        UserModel user = controllerForView.login(username, password);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("loginFailed"), bundle.getString("incorrectUsernameOrPassword"));
            return;
        }

        if (user.getJwt() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                loader.setResources(bundle);
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setMainView(mainView);
                mainController.setBundle(bundle);

                Stage stage = (Stage) loginButton.getScene().getWindow();

                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
                stage.setScene(scene);
                stage.setTitle(bundle.getString("mainPageTitle"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, bundle.getString("loginFailed"), bundle.getString("incorrectUsernameOrPassword"));
        }
    }

    private void handleRegisterLink(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();

            RegisterController registerController = loader.getController();
            registerController.setBundle(bundle);

            Stage stage = (Stage) registerButton.getScene().getWindow();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.setTitle(bundle.getString("registerPageTitle"));
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

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

}

