package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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


    private void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        ProfileController profileController = fxmlLoader.getController();
        profileController.setControllerForView(controllerForView);

        Scene scene = new Scene(root, 350, 500);
        stage.setScene(scene);
        stage.setTitle(title);
    }

    private void handleNewPost() {
    }

    @FXML
    private void initialize() {
        homeButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", "Home Page");
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

        newPostButton.setOnAction(event -> handleNewPost());
    }

    public void setControllerForView(ControllerForView controller) {
        this.controllerForView = controller;
    }
}
