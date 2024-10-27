
package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.ScreenUtil;
import model.SessionManager;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogOutController {

    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label logOut;

    private Stage dialogStage;
    private MainController mainController;
    private Stage primaryStage;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

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
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void handleLogOut() throws Exception {
        mainController.stopQueueProcessing();

        SessionManager.getInstance().closeSession();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent loginRoot = loader.load();
        Scene loginScene = new Scene(loginRoot, 360, ScreenUtil.getScaledHeight());


        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");

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
