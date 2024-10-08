package controller;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.*;
import view.*;
import java.io.IOException;
import java.util.List;

public class UserProfileController {
    private ControllerForView controllerForView = ControllerForView.getInstance();
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
    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button leaveMessageButton;
    @FXML
    private ListView<Post> feedListView;
    @FXML
    private Label followersCountLabel;
    @FXML
    private Label followingCountLabel;

    public void initialize(int userId) throws IOException, InterruptedException {
        homeButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", "Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        profileButton.setOnAction(event -> {
            try {
                switchScene("/profile.fxml", "Profile Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        leaveMessageButton.setOnAction(event -> {
            try {
                switchScene("/messages.fxml", "Messages");
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

    private void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
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

    private void fetchUserData(int userId) throws IOException, InterruptedException {
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

            if (loggedUser.getUserData() != null && loggedUser.getUserData().getFriends() != null) {
                for (UserModel friend : loggedUser.getUserData().getFriends()) {
                    if (friend.getUserId() == userId) {
                        isFriend = true;
                        break;
                    }
                }
            }

            if (isFriend) {
                followButton.setText("Following");
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #0095ff, #1564ba);");
            } else {
                followButton.setText("Follow");
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #007bff, #0056b3);");
            }

        } else {
            System.out.println("Käyttäjää ei löytynyt.");
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

    public void handleFollowButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        UserModel userToFollow = controllerForView.getUserById(userId);
        int friendId = userToFollow.getUserId();
        int currentUserId = SessionManager.getInstance().getLoggedUser().getUserId();

        if (controllerForView.isFriend(currentUserId, friendId)) {
            boolean success = controllerForView.removeFriend(friendId);

            if (success) {
                followButton.setText("Follow");
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #007bff, #0056b3)");

                int currentFollowers = Integer.parseInt(followersCountLabel.getText());
                followersCountLabel.setText(String.valueOf(currentFollowers - 1));
            }
        } else {
            boolean success = controllerForView.addFriend(currentUserId, friendId);

            if (success) {
                followButton.setText("Following");
                followButton.setStyle("-fx-background-color: linear-gradient(to bottom, #0095ff, #1564ba);");

                int currentFollowers = Integer.parseInt(followersCountLabel.getText());
                followersCountLabel.setText(String.valueOf(currentFollowers + 1));
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
            stage.setTitle("Messages");
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

    public void setMainView(View view) {
        this.mainView = view;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
