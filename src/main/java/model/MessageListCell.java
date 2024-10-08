package model;

import controller.ControllerForView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class MessageListCell extends ListCell<Message> {
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private HBox hBox = new HBox();
    private Label usernameLabel = new Label();
    private Label messageLabel = new Label();

    public MessageListCell() {
        hBox.getChildren().addAll(usernameLabel, messageLabel);
        hBox.getStyleClass().add("message-list-cell");
    }

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if (empty || message == null) {
            setText(null);
            setGraphic(null);
        } else {
            UserModel sender = controllerForView.getUserById(message.getSenderId());
            UserModel receiver = controllerForView.getUserById(message.getReceiverId());

            if (sender != null && receiver != null) {
                String displayUsername = sender.getUsername().equals(SessionManager.getInstance().getLoggedUser().getUsername())
                        ? receiver.getUsername()
                        : sender.getUsername();
                usernameLabel.setText(displayUsername + ": ");
                usernameLabel.getStyleClass().add("username-labelmsg");

                messageLabel.setText(message.getMessageContent());
                messageLabel.setWrapText(true);
                messageLabel.getStyleClass().add("message-labelmsg");
            } else {
                usernameLabel.setText("Unknown User");
                messageLabel.setText(message.getMessageContent());
            }

            setGraphic(hBox);
        }
    }
}

