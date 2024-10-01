package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import view.View;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private VBox contentBox;
    @FXML
    private ListView<Message> conversationListView;

    private View mainView;
    private MainController mainController;
    private ControllerForView controllerForView = ControllerForView.getInstance();

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

    private void loadMessages() {
        List<Message> messages = controllerForView.getMessages();
        conversationListView.getItems().clear();

        Set<String> uniqueConversationPartners = new HashSet<>();
        UserModel loggedInUser = SessionManager.getInstance().getLoggedUser();

        for (Message message : messages) {
            UserModel sender = controllerForView.getUserById(message.getSenderId());
            UserModel receiver = controllerForView.getUserById(message.getReceiverId());

            if (sender != null && receiver != null) {
                if (whoIsMessaging(loggedInUser, sender, receiver)) {
                    String conversationPartner = (loggedInUser.getUserId() == sender.getUserId())
                            ? receiver.getUsername()
                            : sender.getUsername();

                    if (!conversationPartner.equals(loggedInUser.getUsername()) && uniqueConversationPartners.add(conversationPartner)) {
                        conversationListView.getItems().add(message);
                    }
                }
            }
        }

        conversationListView.setCellFactory(param -> new MessageListCell());
        checkIfNoMessages();
    }

    private boolean whoIsMessaging(UserModel loggedInUser, UserModel sender, UserModel receiver) {
        return sender.getUserId() == loggedInUser.getUserId() || receiver.getUserId() == loggedInUser.getUserId();
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
}
