
package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.ScreenUtil;
import model.SessionManager;
import view.View;

public class LogOutController {

    @FXML
    private Button logOutButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
    private View mainView;
    private MainController mainController;
    private Stage primaryStage;

    @FXML
    private void initialize() {
        logOutButton.setOnAction(event -> {
            try {
                handleLogOut();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cancelButton.setOnAction(event -> handleCancel());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainView(View mainView) {
        this.mainView = mainView;
    }

    private void handleLogOut() throws Exception {
        mainController.stopQueueProcessing();

        SessionManager.getInstance().closeSession();
        // Load the logout scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent loginRoot = loader.load();
        Scene loginScene = new Scene(loginRoot, 360, ScreenUtil.getScaledHeight());

        // Get the current stage and set the new scene
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
