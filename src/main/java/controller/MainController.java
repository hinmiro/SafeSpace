package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private volatile boolean stopQueueProcessingFlag = true;
    private Thread queueThread;
    private View mainView;
    private ArrayList<Post> posts;
    private ImageView coggerImageView;
    private Stage coggerStage;

    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button newPostButton;
    @FXML
    private Button leaveMessageButton;
    @FXML
    private HBox postMenu;
    @FXML
    private Button createPicPostButton;
    @FXML
    private Button createTextPostButton;
    @FXML
    private Label noPostsLabel;
    @FXML
    private ListView<Post> feedListView;
    @FXML
    private VBox contentBox;
    @FXML
    private Label loadingBox;

    public MainController() {
        this.posts = new ArrayList<>();
    }

    @FXML
    private void initialize() {
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

        newPostButton.setOnAction(event -> togglePostMenu());

        createPicPostButton.setOnAction(event -> openPicPostForm());
        createTextPostButton.setOnAction(event -> openTextPostForm());

        feedListView.setCellFactory(param -> new PostListCell());
        loadEvents();

        if (stopQueueProcessingFlag) {
            startQueueProcessing();
            stopQueueProcessingFlag = false;
        }
    }

    private void togglePostMenu() {
        postMenu.setVisible(!postMenu.isVisible());
    }

    private void openPicPostForm() {
        showNewPostWindow();
    }

    private void openTextPostForm() {
        showNewTextWindow();
    }

    protected void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
        stage.setScene(scene);
        stage.setTitle(title);


        if (fxmlFile.equals("/profile.fxml")) {
            stopQueueProcessing();

            ProfileController profileController = fxmlLoader.getController();
            profileController.setMainController(this);
            profileController.setMainView(mainView);
            profileController.setDialogStage(stage);

        }

        if (fxmlFile.equals("/main.fxml")) {
            MainController mainController = fxmlLoader.getController();
            mainController.loadEvents();
            mainController.stopQueueProcessingFlag = false;
            mainController.startQueueProcessing();
        }

        if (fxmlFile.equals("/messages.fxml")) {
            stopQueueProcessing();

            MessageController messageController = fxmlLoader.getController();
            messageController.setMainView(mainView);
            messageController.setMainController(this);

        }
    }

    private void showNewPostWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPost.fxml"));
            Parent newPostPane = loader.load();

            NewPostController newPostController = loader.getController();
            newPostController.setMainController(this);

            Stage stage = (Stage) feedListView.getScene().getWindow();
            stage.setResizable(false);
            Scene scene = new Scene(newPostPane, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNewTextWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newText.fxml"));
            Parent newPostPane2 = loader.load();

            NewTextController newTextController = loader.getController();
            newTextController.setMainController(this);

            Stage stage = (Stage) feedListView.getScene().getWindow();
            stage.setResizable(false);
            Scene scene = new Scene(newPostPane2, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void startQueueProcessing() {
        stopQueueProcessingFlag = false;
        loadingBox.setVisible(true);
        loadingBox.setManaged(true);

        if (queueThread == null || !queueThread.isAlive()) {
            queueThread = new Thread(() -> {
                while (!stopQueueProcessingFlag) {
                    try {
                        boolean processed = false;

                        // Process all available posts
                        Post post;
                        while ((post = SharedData.getInstance().getEventQueue().poll(100, TimeUnit.MICROSECONDS)) != null) {
                            loadingBox.setVisible(true);
                            processed = true;
                            Post finalPost = post;
                            Platform.runLater(() -> {
                                loadingBox.setVisible(false);
                                loadingBox.setManaged(false);
                                feedListView.getItems().add(finalPost);
                                feedListView.scrollTo(feedListView.getItems().size() - 1);
                            });
                        }
                        // Process all available likes
                        Like like;
                        while ((like = SharedData.getInstance().getLikeQueue().poll(100, TimeUnit.MICROSECONDS)) != null) {
                            processed = true;
                            Like finalLike = like;
                            Platform.runLater(() -> handleLikeAdded(finalLike));
                        }

                        // process all available removed likes
                        while ((like = SharedData.getInstance().getRemovedLikeQueue().poll(100, TimeUnit.MICROSECONDS)) != null) {
                            processed = true;
                            Like finalLike = like;
                            Platform.runLater(() -> handleLikeRemoved(finalLike));

                        }

                        if (!processed) {
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            queueThread.start();
            loadingBox.setVisible(false);
            loadingBox.setManaged(false);
        }
    }

    public synchronized void stopQueueProcessing() {
        stopQueueProcessingFlag = true;
        if (queueThread != null) {
            queueThread.interrupt();
        }
    }

    public void loadEvents() {
        feedListView.getItems().setAll(SharedData.getInstance().getPosts());
        feedListView.scrollTo(feedListView.getItems().size() - 1);

    }

    public void handleLikeAdded(Like like) {
        int i = 0;
        for (Post post : feedListView.getItems()) {
            if (post.getPostID() == like.getPostId()) {
                post.setLikeCount(post.getLikeCount() + 1);
                feedListView.getItems().set(i, post);
                feedListView.refresh();
                break;
            }
            i++;
        }
    }

    public void handleLikeRemoved(Like like) {
        int i = 0;
        for (Post post : feedListView.getItems()) {
            if (post.getPostID() == like.getPostId()) {
                post.setLikeCount(post.getLikeCount() - 1);
                feedListView.getItems().set(i, post);
                feedListView.refresh();
                break;
            }
            i++;
        }
    }

    public void setMainView(View view) {
        mainView = view;
    }
}
