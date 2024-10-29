package controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.*;
import view.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ContextMenu settingsContextMenu;
    private MainController mainController;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

    @FXML
    public Label followersCountLabel;
    @FXML
    public Label followingCountLabel;
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
    private ListView<Post> feedListView;
    @FXML
    private Label followersLabel;
    @FXML
    private Label followingLabel;
    @FXML
    private ComboBox<String> languageBox;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);

        languageBox.getItems().setAll(
                Language.EN.getDisplayName(),
                Language.FI.getDisplayName(),
                Language.JP.getDisplayName(),
                Language.RU.getDisplayName()
        );

        Language currentLanguage = SessionManager.getInstance().getSelectedLanguage();
        languageBox.setValue(currentLanguage.getDisplayName());

        languageBox.setOnAction(event -> {
            try {
                changeLanguage();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        usernameLabel.setText(SessionManager.getInstance().getLoggedUser().getUsername());
        registeredLabel.setText(SessionManager.getInstance().getLoggedUser().getDateOfCreation());
        bioLabel.setText(SessionManager.getInstance().getLoggedUser().getBio() == null ? "..." : SessionManager.getInstance().getLoggedUser().getBio());

        profileImageView.setImage(createPlaceholderImage(150, 150));
        makeCircle(profileImageView);

        if (!SessionManager.getInstance().getLoggedUser().getProfilePictureUrl().equals("default")) {
            try {
                profileImageView.setImage(controllerForView.getProfilePicture(SessionManager.getInstance().getLoggedUser().getUserId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        fetchUserFollowers(SessionManager.getInstance().getLoggedUser().getUserId());

        homeButton.setOnAction(event -> navigateTo("/main.fxml"));
        profileButton.setOnAction(event -> navigateTo("/profile.fxml"));


        // menu profiilissa
        settingsContextMenu = new ContextMenu();
        MenuItem editProfileItem = new MenuItem("Edit Profile");
        editProfileItem.setOnAction(event -> openEditProfilePage());
        editProfileItem.setText(buttons.getString("editProfile"));

        MenuItem editInfoItem = new MenuItem("Edit Password");
        editInfoItem.setOnAction(event -> openUpdateInfoPage());
        editInfoItem.setText(buttons.getString("editInfo"));

        MenuItem logOutItem = new MenuItem("Log Out");
        logOutItem.setOnAction(event -> showLogOut());
        logOutItem.setText(buttons.getString("logOut"));

        settingsContextMenu.getItems().addAll(editProfileItem, editInfoItem, logOutItem);
        settingsProfileID.setOnMouseClicked(event -> showContextMenu(event));
        displayUserPosts();

        updateLanguage();

    }

    private void updateTexts() {
        homeButton.setText(buttons.getString("home"));
        profileButton.setText(buttons.getString("profile"));
        noPostsLabel.setText(labels.getString("noPosts"));
        followersLabel.setText(labels.getString("followers"));
        followingLabel.setText(labels.getString("following"));
        AtomicInteger i = new AtomicInteger();
        settingsContextMenu.getItems().forEach((item) -> {
            item.setText(buttons.getString("menuButton" + i));
            i.addAndGet(1);
        });
    }

    @FXML
    private void changeLanguage() throws Exception {
        String selectedLanguage = languageBox.getValue();

        if (selectedLanguage.equals(Language.FI.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.FI);
        } else if (selectedLanguage.equals(Language.JP.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.JP);
        } else if (selectedLanguage.equals(Language.RU.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.RU);
        } else {
            SessionManager.getInstance().setLanguage(Language.EN);
        }

        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        updateLanguage();
        //mainView.showProfile();
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    private void fetchUserFollowers(int userId) {
        UserModel user = controllerForView.getUserById(userId);

        int friendsCount = user.getUserData().getFriendsCount();
        int followersCount = user.getUserData().getFollowersCount() + friendsCount;
        int followingCount = user.getUserData().getFollowingCount() + friendsCount;

        followersCountLabel.setText(String.valueOf(followersCount));
        followingCountLabel.setText(String.valueOf(followingCount));
    }

    public void displayUserPosts() {
        List<Post> posts;
        try {
            posts = controllerForView.getUserPostsOwnProfile();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        if (posts.isEmpty()) {
            noPostsLabel.setVisible(true);
            feedListView.setVisible(false);
        } else {
            noPostsLabel.setVisible(false);
            feedListView.setVisible(true);

            posts.sort((post1, post2) -> post2.getPostDate().compareTo(post1.getPostDate()));

            ObservableList<Post> observablePosts = FXCollections.observableArrayList(posts);
            feedListView.setItems(observablePosts);

            feedListView.setCellFactory(listView -> new PostListCell());
        }
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
            logOutController.setMainController(mainController);
            Stage primaryStage = (Stage) profileButton.getScene().getWindow();
            logOutController.setPrimaryStage(primaryStage);
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

            UpdateInfoController updateInfoController = fxmlLoader.getController();
            updateInfoController.setProfileController(this);
            updateInfoController.setMainView(mainView);

            updateInfoController.setMainController(mainController);

            Stage stage = (Stage) profileButton.getScene().getWindow();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("updateinfo"));
            stage.setScene(new Scene(root, 360, ScreenUtil.getScaledHeight()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditProfilePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editProfile.fxml"));
            Parent root = fxmlLoader.load();

            EditProfileController editProfileController = fxmlLoader.getController();
            editProfileController.setProfileController(this);
            editProfileController.setMainController(mainController);
            editProfileController.setMainView(mainView);

            Stage stage = (Stage) profileButton.getScene().getWindow();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("editprofile"));
            stage.setScene(new Scene(root, 360, ScreenUtil.getScaledHeight()));
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

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("main"));
            MainController mainController = fxmlLoader.getController();
            mainController.setMainView(mainView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllerForView(ControllerForView controller) {
        controllerForView = controller;
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
