package model;

import controller.MainController;
import controller.ProfileController;
import controller.UserProfileController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SharedData {
    private static SharedData instance;


    private BlockingQueue<Post> postQueue;
    private BlockingQueue<Like> likeQueue;
    private BlockingQueue<Like> removedLikeQueue;

    private ArrayList<Post> posts;
    private ArrayList<Like> likes;


    private SharedData() {
        postQueue = new LinkedBlockingQueue<>();
        likeQueue = new LinkedBlockingQueue<>();
        removedLikeQueue = new LinkedBlockingQueue<>();
        posts = new ArrayList<>();
        likes = new ArrayList<>();
    }

    public static synchronized SharedData getInstance() {
        instance = instance == null ? new SharedData() : instance;
        return instance;
    }

    public void addEvent(Post event) {
        try {
            postQueue.put(event);
            posts.add(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void addLike(Like like) {
        likeQueue.add(like);
        likes.add(like);
    }


    public BlockingQueue<Like> getRemovedLikeQueue() {
        return removedLikeQueue;
    }

    public void addRemoveLike(Like like) {
        removedLikeQueue.add(like);
    }

    public BlockingQueue<Post> getEventQueue() {
        return postQueue;
    }

    public BlockingQueue<Like> getLikeQueue() { return likeQueue; }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public static Label createClickableUsername(String username, int userId, Stage primaryStage, Stage modalStage) {
        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("username");
        usernameLabel.setStyle("-fx-cursor: hand;");

        usernameLabel.setOnMouseClicked(event -> {
            modalStage.close();

            openUserProfile(primaryStage, userId);
        });

        return usernameLabel;
    }

    public static void openUserProfile(Stage primaryStage, int userId) {
        try {
            int loggedInUserId = SessionManager.getInstance().getLoggedUser().getUserId();

            FXMLLoader loader;
            if (userId == loggedInUserId) {
                loader = new FXMLLoader(SharedData.class.getResource("/profile.fxml"));
            } else {
                loader = new FXMLLoader(SharedData.class.getResource("/userProfile.fxml"));
            }

            Parent root = loader.load();

            if (userId == loggedInUserId) {
                ProfileController profileController = loader.getController();
                profileController.initialize();
                profileController.setMainController(new MainController());
            } else {
                UserProfileController userProfileController = loader.getController();
                userProfileController.initialize(userId);
                userProfileController.setMainController(new MainController());
            }

            primaryStage.setScene(new Scene(root, 360, ScreenUtil.getScaledHeight()));
            primaryStage.setTitle(userId == loggedInUserId ? "Profile Page" : "User Profile Page");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
