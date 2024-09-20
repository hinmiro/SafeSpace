package model;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PostListCell extends ListCell<Post> {
    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            HBox hBox = new HBox();
            hBox.setOnMousePressed(evt -> {
                System.out.println("clicked add modal here to open post content " + item.getPostID());
            });
            Text postContent = new Text(item.getPostContent());
            Text likes = new Text("Likes: " + item.getLikeCount());
            Button likeButton = new Button("👍");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hBox.getChildren().addAll(postContent,spacer, likes);
            setGraphic(hBox);
        }
    }
}
