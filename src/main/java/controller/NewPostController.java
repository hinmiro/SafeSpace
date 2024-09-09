package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NewPostController {

    private ControllerForView controllerForView;
    private MainController mainController;

    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea captionTextArea;
    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextArea textPostArea;

    @FXML
    private void initialize() {
        postButton.setOnAction(event -> handlePost());
        closeButton.setOnAction(event -> handleClose());
    }

    @FXML
    private void handlePost() {
        Image image = imageView.getImage();
        String caption = captionTextArea.getText();
        String textPost = textPostArea.getText();

        boolean hasImage = image != null;
        boolean hasCaption = caption != null && !caption.trim().isEmpty();
        boolean hasTextPost = textPost != null && !textPost.trim().isEmpty();

        if (hasImage || hasTextPost) {
            if (hasImage && !hasCaption && !hasTextPost) {
                mainController.updateContent(image, null, null);
            } else if (hasImage && (hasCaption || hasTextPost)) {
                mainController.updateContent(image, caption, textPost);
            } else if (hasTextPost && !hasImage) {
                mainController.updateContent(null, null, textPost);
            }
            handleClose();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Post");
            alert.setHeaderText(null);

            if (!hasImage && !hasTextPost) {
                alert.setContentText("Please add an image or write some text to post.");
            } else if (!hasImage) {
                alert.setContentText("Please add an image to your post.");
            } else if (!hasTextPost) {
                alert.setContentText("Please write some text for your post.");
            }
            alert.showAndWait();
        }
    }

    @FXML
    private void handleClose() {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setControllerForView(ControllerForView controllerForView) {
        this.controllerForView = controllerForView;
    }
}
