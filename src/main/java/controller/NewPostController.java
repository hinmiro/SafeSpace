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
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for handling the creation of new posts with images.
 */
public class NewPostController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private MainController mainController;
    private File selectedFile;
    private static final Logger logger = Logger.getLogger(NewPostController.class.getName());

    @FXML private Button chooseImageButton;
    @FXML private ImageView imageView;
    @FXML private Button postButton;
    @FXML private Button closeButton;
    @FXML private TextArea captionTextArea;
    @FXML private Label headerLabel;
    @FXML private Label captionLabel;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        updateLanguage();

        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handleNewPost());
        imageView.setImage(createPlaceholderImage(200, 225));

        clickChooseButton(chooseImageButton);
    }

    /**
     * Updates the text of UI elements based on the selected language.
     */
    private void updateTexts() {
        postButton.setText(buttons.getString("post"));
        captionTextArea.setPromptText(fields.getString("caption"));
        chooseImageButton.setText(buttons.getString("chooseImage"));
        headerLabel.setText(labels.getString("picPost"));
        captionLabel.setText(labels.getString("captionLabel"));
    }

    /**
     * Updates the language of the application based on the selected locale.
     */
    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    /**
     * Handles the action of creating a new post with an image.
     */
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
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    /**
     * Handles the action of closing the new post window.
     */
    private void handleClose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) closeButton.getScene().getWindow();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("main"));

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning("Theme URL is null");
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a placeholder image with the specified width and height.
     * @param width the width of the placeholder image
     * @param height the height of the placeholder image
     * @return the created placeholder image
     */
    private WritableImage createPlaceholderImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        Canvas canvas = new Canvas(width, height);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.getCanvas().snapshot(null, image);

        return image;
    }

    /**
     * Handles the action of posting a picture.
     */
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

    /**
     * Sets the action for the choose image button.
     * @param chooseImageButton the button to set the action for
     */
    private void clickChooseButton(Button chooseImageButton) {
        chooseImageButton.setOnMouseClicked((MouseEvent event) -> postPicture());
    }

    /**
     * Sets the main controller of the application.
     * @param mainController the main controller to be set
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Shows an alert with the specified message.
     * @param message the message to be displayed in the alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alerts.getString("post.information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}