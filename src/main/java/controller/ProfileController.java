package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.shape.Circle;
import model.SessionManager;
import view.View;

public class ProfileController {

    private ControllerForView controllerForView;
    private ContextMenu settingsContextMenu;
    private MainController mainController;

    @FXML
    private ImageView profileImageView;
    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button settingsProfileID;
    @FXML
    private Label noPostsLabel;
    @FXML
    private View mainView;
    @FXML
    private Stage dialogStage;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label registeredLabel;
    @FXML
    public Label bioLabel;
    @FXML
    public Label nameLabel;

    @FXML
    public void initialize() {
        updateNameLabel(SessionManager.getInstance().getFullName());
        usernameLabel.setText(SessionManager.getInstance().getLoggedUser().getUsername());
        registeredLabel.setText(SessionManager.getInstance().getLoggedUser().getDateOfCreation());
        bioLabel.setText(SessionManager.getInstance().getLoggedUser().getBio() == null ? "..." : SessionManager.getInstance().getLoggedUser().getBio());

        profileImageView.setImage(createPlaceholderImage(150, 150));
        makeCircle(profileImageView);

        homeButton.setOnAction(event -> navigateTo("/main.fxml"));
        profileButton.setOnAction(event -> navigateTo("/profile.fxml"));

        // menu
        settingsContextMenu = new ContextMenu();
        MenuItem editProfileItem = new MenuItem("Edit Profile");
        editProfileItem.setOnAction(event -> openEditProfilePage());
        MenuItem editInfoItem = new MenuItem("Update Info");
        editInfoItem.setOnAction(event -> openUpdateInfoPage());
        MenuItem logOutItem = new MenuItem("Log Out");
        logOutItem.setOnAction(event -> showLogOut());

        settingsContextMenu.getItems().addAll(editProfileItem, editInfoItem, logOutItem);
        settingsProfileID.setOnMouseClicked(event -> showContextMenu(event));
    }

    private void showContextMenu(MouseEvent event) {
        settingsContextMenu.show(settingsProfileID, Side.BOTTOM, 0, 0);
    }

    private void showLogOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/logOut.fxml"));
            Parent logoutRoot = loader.load();

            LogOutController logOutController = loader.getController();
            logOutController.setDialogStage(dialogStage);
            logOutController.setMainView(mainView);
            logOutController.setMainController(mainController);

            Scene logoutScene = new Scene(logoutRoot);
            Stage dialogStage = new Stage();
            dialogStage.setScene(logoutScene);
            logOutController.setDialogStage(dialogStage);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateInfoPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/updateInfo.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Info");
            stage.setScene(new Scene(root, 360, 800));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditProfilePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editProfile.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root, 360, 800));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeCircle(ImageView imageView) {
        Circle clip = new Circle(
                imageView.getFitWidth() / 2,
                imageView.getFitHeight() / 2,
                Math.min(imageView.getFitWidth(),
                        imageView.getFitHeight()) / 2);
        imageView.setClip(clip);
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

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 360, 800);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Main Page");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllerForView(ControllerForView controller) {
        controllerForView = controller;
    }

    public void updateNameLabel(String newName) {
        if (nameLabel != null) {
            nameLabel.setText(newName);
        }
    }

    public void setMainView(View view) {
        this.mainView = view;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
