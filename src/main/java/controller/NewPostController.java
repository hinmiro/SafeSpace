package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import model.*;
import services.Theme;
import java.io.*;
import java.util.*;

public class NewPostController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private MainController mainController;
    private File selectedFile;

    @FXML private Button chooseImageButton;
    @FXML private ImageView imageView;
    @FXML private Button postButton;
    @FXML private Button closeButton;
    @FXML private TextArea captionTextArea;
    @FXML private Label headerLabel;
    @FXML private Label captionLabel;

    @FXML
    private void initialize() {
        updateLanguage();

        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handleNewPost());
        imageView.setImage(createPlaceholderImage(200, 225));

        clickChooseButton(chooseImageButton);
    }

    private void updateTexts() {
        postButton.setText(buttons.getString("post"));
        captionTextArea.setPromptText(fields.getString("caption"));
        chooseImageButton.setText(buttons.getString("chooseImage"));
        headerLabel.setText(labels.getString("picPost"));
        captionLabel.setText(labels.getString("captionLabel"));
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    @FXML
    private void handleNewPost() {
        String text = captionTextArea.getText();

        if (selectedFile == null) {
            showAlert(alerts.getString("image.required"));
            return;
        }

        try {
            boolean response = controllerForView.sendPostWithImage(text, selectedFile);
            if (response) {
                showAlert(alerts.getString("post.success"));
                handleClose();
            } else {
                showAlert(alerts.getString("post.error"));
            }
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleClose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) closeButton.getScene().getWindow();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("main"));

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            scene.getStylesheets().add(getClass().getResource(Theme.getTheme()).toExternalForm());
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
        selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedFile.length() > 5242880) {
            showAlert(alerts.getString("fileSize"));
            return;
        }

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
        chooseImageButton.setOnMouseClicked((MouseEvent event) -> postPicture());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alerts.getString("post.information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
