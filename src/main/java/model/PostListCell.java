package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class PostListCell extends ListCell<Post> {
    private SoftwareModel softwareModel = new SoftwareModel();

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPadding(new Insets(10));
            hBox.setOnMousePressed(evt -> {
                System.out.println("clicked add modal here to open post content " + item.getPostID());
            });


            VBox contentBox = new VBox();
            contentBox.setSpacing(5);

            try {
                String username = softwareModel.getUsernameByPostCreatorID(item.getPostCreatorID());
                //System.out.println("username: " + username);
                //System.out.println("item.getPostCreatorID(): " + item.getPostCreatorID());

                VBox usernameBox = new VBox();
                usernameBox.setPadding(new Insets(5));
                usernameBox.getStyleClass().add("username-box");

                Text usernameText = new Text(username);
                usernameText.getStyleClass().add("username");
                usernameBox.getChildren().add(usernameText);

                contentBox.getChildren().add(usernameBox);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            Text postContent = new Text(item.getPostContent());
            postContent.setWrappingWidth(250);
            postContent.getStyleClass().add("post-content");

            contentBox.getChildren().addAll(postContent);

            VBox likeBox = new VBox();
            likeBox.setAlignment(Pos.CENTER_RIGHT);
            Text likes = new Text("Likes: " + item.getLikeCount());
            likes.getStyleClass().add("like-text");

            Image thumbsUpImage = new Image("/kuvat/like.png");
            ImageView thumbsUpIcon = new ImageView(thumbsUpImage);
            thumbsUpIcon.setFitWidth(18);
            thumbsUpIcon.setFitHeight(18);

            Button likeButton = new Button();
            likeButton.setGraphic(thumbsUpIcon);
            likeButton.getStyleClass().add("like-button");

            likeBox.getChildren().addAll(likeButton, likes);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            hBox.getChildren().addAll(contentBox, spacer, likeBox);
            hBox.getStyleClass().add("post-cell");

            setGraphic(hBox);
        }
    }
}