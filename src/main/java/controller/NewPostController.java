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
import model.SharedData;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewPostController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private SharedData language = SharedData.getInstance();
    private ResourceBundle bundle;
    private Locale currentLocale;
    private MainController mainController;
    private File selectedFile;

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
    private ComboBox<String> languageBox;

    @FXML
    private void initialize() {
        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handleNewPost());
        imageView.setImage(createPlaceholderImage(200, 225));
        clickChooseButton(chooseImageButton);
    }

    @FXML
    private void changeLanguage() {
        if (languageBox.getValue().equals(bundle.getString("language.fi"))) {
            currentLocale = Locale.forLanguageTag("fi");
        } else {
            currentLocale = Locale.forLanguageTag("en");
        }

        SharedData.getInstance().setCurrentLocale(currentLocale);
        bundle = ResourceBundle.getBundle("Messages", currentLocale);
        //updateTexts();
    }

    @FXML
    private void handleNewPost() {
        String text = captionTextArea.getText();

        if (selectedFile == null) {
            showAlert("Please add an image to your post.");
        }

        try {
            boolean response = controllerForView.sendPostWithImage(text, selectedFile);
            if (response) {
                showAlert("New post sent successfully!");
                handleClose();
            } else {
                showAlert("An error occurred while sending the post.");
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
            stage.setTitle("Main Page");
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
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}
