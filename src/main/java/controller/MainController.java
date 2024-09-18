package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PostListCell;
import model.SharedData;
import view.View;

import java.io.IOException;
import java.util.List;

public class MainController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private volatile boolean stopQueueProcessingFlag = true;
    private Thread queueThread;
    private View mainView;


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
    private Pane newWindow;
    @FXML
    private Label noPostsLabel;
    @FXML
    private ListView<String> feedListView;
    @FXML
    private VBox contentBox;

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
        checkIfNoPosts();
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

        Scene scene = new Scene(root, 360, 800);
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
            System.out.println("maini view " + mainView.getClass());
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
            Scene scene = new Scene(newPostPane);
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
            Scene scene = new Scene(newPostPane2);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkIfNoPosts() {
        if (SharedData.getInstance().getEventQueue().isEmpty()) {
            noPostsLabel.setVisible(true);
        } else noPostsLabel.setVisible(false);
    }


    private synchronized void startQueueProcessing() {
        stopQueueProcessingFlag = false;
        if (queueThread == null || !queueThread.isAlive()) {
            queueThread = new Thread(() -> {
                while (!stopQueueProcessingFlag) {
                    try {
                        String data = SharedData.getInstance().takeEvent();
                        Platform.runLater(() -> {
                            feedListView.getItems().add(data);
                            feedListView.scrollTo(feedListView.getItems().size() - 1);
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            queueThread.start();
        }
    }

    public synchronized void stopQueueProcessing() {
        stopQueueProcessingFlag = true;
        if (queueThread != null) {
            queueThread.interrupt();
        }
    }


    private void loadEvents() {
        List<String> events = SharedData.getInstance().getEvents();
        System.out.println(events);
        feedListView.getItems().setAll(events);
    }

    public void setMainView(View view) { mainView = view; }


}
