package model;

import controller.UserProfileController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SharedData {
    private static SharedData instance;

    private List<LikeListener> likeListeners;

    private BlockingQueue<Post> postQueue;
    private BlockingQueue<Like> likeQueue;

    private ArrayList<Post> posts;
    private ArrayList<Like> likes;


    private SharedData() {
        postQueue = new LinkedBlockingQueue<>();
        likeQueue = new LinkedBlockingQueue<>();
        likeListeners = new ArrayList<>();
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void addLike(Like like) {
        likeQueue.add(like);
        notifyLikeListeners(like);
    }

    public Post takeEvent() throws InterruptedException {
        Post post = postQueue.take();
        posts.add(post);
        return post;
    }

    public Like takeLike() throws InterruptedException {
        Like like = likeQueue.take();
        System.out.println("taking like " + like.getUserId());
        likes.add(like);
        return like;
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
            FXMLLoader loader = new FXMLLoader(SharedData.class.getResource("/userProfile.fxml"));
            Parent root = loader.load();

            UserProfileController controller = loader.getController();
            controller.initialize(userId);

            primaryStage.setScene(new Scene(root, 360, ScreenUtil.getScaledHeight()));
            primaryStage.setTitle("User Profile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerLikeListener(LikeListener listener) {
        likeListeners.add(listener);
    }

    public void unregisterLikeListener(LikeListener listener) {
        likeListeners.remove(listener);
    }

    private void notifyLikeListeners(Like like) {
        for (LikeListener listener : likeListeners) {
            listener.onLikeAdded(like);
        }
    }


}
