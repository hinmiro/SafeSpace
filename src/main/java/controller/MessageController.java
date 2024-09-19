package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.View;

import java.io.IOException;

public class MessageController {

    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button leaveMessageButton;
    @FXML
    private Label noMessagesLabel;
    @FXML
    private VBox contentBox;
    @FXML
    private View mainView;

    private MainController mainController;
    private ControllerForView controllerForView = ControllerForView.getInstance();

    @FXML
    private void initialize() {

        checkIfNoMessages();

        leaveMessageButton.setText("x");
        leaveMessageButton.setStyle("-fx-font-size: 16px;");

        leaveMessageButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", "Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        homeButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", "Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        profileButton.setOnAction(event -> {
            try {
                switchScene("/profile.fxml", "Profile Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 360, 800);
        stage.setScene(scene);
        stage.setTitle(title);

        if (fxmlFile.equals("/profile.fxml")) {
            ProfileController profileController = fxmlLoader.getController();
            profileController.setMainView(mainView);
            //profileController.setDialogStage(stage);

        }

        if (fxmlFile.equals("/main.fxml")) {
            MainController mainController = fxmlLoader.getController();
            mainController.setMainView(mainView);
        }
    }

    public void checkIfNoMessages() {
        if (contentBox.getChildren().isEmpty() || (contentBox.getChildren().size() == 1 && contentBox.getChildren().contains(noMessagesLabel))) {
            noMessagesLabel.setVisible(true);
        } else {
            noMessagesLabel.setVisible(false);
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setMainView(View view) {
        this.mainView = view;
    }

}
