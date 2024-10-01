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
import model.Message;
import model.ScreenUtil;
import model.SessionManager;
import model.UserModel;
import view.View;
import java.io.IOException;
import java.util.List;

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

    public void initialize(int userId) {
        this.userId = userId;
        fetchUser(userId);
        loadConversation();

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

        closeButton.setOnAction(event -> handleClose());
        sendMessageButton.setOnAction(event -> handleSendMessage());
    }

    private void fetchUser(int userId) {
        UserModel user = controllerForView.getUserById(userId);
        if (user != null) {
            usernameLabelMessage.setText(user.getUsername());
        }
    }

    private void loadConversation() {
        int currentUserId = SessionManager.getInstance().getLoggedUser().getUserId();

        List<Message> messages = controllerForView.getMessages();
        System.out.println("Messages: " + messages);
        messageListVBox.getChildren().clear();

        for (Message message : messages) {
            if ((message.getSenderId() == currentUserId && message.getReceiverId() == userId) ||
                    (message.getSenderId() == userId && message.getReceiverId() == currentUserId)) {
                displayMessage(message);
            }
        }
    }

    private void displayMessage(Message message) {
        UserModel sender = controllerForView.getUserById(message.getSenderId());
        String username = sender.getUsername();
        Label messageLabel = new Label(username + ": " + message.getMessageContent());
        System.out.println("Message: " + message.getMessageContent());
        System.out.println("messagelabel: " + messageLabel);
        messageLabel.getStyleClass().add("message-label");
        messageListVBox.getChildren().add(messageLabel);
    }

    @FXML
    private void handleSendMessage() {
        String messageContent = messageTextField.getText();
        UserModel receiver = controllerForView.getUserById(userId);

        if (receiver != null) {
            int receiverId = receiver.getUserId();
            int senderId = SessionManager.getInstance().getLoggedUser().getUserId();
            String dateOfMessage = "2024-01-01"; //placeholderina

            if (!messageContent.isEmpty()) {
                try {
                    boolean success = controllerForView.sendMessage(messageContent, receiverId);

                    if (success) {
                        Message message = new Message(0, messageContent, senderId, receiverId, dateOfMessage);
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
        }}

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

