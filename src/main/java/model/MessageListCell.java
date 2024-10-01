package model;

import controller.ControllerForView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class MessageListCell extends ListCell<Message> {
    private ControllerForView controllerForView = ControllerForView.getInstance();
    private HBox hBox = new HBox();
    private Label usernameLabel = new Label();

    public MessageListCell() {
        hBox.getChildren().addAll(usernameLabel);
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            UserModel receiver = controllerForView.getUserById(item.getReceiverId());
            if (receiver != null) {
                usernameLabel.setText(receiver.getUsername());
                usernameLabel.getStyleClass().add("username-label");
            } else {
                usernameLabel.setText("Unknown");
            }

            hBox.getStyleClass().add("message-list-cell");
            setGraphic(hBox);
        }
    }

}
