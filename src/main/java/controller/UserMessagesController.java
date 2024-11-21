package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;
import services.Theme;
import view.*;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserMessagesController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private View mainView;
    private int userId;
    private MainController mainController;
    private Conversation currentConversation;

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
        updateLanguage();

        this.userId = userId;
        fetchUser(userId);
        loadConversation();

        closeButton.setOnAction(event -> handleClose());
        sendMessageButton.setOnAction(event -> handleSendMessage());
        messageTextField.setOnKeyPressed(this::handleEnterKey);
    }

    private void updateTexts() {
        messageTextField.setPromptText(fields.getString("sendMessage"));
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
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
        currentConversation = controllerForView.getCurrentConversation(userId);
        if (currentConversation != null) {
            usernameLabelMessage.setText(currentConversation.getWithUser().getUsername());

            messageListVBox.getChildren().clear();
            for (Message message : currentConversation.getMessages()) {
                displayMessage(message);
            }
        }
    }

    private void displayMessage(Message message) {
        currentConversation = controllerForView.getCurrentConversation(userId);
        String username = message.getType().equals("sent") ?
                SessionManager.getInstance().getLoggedUser().getUsername() : currentConversation.getWithUser().getUsername();

        Label messageLabel = new Label(username + ": " + message.getMessageContent());
        messageLabel.setWrapText(true);

        if (message.getType().equals("sent")) {
            messageLabel.getStyleClass().add("message-label1");
        } else {
            messageLabel.getStyleClass().add("message-label2");
        }

        messageListVBox.getChildren().add(messageLabel);
    }

    private void handleSendMessage() {
        String messageContent = messageTextField.getText();

        if (currentConversation == null) {
            UserModel receiver = controllerForView.getUserById(userId);
            currentConversation = new Conversation(receiver);
        }

        if (!messageContent.isEmpty()) {
            Message message = new Message("sent", messageContent, LocalDateTime.now().toString());
            message.setSenderId(SessionManager.getInstance().getLoggedUser().getUserId());
            message.setReceiverId(currentConversation.getWithUser().getUserId());

            try {
                boolean success = controllerForView.sendMessage(message);
                if (success) {
                    currentConversation.addMessage(message);
                    displayMessage(message);
                    messageTextField.clear();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                showError(alerts.getString("error.message"));
            }
        }
    }

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
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("messages"));
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                System.err.println("Theme URL is null");
            }
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
        alert.setTitle(alerts.getString("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

