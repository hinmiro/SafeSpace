package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ScreenUtil;
import model.SessionManager;
import model.UserModel;
import view.View;
import java.io.IOException;

public class UserMessagesController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private View mainView;
    private int userId;
    private MainController mainController;

    @FXML
    private Label usernameLabelMessage;
    @FXML
    private VBox messageListVBox;
    @FXML
    private TextField messageTextField;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button closeButton;

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

        // väärä viel
        String username = SessionManager.getInstance().getLoggedUser().getUsername();
        usernameLabelMessage.setText(username);
        sendMessageButton.setOnAction(event -> handleSendMessage());
    }

    @FXML
    private void handleSendMessage() {
        String message = messageTextField.getText();
        UserModel receiver = controllerForView.getUserById(userId);

        if (receiver != null) {
            int receiverId = receiver.getUserId();

            if (!message.isEmpty()) {
                try {
                    boolean success = controllerForView.sendMessage(message, receiverId);
                    if (success) {
                        displayMessage(message);
                        messageTextField.clear();
                    } else {
                        showError("Sending the message failed.");
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    showError("An error occurred while sending the message.");
                }
            }
        } else {
            System.err.println("Error: Receiver not found.");
        }
    }

    private void displayMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("message-label");
        messageListVBox.getChildren().add(messageLabel);
    }

    @FXML
    private void handleClose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/messages.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Messages");
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            profileController.setMainView(mainView);
        }

        if (fxmlFile.equals("/main.fxml")) {
            MainController mainController = fxmlLoader.getController();
            mainController.setMainView(mainView);
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

