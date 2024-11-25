package controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import model.*;
import services.Theme;
import view.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.logging.Logger;

public class ProfileController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ContextMenu settingsContextMenu;
    private MainController mainController;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(ProfileController.class.getName());
    private String themeNull = "Theme URL is null";
    private String pageTitles = "PageTitles";

    @FXML public Label followersCountLabel;
    @FXML public Label followingCountLabel;
    @FXML private ImageView profileImageView;
    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button settingsProfileID;
    @FXML private Label noPostsLabel;
    @FXML private View mainView;
    @FXML private Stage dialogStage;
    @FXML public Label usernameLabel;
    @FXML public Label registeredLabel;
    @FXML public Label bioLabel;
    @FXML private ListView<Post> feedListView;
    @FXML private Label followersLabel;
    @FXML private Label followingLabel;

    @FXML
    public void initialize() {
        SessionManager.getInstance().setProfileController(this);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);

        usernameLabel.setText(SessionManager.getInstance().getLoggedUser().getUsername());
        registeredLabel.setText(SessionManager.getInstance().getLoggedUser().getDateOfCreation());
        bioLabel.setText(SessionManager.getInstance().getLoggedUser().getBio() == null ? "..." : SessionManager.getInstance().getLoggedUser().getBio());

        profileImageView.setImage(createPlaceholderImage(150, 150));
        makeCircle(profileImageView);

        if (!SessionManager.getInstance().getLoggedUser().getProfilePictureUrl().equals("default")) {
            try {
                profileImageView.setImage(controllerForView.getProfilePicture(SessionManager.getInstance().getLoggedUser().getUserId()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load profile picture", e);
            }
        }

        fetchUserFollowers(SessionManager.getInstance().getLoggedUser().getUserId());

        homeButton.setOnAction(event -> navigateTo("/main.fxml"));
        profileButton.setOnAction(event -> navigateTo("/profile.fxml"));

        menuInProfile();
        settingsProfileID.setOnMouseClicked(event -> showContextMenu());
        displayUserPosts();

        updateLanguage();
    }

    private void menuInProfile() {
        settingsContextMenu = new ContextMenu();

        MenuItem changeLanguageItem = new MenuItem("Change Language");
        changeLanguageItem.setOnAction(event -> showLanguageSelectionModal());
        settingsContextMenu.getItems().add(changeLanguageItem);

        MenuItem editProfileItem = new MenuItem("Edit Profile");
        editProfileItem.setOnAction(event -> openEditProfilePage());
        editProfileItem.setText(buttons.getString("editProfile"));

        MenuItem editInfoItem = new MenuItem("Edit Password");
        editInfoItem.setOnAction(event -> openUpdateInfoPage());
        editInfoItem.setText(buttons.getString("editInfo"));

        MenuItem logOutItem = new MenuItem("Log Out");
        logOutItem.setOnAction(event -> showLogOut());
        logOutItem.setText(buttons.getString("logOut"));

        MenuItem changeThemeItem = new MenuItem("Switch Theme");
        changeThemeItem.setOnAction(event -> {
            Theme.switchTheme();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/profile.fxml"));
                Parent root = fxmlLoader.load();

                Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

                URL themeUrl = getClass().getResource(Theme.getTheme());
                if (themeUrl != null) {
                    scene.getStylesheets().add(themeUrl.toExternalForm());
                } else {
                    logger.warning(themeNull);
                }

                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        changeThemeItem.setText(buttons.getString("switchTheme"));

        settingsContextMenu.getItems().addAll(editProfileItem, editInfoItem, changeThemeItem, logOutItem);
    }

    private void showContextMenu() {
        settingsContextMenu.show(settingsProfileID, Side.BOTTOM, 0, 0);
    }

    private void updateTexts() {
        homeButton.setText(buttons.getString("home"));
        profileButton.setText(buttons.getString("profile"));
        noPostsLabel.setText(labels.getString("noPosts"));
        followersLabel.setText(labels.getString("followers"));
        followingLabel.setText(labels.getString("following"));
        AtomicInteger i = new AtomicInteger();
        settingsContextMenu.getItems().forEach(item -> {
            item.setText(buttons.getString("menuButton" + i));
            i.addAndGet(1);
        });
    }

    protected void updateLanguage() {
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
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

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                logoutScene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning(themeNull);
            }

            Stage dialogStageLogOut = new Stage();
            dialogStageLogOut.setScene(logoutScene);
            logOutController.setDialogStage(dialogStageLogOut);

            dialogStageLogOut.show();
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
            ResourceBundle pageTitle = ResourceBundle.getBundle(pageTitles, locale);
            stage.setTitle(pageTitle.getString("updateinfo"));

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning(themeNull);
            }

            stage.setScene(scene);

            URL themeUrl2 = getClass().getResource(Theme.getTheme());
            if (themeUrl2 != null) {
                stage.getScene().getStylesheets().set(0, themeUrl2.toExternalForm());
            } else {
                logger.warning(themeNull);
            }

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
            ResourceBundle pageTitle = ResourceBundle.getBundle(pageTitles, locale);
            stage.setTitle(pageTitle.getString("editprofile"));

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning(themeNull);
            }

            stage.setScene(scene);
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

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning(themeNull);
            }

            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle(pageTitles, locale);
            stage.setTitle(pageTitle.getString("main"));

            if (fxmlFile.equals("/main.fxml")) {
                MainController mainControllerLoader = fxmlLoader.getController();
                mainControllerLoader.setMainView(mainView);
            } else if (fxmlFile.equals("/profile.fxml")) {
                ProfileController profileController = fxmlLoader.getController();
                profileController.setMainView(mainView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLanguageSelectionModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/languageSelection.fxml"));
            Parent root = loader.load();

            LanguageSelectionController controller = loader.getController();
            Stage modalStage = new Stage();
            controller.setStage(modalStage);

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setResizable(false);

            Scene scene = new Scene(root, 200, 300);

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning(themeNull);
            }

            modalStage.setScene(scene);
            modalStage.showAndWait();

            navigateTo("/profile.fxml");
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
