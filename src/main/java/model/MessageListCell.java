package model;

import controller.ControllerForView;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;

/**
 * The MessageListCell class extends ListCell to display messages in a custom format.
 */
public class MessageListCell extends ListCell<Messages> {
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private HBox hBox = new HBox();
    private Label usernameLabel = new Label();
    private Label messageLabel = new Label();

    /**
     * Constructs a new MessageListCell instance and initializes the layout.
     */
    public MessageListCell() {
        hBox.getChildren().addAll(usernameLabel, messageLabel);
        hBox.getStyleClass().add("message-list-cell");
    }

    /**
     * Updates the item in the cell with the provided message.
     *
     * @param message the message to display in the cell
     * @param empty whether the cell is empty
     */
    @Override
    protected void updateItem(Messages message, boolean empty) {
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
            }

            setGraphic(hBox);
        }
    }
}