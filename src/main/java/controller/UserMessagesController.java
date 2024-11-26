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
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for handling user messages.
 */
public class UserMessagesController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle alerts;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private View mainView;
    private int userId;
    private MainController mainController;
    private Conversation currentConversation;
    private static final Logger logger = Logger.getLogger(UserMessagesController.class.getName());

    @FXML private Label usernameLabelMessage;
    @FXML private VBox messageListVBox;
    @FXML private TextField messageTextField;
    @FXML private Button sendMessageButton;
    @FXML private Button closeButton;

    /**
     * Initializes the controller with the given user ID.
     * @param userId the ID of the user
     */
    public void initialize(int userId) {
        updateLanguage();

        this.userId = userId;
        fetchUser(userId);
        loadConversation();

        closeButton.setOnAction(event -> handleClose());
        sendMessageButton.setOnAction(event -> handleSendMessage());
        messageTextField.setOnKeyPressed(this::handleEnterKey);
    }

    /**
     * Updates the text of UI elements based on the selected language.
     */
    private void updateTexts() {
        messageTextField.setPromptText(fields.getString("sendMessage"));
    }

    /**
     * Updates the language of the application based on the selected locale.
     */
    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    /**
     * Handles the Enter key press event to send a message.
     * @param event the key event
     */
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSendMessage();
        }
    }

    /**
     * Fetches the user information based on the user ID.
     * @param userId the ID of the user
     */
    private void fetchUser(int userId) {
        UserModel user = controllerForView.getUserById(userId);
        if (user != null) {
            usernameLabelMessage.setText(user.getUsername());
        }
    }

    /**
     * Loads the conversation for the current user.
     */
    private void loadConversation() {
        currentConversation = controllerForView.getCurrentConversation(userId);
        if (currentConversation != null) {
            usernameLabelMessage.setText(currentConversation.getWithUser().getUsername());

            messageListVBox.getChildren().clear();
            for (Messages message : currentConversation.getMessages()) {
                displayMessage(message);
            }
        }
    }

    /**
     * Displays a message in the message list.
     * @param message the message to be displayed
     */
    private void displayMessage(Messages message) {
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

    /**
     * Handles the action of sending a message.
     */
    private void handleSendMessage() {
        String messageContent = messageTextField.getText();

        if (currentConversation == null) {
            UserModel receiver = controllerForView.getUserById(userId);
            currentConversation = new Conversation(receiver);
        }

        if (!messageContent.isEmpty()) {
            Messages message = new Messages("sent", messageContent, LocalDateTime.now().toString());
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
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                e.printStackTrace();
                showError(alerts.getString("error.message"));
            }
        }
    }

    /**
     * Handles the action of closing the user messages window.
     */
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
                logger.warning("Theme URL is null");
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the user ID.
     * @param userId the ID of the user
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Shows an error alert with the specified message.
     * @param message the message to be displayed in the alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alerts.getString("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}