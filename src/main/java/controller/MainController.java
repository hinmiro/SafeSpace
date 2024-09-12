package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.PostListCell;
import model.SharedData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MainController {

    private ControllerForView controllerForView;
    private BlockingQueue<String> feedQueue = ControllerForView.feedQueue;
    private volatile boolean stopQueueProcessing = false;


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
    private ListView<String> feedListView;

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
        processQueue();
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
        stopQueueProcessing = true;
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        if (fxmlFile.equals("/profile.fxml")) {
            ProfileController profileController = fxmlLoader.getController();
            profileController.setControllerForView(controllerForView);
        }

        Scene scene = new Scene(root, 350, 550);
        stage.setScene(scene);
        stage.setTitle(title);

        if (fxmlFile.equals("/main.fxml")) {
            MainController mainController = fxmlLoader.getController();
            mainController.loadEvents();
            mainController.processQueue();
        }
    }

    private void showNewPostWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPost.fxml"));
            Parent newPostPane = loader.load();

            NewPostController newPostController = loader.getController();
            newPostController.setControllerForView(controllerForView);
            newPostController.setMainController(this);

            Stage stage = (Stage) newWindow.getScene().getWindow();
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

            NewPostController newPostController = loader.getController();
            newPostController.setControllerForView(controllerForView);
            newPostController.setMainController(this);

            Stage stage = (Stage) newWindow.getScene().getWindow();
            Scene scene = new Scene(newPostPane2);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllerForView(ControllerForView controller) {
        this.controllerForView = controller;
    }

    private void processQueue() {
        if (stopQueueProcessing) {
            return;
        }

        new Thread(() -> {
            while (!stopQueueProcessing) {
                try {
                    String data = SharedData.getInstance().takeEvent();
                    System.out.println(data);
                    Platform.runLater(() -> {
                        feedListView.getItems().add(data);
                        feedListView.setOnMouseClicked((evt) -> {
                            evt.consume();
                            System.out.println("click: " + feedListView.getSelectionModel().getSelectedItem());
                        });
                        feedListView.scrollTo(feedListView.getItems().size() -1);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadEvents() {
        List<String> events = SharedData.getInstance().getEvents();
        feedListView.getItems().setAll(events);
    }

}
