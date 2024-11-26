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
import java.util.logging.Logger;

public class SharedData {
    private static SharedData instance;
    private static Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(SharedData.class.getName());

    private BlockingQueue<Post> postQueue;
    private BlockingQueue<Like> likeQueue;
    private BlockingQueue<Like> removedLikeQueue;

    private ArrayList<Post> posts;
    private ArrayList<Like> likes;

    /**
     * Private constructor to initialize the queues and lists.
     */
    private SharedData() {
        postQueue = new LinkedBlockingQueue<>();
        likeQueue = new LinkedBlockingQueue<>();
        removedLikeQueue = new LinkedBlockingQueue<>();
        posts = new ArrayList<>();
        likes = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of SharedData.
     *
     * @return the singleton instance
     */
    public static synchronized SharedData getInstance() {
        instance = instance == null ? new SharedData() : instance;
        return instance;
    }

    /**
     * Adds a post event to the queue and list.
     *
     * @param event the post event to add
     */
    public void addEvent(Post event) {
        try {
            postQueue.put(event);
            posts.add(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Adds a like to the queue and list.
     *
     * @param like the like to add
     */
    public void addLike(Like like) {
        likeQueue.add(like);
        likes.add(like);
    }

    /**
     * Returns the queue of removed likes.
     *
     * @return the queue of removed likes
     */
    public BlockingQueue<Like> getRemovedLikeQueue() {
        return removedLikeQueue;
    }

    /**
     * Adds a removed like to the queue.
     *
     * @param like the like to remove
     */
    public void addRemoveLike(Like like) {
        removedLikeQueue.add(like);
    }

    /**
     * Returns the queue of post events.
     *
     * @return the queue of post events
     */
    public BlockingQueue<Post> getEventQueue() {
        return postQueue;
    }

    /**
     * Returns the queue of likes.
     *
     * @return the queue of likes
     */
    public BlockingQueue<Like> getLikeQueue() { return likeQueue; }

    /**
     * Returns the list of posts.
     *
     * @return the list of posts
     */
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * Creates a clickable username label.
     *
     * @param username the username to display
     * @param userId the ID of the user
     * @param primaryStage the primary stage of the application
     * @param modalStage the modal stage to close on click
     * @return the clickable username label
     */
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

    /**
     * Opens the user profile in a new scene.
     *
     * @param primaryStage the primary stage of the application
     * @param userId the ID of the user to display
     */
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
                logger.info("Theme URL is null");
            }

            primaryStage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            primaryStage.setTitle(userId == loggedInUserId ? pageTitle.getString("profile") : pageTitle.getString("userProfile"));
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of posts from followed users.
     *
     * @return the list of followed posts
     */
    public List<Post> getFollowedPosts() {
        ArrayList<Post> followedPosts = new ArrayList<>();

        List<Integer> followingUserIds = SessionManager.getInstance().getLoggedUser().getUserData().getFollowingUserIds();
        List<Integer> friendIds = SessionManager.getInstance().getLoggedUser().getUserData().getFriendsIds();

        for (Post post : posts) {
            if (followingUserIds.contains(post.getPostCreatorID()) || friendIds.contains(post.getPostCreatorID())) {
                followedPosts.add(post);
            }
        }
        return followedPosts;
    }
}