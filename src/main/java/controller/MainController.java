package controller;

import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;
import services.Theme;
import view.View;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static model.SharedData.createClickableUsername;

public class MainController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private volatile boolean stopQueueProcessingFlag = true;
    private Thread queueThread;
    private View mainView;
    private ArrayList<Post> posts;
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

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
    private ListView<Post> feedListView;
    @FXML
    private Label loadingBox;
    @FXML
    private TextField usernameSearchField;
    @FXML
    private Button searchButton;
    @FXML
    private VBox searchResultsBox;

    public MainController() {
        this.posts = new ArrayList<>();
    }

    @FXML
    private void initialize() {
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

        newPostButton.setOnAction(event -> togglePostMenu());

        createPicPostButton.setOnAction(event -> openPicPostForm());
        createTextPostButton.setOnAction(event -> openTextPostForm());

        feedListView.setCellFactory(param -> new PostListCell());
        loadEvents();

        if (stopQueueProcessingFlag) {
            startQueueProcessing();
            stopQueueProcessingFlag = false;
        }

        searchButton.setOnAction(event -> handleSearch());
    }

    private void updateTexts() {
        homeButton.setText(buttons.getString("home"));
        profileButton.setText(buttons.getString("profile"));
        createPicPostButton.setText(buttons.getString("createPicPost"));
        createTextPostButton.setText(buttons.getString("createTextPost"));
        usernameSearchField.setPromptText(fields.getString("searchUsername"));
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    private void togglePostMenu() {
        postMenu.setVisible(!postMenu.isVisible());
        postMenu.setManaged(!postMenu.isManaged());
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

        Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
        scene.getStylesheets().add(getClass().getResource(Theme.getTheme()).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(title);
    }

    @FXML
    private void handleSearch() {
        String username = usernameSearchField.getText().trim();
        UserModel user = controllerForView.getUserByName(username);

        searchResultsBox.getChildren().clear();

        if (user != null) {
            Label usernameSearch = createClickableUsername(user.getUsername(), user.getUserId(), (Stage) feedListView.getScene().getWindow(), new Stage());
            usernameSearch.getStyleClass().add("usernameSearchLabel");
            searchResultsBox.getChildren().add(usernameSearch);
        } else {
            Label noResults = new Label(labels.getString("noResults"));
            noResults.getStyleClass().add("noResultsLabel");
            searchResultsBox.getChildren().add(noResults);
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
            scene.getStylesheets().add(getClass().getResource(Theme.getTheme()).toExternalForm());
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
            scene.getStylesheets().add(getClass().getResource(Theme.getTheme()).toExternalForm());
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
                        loadingBox.setVisible(false);
                        loadingBox.setManaged(false);

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
