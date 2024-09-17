package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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
import javafx.scene.shape.Circle;
import model.SessionManager;
import view.View;

public class EditProfileController {

    private ControllerForView controllerForView;
    private MainController mainController;
    private ProfileController profileController;

    @FXML
    private ImageView profileImageView;
    @FXML
    private View mainView;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label registeredLabel;
    @FXML
    public Label bioLabel;
    @FXML
    private Button closeButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextArea bioField;
    @FXML
    private Button uploadImageButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Button saveChangesButton;

    @FXML
    public void initialize() {
        usernameLabel.setText(SessionManager.getInstance().getLoggedUser().getUsername());
        registeredLabel.setText(SessionManager.getInstance().getLoggedUser().getDateOfCreation());

        profileImageView.setImage(createPlaceholderImage(150, 150));
        makeCircle(profileImageView);

        clickProfilePic(profileImageView);
        profileImageView.setCursor(Cursor.HAND);
        closeButton.setOnAction(event -> handleClose());
        saveChangesButton.setOnAction(this::handleSaveChanges);
    }

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setControllerForView(controllerForView);

            Stage stage = new Stage();
            stage.setTitle("Profile Page");
            Scene scene = new Scene(root, 360, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleImageUpload(ActionEvent actionEvent) {
        changeProfilePicture();
    }

    public void handleSaveChanges(ActionEvent actionEvent) {
        String newName = usernameField.getText().trim();
        String newBio = bioField.getText().trim();
        String newProfilePictureID = profileImageView.getImage().getUrl();

        try {
            boolean updateSuccess = controllerForView.updateProfile(newName, null, newBio, newProfilePictureID);

            if (updateSuccess) {
                showAlert("Changes saved successfully.", Alert.AlertType.INFORMATION);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("An error occurred while saving changes.", Alert.AlertType.ERROR);
        }

    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Updates");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void makeCircle(ImageView imageView) {
        Circle clip = new Circle(
                imageView.getFitWidth() / 2,
                imageView.getFitHeight() / 2,
                Math.min(imageView.getFitWidth(),
                        imageView.getFitHeight()) / 2);
        imageView.setClip(clip);
    }

    private void changeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif",
                        "*.webp", "*.svg"));
        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // controllerForView.uploadProfilePicture(selectedFile); THIS IS FOR UPLOADING PICTURE TO SERVER
                Image newImage = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(newImage);
                makeCircle(profileImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private WritableImage createPlaceholderImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillOval(0, 0, width, height);

        gc.getCanvas().snapshot(null, image);
        return image;
    }

    private void clickProfilePic(ImageView profileImageView) {
        profileImageView.setOnMouseClicked((MouseEvent event) -> {
            changeProfilePicture();
        });
    }

    public void setControllerForView(ControllerForView controller) {
        controllerForView = controller;
    }

    public void setProfileController(ProfileController controller) {
        profileController = controller;
    }

    public void setMainView(View view) {
        this.mainView = view;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
