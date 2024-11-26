package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import services.Theme;
import view.View;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for handling message-related functionalities in the application.
 */
public class MessageController {

    private View mainView;
    private MainController mainController;
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private static final Logger logger = Logger.getLogger(MessageController.class.getName());

    @FXML private Button homeButton;
    @FXML private Button profileButton;
    @FXML private Button leaveMessageButton;
    @FXML private Label noMessagesLabel;
    @FXML private ListView<Messages> conversationListView;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        updateLanguage();
        checkIfNoMessages();

        leaveMessageButton.setText("x");
        leaveMessageButton.setStyle("-fx-font-size: 16px;");

        ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
        leaveMessageButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", pageTitle.getString("main"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

        loadMessages();

        conversationListView.setOnMouseClicked(event -> {
            Messages selectedMessage = conversationListView.getSelectionModel().getSelectedItem();
            if (selectedMessage != null) {
                UserModel loggedInUser = SessionManager.getInstance().getLoggedUser();
                int conversationPartnerId;

                if (selectedMessage.getSenderId() == loggedInUser.getUserId()) {
                    conversationPartnerId = selectedMessage.getReceiverId();
                } else {
                    conversationPartnerId = selectedMessage.getSenderId();
                }

                openUserMessages(conversationPartnerId);
            }
        });
    }

    /**
     * Updates the text of UI elements based on the selected language.
     */
    private void updateTexts() {
        homeButton.setText(buttons.getString("home"));
        profileButton.setText(buttons.getString("profile"));
        noMessagesLabel.setText(labels.getString("noMessages"));
    }

    /**
     * Updates the language of the application based on the selected locale.
     */
    private void updateLanguage() {
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        updateTexts();
    }

    /**
     * Loads the messages into the conversation list view.
     */
    private void loadMessages() {
        conversationListView.getItems().clear();

        UserModel loggedInUser = SessionManager.getInstance().getLoggedUser();

        List<Conversation> userConversations = controllerForView.getMessages();

        Set<String> uniqueConversationPartners = new HashSet<>();

        for (Conversation conversation : userConversations) {
            UserModel withUser = conversation.getWithUser();

            String conversationPartner = withUser.getUsername();

            if (!conversationPartner.equals(loggedInUser.getUsername()) && uniqueConversationPartners.add(conversationPartner)) {
                List<Messages> messages = conversation.getMessages();

                Messages latestMessage = messages.stream()
                        .max(Comparator.comparing(Messages::getDateOfMessage))
                        .orElse(null);

                if (latestMessage != null) {
                    conversationListView.getItems().add(latestMessage);
                }
            }
        }

        conversationListView.getItems().sort(Comparator.comparing(Messages::getDateOfMessage).reversed());
        conversationListView.setCellFactory(param -> new MessageListCell());
        checkIfNoMessages();
    }

    /**
     * Opens the user messages window for the specified user ID.
     * @param userId the ID of the user whose messages are to be displayed
     */
    private void openUserMessages(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/userMessages.fxml"));
            Parent root = loader.load();

            UserMessagesController userMessagesController = loader.getController();
            userMessagesController.setUserId(userId);
            userMessagesController.initialize(userId);

            Stage stage = (Stage) homeButton.getScene().getWindow();

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                logger.warning("Theme URL is null");
            }

            stage.setScene(scene);

            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("messages"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches the scene to the specified FXML file and sets the title.
     * @param fxmlFile the FXML file to load
     * @param title the title of the new scene
     * @throws IOException if the FXML file cannot be loaded
     */
    private void switchScene(String fxmlFile, String title) throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());

        URL themeUrl = getClass().getResource(Theme.getTheme());
        if (themeUrl != null) {
            scene.getStylesheets().add(themeUrl.toExternalForm());
        } else {
            logger.warning("Theme URL is null");
        }

        stage.setScene(scene);
        stage.setTitle(title);

        if (fxmlFile.equals("/profile.fxml")) {
            ProfileController profileController = fxmlLoader.getController();
            profileController.setMainView(mainView);
            profileController.setMainController(mainController);
        }

        if (fxmlFile.equals("/main.fxml")) {
            MainController mainControllerLoader = fxmlLoader.getController();
            mainControllerLoader.setMainView(mainView);
        }
    }

    /**
     * Checks if there are no messages and updates the UI accordingly.
     */
    public void checkIfNoMessages() {
        if (conversationListView.getItems().isEmpty()) {
            noMessagesLabel.setVisible(true);
            conversationListView.setVisible(false);
        } else {
            noMessagesLabel.setVisible(false);
            conversationListView.setVisible(true);
        }
    }

    /**
     * Sets the main controller of the application.
     * @param mainController the main controller to be set
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Sets the main view of the application.
     * @param view the main view to be set
     */
    public void setMainView(View view) {
        this.mainView = view;
    }
}