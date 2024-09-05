package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class ProfileController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button changeProfilePictureButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    public void initialize() {
        profileImageView.setImage(createPlaceholderImage(150, 150));
        changeProfilePictureButton.setOnAction(event -> handleChangeProfilePicture());
        homeButton.setOnAction(event -> navigateTo("/main.fxml"));
        profileButton.setOnAction(event -> navigateTo("/profile.fxml"));
    }

    private void handleChangeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image newImage = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(newImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 350, 500);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Main Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WritableImage createPlaceholderImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);

        gc.getCanvas().snapshot(null, image);

        return image;
    }
}
