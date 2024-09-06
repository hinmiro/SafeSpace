package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
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
    private Button createPostButton;
    @FXML
    private Button leaveMessageButton;
    @FXML
    private Pane newWindow;

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

        newPostButton.setOnAction(event -> menuVisible());
        createPostButton.setOnAction(event -> showNewWindow());
        leaveMessageButton.setOnAction(event -> {});
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

    private void menuVisible() {
        boolean isVisible = createPostButton.isVisible();
        createPostButton.setVisible(!isVisible);
        leaveMessageButton.setVisible(!isVisible);
    }

    private void showNewWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPost.fxml"));
            Parent newPostPane = loader.load();

            newWindow.getChildren().clear();
            newWindow.getChildren().add(newPostPane);

            createPostButton.setVisible(false);
            leaveMessageButton.setVisible(false);
            newPostButton.setVisible(false);

            newWindow.setVisible(true);

            NewPostController newPostController = loader.getController();
            newPostController.setMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeNewWindow() {
        newWindow.setVisible(false);
        newPostButton.setVisible(true);
    }

    public void setControllerForView(ControllerForView controller) {
        this.controllerForView = controller;
    }
}
