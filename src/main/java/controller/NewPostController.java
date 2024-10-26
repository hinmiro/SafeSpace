package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.*;
import model.ScreenUtil;
import model.SessionManager;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewPostController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private MainController mainController;
    private File selectedFile;
    private Locale locale;
    private ResourceBundle titles;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle alerts;

    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextArea captionTextArea;

    @FXML
    private void initialize() {
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        titles = ResourceBundle.getBundle("PageTitles", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        alerts = ResourceBundle.getBundle("Alerts", locale);


        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handleNewPost());
        imageView.setImage(createPlaceholderImage(200, 225));
        clickChooseButton(chooseImageButton);
    }

    @FXML
    private void handleNewPost() {
        String text = captionTextArea.getText();

        if (selectedFile == null) {
            showAlert(alerts.getString("addImageToPost"));
        }

        try {
            boolean response = controllerForView.sendPostWithImage(text, selectedFile);
            if (response) {
                showAlert(alerts.getString("newPostSuccess"));
                handleClose();
            } else {
                showAlert(alerts.getString("sendPostErr"));
            }
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleClose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();

            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setTitle(titles.getString("mainPage"));
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alerts.getString("info"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
