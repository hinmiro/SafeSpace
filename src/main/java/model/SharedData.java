package model;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.Theme;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class SharedData {
    private static SharedData instance;
    private static Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

    private BlockingQueue<Post> postQueue;
    private BlockingQueue<Like> likeQueue;
    private BlockingQueue<Like> removedLikeQueue;

    private ArrayList<Post> posts;
    private ArrayList<Like> likes;

    // This is used for getting events and saving them trough SSH connection, even if user is not in
    //  main page.

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

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = SharedData.class.getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                System.err.println("Theme URL is null");
            }

            primaryStage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            primaryStage.setTitle(userId == loggedInUserId ? pageTitle.getString("profile") : pageTitle.getString("userProfile"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Post> getFollowedPosts() {
        ArrayList<Post> followedPosts = new ArrayList<>();

        ArrayList<Integer> followingUserIds = SessionManager.getInstance().getLoggedUser().getUserData().getFollowingUserIds();
        ArrayList<Integer> friendIds = SessionManager.getInstance().getLoggedUser().getUserData().getFriendsIds();

        for (Post post : posts) {
            if (followingUserIds.contains(post.getPostCreatorID()) || friendIds.contains(post.getPostCreatorID())) {
                followedPosts.add(post);
            }
        }
        return followedPosts;
    }
}
