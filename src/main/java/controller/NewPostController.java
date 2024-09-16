package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;

public class NewPostController {

    private ControllerForView controllerForView;
    private MainController mainController;

    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea textPostArea;
    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {

        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handleNewPost());
        chooseImageButton.setOnAction(event -> handleImagePost());
    }

    @FXML
    private void handleImagePost() {}

    @FXML
    private void handleNewPost() {
        FileChooser fileChooser = new FileChooser();
        System.out.println("dsd");
    }

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setControllerForView(controllerForView);

            Stage stage = new Stage();
            stage.setTitle("Main Page");
            Scene scene = new Scene(root, 360, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setControllerForView(ControllerForView controllerForView) {
        this.controllerForView = controllerForView;
    }
}
