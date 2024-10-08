package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;
import view.*;
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
    private Button closeButton;

    public void initialize(int userId) {
        this.userId = userId;
        fetchUser(userId);
        loadConversation();

        closeButton.setOnAction(event -> handleClose());
        sendMessageButton.setOnAction(event -> handleSendMessage());
        messageTextField.setOnKeyPressed(this::handleEnterKey);
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSendMessage();
        }
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

        int currentUserId = SessionManager.getInstance().getLoggedUser().getUserId();
        if (message.getSenderId() == currentUserId) {
            messageLabel.getStyleClass().add("message-label1");
        } else {
            messageLabel.getStyleClass().add("message-label2");
        }

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
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/messages.fxml"));
            Parent root = loader.load();

            MessageController messageController = loader.getController();
            messageController.setMainView(mainView);
            messageController.setMainController(mainController);

            Stage stage = new Stage();
            stage.setTitle("Messages");
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

