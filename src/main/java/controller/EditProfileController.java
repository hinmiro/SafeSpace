package controller;

import javafx.event.*;
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
import view.View;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import javafx.scene.shape.*;

public class EditProfileController {

    private final ControllerForView controllerForView = ControllerForView.getInstance();
    private MainController mainController;
    private ProfileController profileController;
    private Image newImage;
    private File selectedFile;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(EditProfileController.class.getName());

    @FXML private Label nameLabel;
    @FXML private ImageView profileImageView;
    @FXML private View mainView;
    @FXML public Label usernameLabel;
    @FXML public Label registeredLabel;
    @FXML public Label bioLabel;
    @FXML private Button closeButton;
    @FXML private TextField usernameField;
    @FXML private TextArea bioField;
    @FXML private Button saveChangesButton;
    @FXML private Button uploadImageButton;

    @FXML
    public void initialize() {
        updateLanguage();

        usernameLabel.setText(SessionManager.getInstance().getLoggedUser().getUsername());
        registeredLabel.setText(SessionManager.getInstance().getLoggedUser().getDateOfCreation());

        if (SessionManager.getInstance().getLoggedUser().getProfilePictureUrl().equals("default")) {
            profileImageView.setImage(createPlaceholderImage(150, 150));
        } else {
            profileImageView.setImage(controllerForView.getProfilePicture(SessionManager.getInstance().getLoggedUser().getUserId()));
        }

        makeCircle(profileImageView);
        clickProfilePic(profileImageView);
        profileImageView.setCursor(Cursor.HAND);

        closeButton.setOnAction(event -> handleClose());
        saveChangesButton.setOnAction(this::handleSaveChanges);
    }

    private void updateTexts() {
        uploadImageButton.setText(buttons.getString("uploadImage"));
        nameLabel.setText(labels.getString("username"));
        usernameField.setPromptText(fields.getString("usernameNew"));
        bioLabel.setText(labels.getString("bio"));
        bioField.setPromptText(fields.getString("bioNew"));
        saveChangesButton.setText(buttons.getString("saveChanges"));
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            profileController = loader.getController();
            profileController.setMainController(mainController);

            Stage stage = new Stage();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("profile"));

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.info("Theme URL is null");
            }

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
        newName = newName.isEmpty() ? SessionManager.getInstance().getLoggedUser().getUsername() : newName;
        String newBio = bioField.getText().trim();
        newBio = newBio.isEmpty() ? SessionManager.getInstance().getLoggedUser().getBio() : newBio;

        try {
            boolean updateSuccess = controllerForView.updateProfile(newName, null, newBio, selectedFile);
            controllerForView.setUserArrays();

            if (updateSuccess) {
                if (newImage != null) {
                    profileImageView.setImage(newImage);
                } else {
                    profileImageView.setImage(createPlaceholderImage(150, 150));
                }
                showAlert(alerts.getString("changesSaved"), Alert.AlertType.INFORMATION);
            }
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            showAlert(alerts.getString("failedSave"), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alerts.getString("update.title"));
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

    private File changeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif",
                        "*.webp", "*.svg"));
        selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile == null) {
            logger.info("No file selected");
            return null;
        }

        if (selectedFile.length() > 5000000) {
            showAlert(alerts.getString("fileSize"), Alert.AlertType.ERROR);
            return null;
        }

        if (selectedFile != null) {
            try {
                newImage = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(newImage);
                makeCircle(profileImageView);
                return selectedFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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
        profileImageView.setOnMouseClicked((MouseEvent event) ->
            changeProfilePicture()
        );
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
