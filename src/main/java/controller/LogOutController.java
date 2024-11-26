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

/**
 * Controller class for handling user logout.
 */
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

    /**
     * Initializes the controller. Sets up event handlers and updates UI texts.
     */
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

    /**
     * Updates the texts of UI elements based on the selected language.
     */
    private void updateTexts() {
        logOutButton.setText(buttons.getString("logOut"));
        cancelButton.setText(buttons.getString("cancel"));
        logOut.setText(labels.getString("logOutLabel"));
    }

    /**
     * Updates the language resources and UI texts.
     */
    private void updateLanguage() {
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        updateTexts();
    }

    /**
     * Sets the dialog stage for this controller.
     *
     * @param dialogStage the dialog stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Handles the logout process. Stops queue processing, closes the session, and navigates to the login view.
     *
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Handles the cancel action to close the dialog stage.
     */
    private void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    /**
     * Sets the main controller for this controller.
     *
     * @param mainController the main controller to set
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Sets the primary stage for this controller.
     *
     * @param primaryStage the primary stage to set
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}