package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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
        mainView.showLogin();
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


}
