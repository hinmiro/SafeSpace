package controller;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import model.*;
import services.Theme;
import view.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UserProfileController {
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private View mainView;
    private int userId;
    private MainController mainController;

    @FXML private Label usernameLabel;
    @FXML private Label bioLabel;
    @FXML private Label registeredLabel;
    @FXML private ImageView profileImageView;
    @FXML private Button followButton;
    @FXML private Button messageButton;
    @FXML private Label noPostsLabel;
    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button leaveMessageButton;
    @FXML private ListView<Post> feedListView;
    @FXML private Label followersCountLabel;
    @FXML private Label followingCountLabel;
    @FXML private Label followers;
    @FXML private Label following;

    public void initialize(int userId) throws IOException, InterruptedException {
        updateLanguage();

        ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
        homeButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", pageTitle.getString("main"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        profileButton.setOnAction(event -> {
            try {
                switchScene("/profile.fxml", pageTitle.getString("profile"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        leaveMessageButton.setOnAction(event -> {
            try {
                switchScene("/messages.fxml", pageTitle.getString("messages"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        profileImageView.setImage(createPlaceholderImage(150, 150));
        makeCircle(profileImageView);

        this.userId = userId;
        fetchUserData(userId);

        displayUserPosts(feedListView, noPostsLabel, userId);
    }

    private void updateTexts() {
        homeButton.setText(buttons.getString("home"));
        profileButton.setText(buttons.getString("profile"));
        followers.setText(labels.getString("following"));
        following.setText(labels.getString("followers"));
        messageButton.setText(buttons.getString("message"));
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    private void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

        URL themeUrl = getClass().getResource(Theme.getTheme());
        if (themeUrl != null) {
            scene.getStylesheets().add(themeUrl.toExternalForm());
        } else {
            System.err.println("Theme URL is null");
        }

        stage.setScene(scene);
        stage.setTitle(title);

        if (fxmlFile.equals("/profile.fxml")) {
            ProfileController profileController = fxmlLoader.getController();
            profileController.setMainController(mainController);
            profileController.setMainView(mainView);
            profileController.setMainController(mainController);
        }

        if (fxmlFile.equals("/main.fxml")) {
            mainController = fxmlLoader.getController();
            mainController.setMainView(mainView);
        }
    }

    private void fetchUserData(int userId) {
        UserModel user = controllerForView.getUserById(userId);
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            bioLabel.setText(user.getBio());
            registeredLabel.setText(user.getDateOfCreation());

            String profilePictureUrl = user.getProfilePictureUrl();

            if (!profilePictureUrl.equals("default")) {
                try {
                    profileImageView.setImage(controllerForView.getProfilePicture(userId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int friendsCount = user.getUserData().getFriendsCount();
            int followersCount = user.getUserData().getFollowersCount() + friendsCount;
            int followingCount = user.getUserData().getFollowingCount() + friendsCount;

            followersCountLabel.setText(String.valueOf(followersCount));
            followingCountLabel.setText(String.valueOf(followingCount));

            UserModel loggedUser = SessionManager.getInstance().getLoggedUser();
            boolean isFriend = false;
            boolean isFollowing = false;

            if (loggedUser.getUserData() != null && loggedUser.getUserData().getFriends() != null) {
                for (UserModel friend : loggedUser.getUserData().getFriends()) {
                    if (friend.getUserId() == userId) {
                        isFriend = true;
                        break;
                    }
                }
            }

            if (loggedUser.getUserData() != null && loggedUser.getUserData().getFollowing() != null) {
                for (UserModel following : loggedUser.getUserData().getFollowing()) {
                    if (following.getUserId() == userId) {
                        isFollowing = true;
                        break;
                    }
                }
            }

            if (isFriend) {
                followButton.setText(labels.getString("followingUser"));
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #0095ff, #1564ba);");
            } else if (isFollowing) {
                followButton.setText(labels.getString("followingUser"));
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #0095ff, #1564ba);");
            } else {
                followButton.setText(labels.getString("follow"));
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #007bff, #0056b3);");
            }
        }
    }

    public void displayUserPosts(ListView<Post> feedListView, Label noPostsLabel, int userId) {
        List<Post> posts;
        try {
            posts = controllerForView.getUserPostsOwnProfile(userId);
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

    public void handleFollowButton(ActionEvent actionEvent) {
        UserModel userToFollow = controllerForView.getUserById(userId);
        int friendId = userToFollow.getUserId();
        UserModel loggedUser = SessionManager.getInstance().getLoggedUser();
        int currentUserId = SessionManager.getInstance().getLoggedUser().getUserId();

        if (controllerForView.isFriend(currentUserId, friendId)) {
            boolean success = controllerForView.removeFriend(friendId);

            if (success) {
                followButton.setText(labels.getString("follow"));
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #007bff, #0056b3)");

                int currentFollowers = Integer.parseInt(followersCountLabel.getText());
                followersCountLabel.setText(String.valueOf(currentFollowers - 1));
                loggedUser.getUserData().getFollowing().removeIf(user -> user.getUserId() == friendId);
                loggedUser.getUserData().getFriends().removeIf(user -> user.getUserId() == friendId);
            }
        } else {
            boolean success = controllerForView.addFriend(currentUserId, friendId);

            if (success) {
                followButton.setText(labels.getString("followingUser"));
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #0095ff, #1564ba);");

                int currentFollowers = Integer.parseInt(followersCountLabel.getText());
                followersCountLabel.setText(String.valueOf(currentFollowers + 1));
                loggedUser.getUserData().getFollowing().add(userToFollow);
                loggedUser.getUserData().getFriends().add(userToFollow);
            }
        }
    }

    @FXML
    private void handleMessageButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/userMessages.fxml"));
            Parent root = loader.load();

            UserMessagesController userMessagesController = loader.getController();
            userMessagesController.setUserId(userId);
            userMessagesController.initialize(userId);

            Stage stage = (Stage) messageButton.getScene().getWindow();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                System.err.println("Theme URL is null");
            }

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("messages"));
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

    public void setControllerForView(ControllerForView controller) {
        controllerForView = controller;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
