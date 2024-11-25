package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import services.Theme;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class LogOutController {

    private Stage dialogStage;
    private MainController mainController;
    private Stage primaryStage;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(LogOutController.class.getName());

    @FXML private Button logOutButton;
    @FXML private Button cancelButton;
    @FXML private Label logOut;

    @FXML
    private void initialize() {
        updateLanguage();

        logOutButton.setOnAction(event -> {
            try {
                handleLogOut();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void updateTexts() {
        logOutButton.setText(buttons.getString("logOut"));
        cancelButton.setText(buttons.getString("cancel"));
        logOut.setText(labels.getString("logOutLabel"));
    }

    private void updateLanguage() {
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        updateTexts();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void handleLogOut() throws IOException {
        mainController.stopQueueProcessing();

        SessionManager.getInstance().closeSession();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent loginRoot = loader.load();

        Scene loginScene = new Scene(loginRoot, 360, ScreenUtil.getScaledHeight());

        URL themeUrl = getClass().getResource(Theme.getTheme());
        if (themeUrl != null) {
            loginScene.getStylesheets().add(themeUrl.toExternalForm());
        } else {
            logger.warning("Theme URL is null");
        }

        primaryStage.setScene(loginScene);
        ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
        primaryStage.setTitle(pageTitle.getString("login"));

        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
