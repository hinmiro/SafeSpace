package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.*;
import view.View;
import java.io.*;
import java.util.*;

public class MessageController {

    @FXML
    private Button homeButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button leaveMessageButton;
    @FXML
    private Label noMessagesLabel;
    @FXML
    private ListView<Message> conversationListView;
    @FXML
    private ComboBox<String> languageBox;

    private View mainView;
    private MainController mainController;
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private SharedData language = SharedData.getInstance();
    private ResourceBundle bundle;
    private Locale currentLocale;


    @FXML
    private void initialize() {
        checkIfNoMessages();

        leaveMessageButton.setText("x");
        leaveMessageButton.setStyle("-fx-font-size: 16px;");

        leaveMessageButton.setOnAction(event -> {
            try {
                switchScene("/main.fxml", "Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

        loadMessages();

        conversationListView.setOnMouseClicked(event -> {
            Message selectedMessage = conversationListView.getSelectionModel().getSelectedItem();
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

    @FXML
    private void changeLanguage() {
        if (languageBox.getValue().equals(bundle.getString("language.fi"))) {
            currentLocale = Locale.forLanguageTag("fi");
        } else {
            currentLocale = Locale.forLanguageTag("en");
        }

        SharedData.getInstance().setCurrentLocale(currentLocale);
        bundle = ResourceBundle.getBundle("Messages", currentLocale);
        //updateTexts();
    }

    private void loadMessages() {
        conversationListView.getItems().clear();

        UserModel loggedInUser = SessionManager.getInstance().getLoggedUser();

        List<Conversation> userConversations = controllerForView.getMessages();

        Set<String> uniqueConversationPartners = new HashSet<>();

        for (Conversation conversation : userConversations) {
            UserModel withUser = conversation.getWithUser();

            String conversationPartner = withUser.getUsername();

            if (!conversationPartner.equals(loggedInUser.getUsername()) && uniqueConversationPartners.add(conversationPartner)) {
                List<Message> messages = conversation.getMessages();

                Message latestMessage = messages.stream()
                        .max(Comparator.comparing(Message::getDateOfMessage))
                        .orElse(null);

                if (latestMessage != null) {
                    conversationListView.getItems().add(latestMessage);
                }
            }
        }

        conversationListView.getItems().sort(Comparator.comparing(Message::getDateOfMessage).reversed());

        conversationListView.setCellFactory(param -> new MessageListCell());
        checkIfNoMessages();
    }

    private void openUserMessages(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/userMessages.fxml"));
            Parent root = loader.load();

            UserMessagesController userMessagesController = loader.getController();
            userMessagesController.setUserId(userId);
            userMessagesController.initialize(userId);

            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.setTitle("Messages");
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
            profileController.setMainController(mainController);
        }

        if (fxmlFile.equals("/main.fxml")) {
            MainController mainController = fxmlLoader.getController();
            mainController.setMainView(mainView);
        }
    }

    public void checkIfNoMessages() {
        if (conversationListView.getItems().isEmpty()) {
            noMessagesLabel.setVisible(true);
            conversationListView.setVisible(false);
        } else {
            noMessagesLabel.setVisible(false);
            conversationListView.setVisible(true);
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setMainView(View view) {
        this.mainView = view;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}
