package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class NewPostController {

    private ControllerForView controllerForView;
    private MainController mainController;

    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;

    @FXML             
    private void initialize() {
        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handleNewPost());
        imageView.setImage(createPlaceholderImage(200, 225));
        clickChooseButton(chooseImageButton);
    }

    @FXML
    private void handleNewPost() {
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

    private WritableImage createPlaceholderImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);

        gc.getCanvas().snapshot(null, image);
        return image;
    }

    private void postPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif",
                        "*.webp", "*.svg"));
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image newImage = new Image(selectedFile.toURI().toString(), 200, 225, false, true);
                imageView.setImage(newImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clickChooseButton(Button chooseImageButton) {
        chooseImageButton.setOnMouseClicked((MouseEvent event) -> {
            postPicture();
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setControllerForView(ControllerForView controllerForView) {
        this.controllerForView = controllerForView;
    }
}
