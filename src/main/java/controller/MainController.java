package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private ControllerForView controllerForView;

    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button newPostButton;
    @FXML
    private Button leaveMessageButton;
    @FXML
    private Pane newWindow;

    @FXML
    private void initialize() {
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

        leaveMessageButton.setOnAction(event -> {
            try {
                switchScene("/messages.fxml", "Messages");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        newPostButton.setOnAction(event -> showNewWindow());
    }

    protected void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        if (fxmlFile.equals("/profile.fxml")) {
            ProfileController profileController = fxmlLoader.getController();
            profileController.setControllerForView(controllerForView);
        }

        Scene scene = new Scene(root, 350, 550);
        stage.setScene(scene);
        stage.setTitle(title);
    }

    private void showNewWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPost.fxml"));
            Parent newPostPane = loader.load();

            NewPostController newPostController = loader.getController();
            newPostController.setControllerForView(controllerForView);
            newPostController.setMainController(this);

            Stage stage = (Stage) newWindow.getScene().getWindow();
            Scene scene = new Scene(newPostPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllerForView(ControllerForView controller) {
        this.controllerForView = controller;
    }
}
