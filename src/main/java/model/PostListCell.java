package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
            hBox.setPadding(new Insets(15));

            VBox contentBox = new VBox();
            contentBox.setSpacing(5);

            addUserInfo(contentBox, item);

            addPostDetails(contentBox, item);

            addBottomSection(contentBox, item);

            createLikeButton(item);

            createCommentButton(item);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            hBox.setOnMousePressed(evt -> {
                openPostDetailModal(item);
            });

            hBox.getChildren().addAll(contentBox, spacer);
            hBox.getStyleClass().add("post-cell");

            setGraphic(hBox);
        }
    }

    private void openPostDetailModal(Post item) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Post");

        VBox contentBox = new VBox();
        contentBox.setSpacing(5);
        contentBox.setPadding(new Insets(10));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setSpacing(10);

        Button closeButton = new Button("―");
        closeButton.setOnAction(e -> modalStage.close());
        closeButton.getStyleClass().add("close-button");
        buttonBox.getChildren().add(closeButton);

        contentBox.getChildren().add(buttonBox);

        addUserInfo(contentBox, item);

        Text postDate = new Text(item.getPostDate());
        postDate.getStyleClass().add("post-date");
        contentBox.getChildren().add(postDate);

        Text postContent = new Text(item.getPostContent());
        postContent.setWrappingWidth(200);
        postContent.getStyleClass().add("post-content");
        VBox.setMargin(postContent, new Insets(10, 0, 20, 0));
        contentBox.getChildren().add(postContent);

        addBottomSection(contentBox, item);

        addCommentSection(contentBox, item);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 300, 550);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }

    private void addUserInfo(VBox contentBox, Post item) {
        try {
            String username = softwareModel.getUsernameByPostCreatorID(item.getPostCreatorID());
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
    }

    private void addPostDetails(VBox contentBox, Post item) {
        Text postDate = new Text(item.getPostDate());
        postDate.getStyleClass().add("post-date");
        contentBox.getChildren().add(postDate);

        Text postContent = new Text(item.getPostContent());
        postContent.setWrappingWidth(300);
        postContent.getStyleClass().add("post-content");
        VBox.setMargin(postContent, new Insets(10, 0, 20, 0));
        contentBox.getChildren().add(postContent);
    }

    private void addBottomSection(VBox contentBox, Post item) {
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setSpacing(20);

        HBox likeBox = createLikeButton(item);

        HBox commentBox = createCommentButton(item);

        bottomBox.getChildren().addAll(likeBox, commentBox);
        contentBox.getChildren().add(bottomBox);

        Separator separator = new Separator();
        contentBox.getChildren().add(separator);
    }

    private HBox createLikeButton(Post item) {
        HBox likeBox = new HBox();
        likeBox.setAlignment(Pos.CENTER_LEFT);
        likeBox.setSpacing(5);

        Image thumbsUpImage = new Image("/kuvat/like.png");
        ImageView thumbsUpIcon = new ImageView(thumbsUpImage);
        thumbsUpIcon.setFitWidth(18);
        thumbsUpIcon.setFitHeight(18);

        Button likeButton = new Button();
        likeButton.setGraphic(thumbsUpIcon);
        likeButton.getStyleClass().add("like-button");

        Text likes = new Text(String.valueOf(item.getLikeCount()));
        likes.getStyleClass().add("like-text");

        likeButton.setOnAction(event -> {
            try {
                UserModel currentUser = SessionManager.getInstance().getLoggedUser();

                int currentUserId = currentUser.getUserId();

                if (item.isLikedByUser(currentUserId)) {
                    boolean likeRemoved = softwareModel.removeLike(String.valueOf(item.getPostID()));
                    if (likeRemoved) {
                        item.getLikers().remove(Integer.valueOf(currentUserId));
                        item.setLikeCount(item.getLikeCount() - 1);
                        likes.setText(String.valueOf(item.getLikeCount()));
                        item.setLikedByUser(false);
                        likeButton.setStyle("-fx-background-color: linear-gradient(to bottom, #85e4a4, #57b657);");
                    } else {
                        System.out.println("Tykkäyksen poistaminen epäonnistui.");
                    }
                } else {
                    boolean liked = softwareModel.likePost(String.valueOf(item.getPostID()));
                    if (liked) {
                        item.getLikers().add(currentUserId);
                        item.setLikeCount(item.getLikeCount() + 1);
                        likes.setText(String.valueOf(item.getLikeCount()));
                        item.setLikedByUser(true);
                    } else {
                        System.out.println("Tykkäys epäonnistui.");
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        likeBox.getChildren().addAll(likeButton, likes);
        return likeBox;
    }

    private HBox createCommentButton(Post item) {
        HBox commentBox = new HBox();
        commentBox.setAlignment(Pos.CENTER_LEFT);
        commentBox.setSpacing(5);

        Image commentImage = new Image("/kuvat/comment.png");
        ImageView commentIcon = new ImageView(commentImage);
        commentIcon.setFitWidth(18);
        commentIcon.setFitHeight(18);

        Button commentButton = new Button();
        commentButton.setGraphic(commentIcon);
        commentButton.getStyleClass().add("comment-button");

        Text comments = new Text(String.valueOf(item.getCommentCount()));
        comments.getStyleClass().add("comment-text");

        commentBox.getChildren().addAll(commentButton, comments);
        return commentBox;
    }

    private void addCommentSection(VBox contentBox, Post item) {
        VBox commentSection = new VBox();
        commentSection.setSpacing(10);
        commentSection.setPadding(new Insets(10));
        commentSection.getStyleClass().add("comment-section");

        VBox.setMargin(commentSection, new Insets(20, 0, 0, 0));


        // todo real comments
        Text commentText = new Text("This is a comment");
        commentText.getStyleClass().add("comment-text");
        commentSection.getChildren().add(commentText);

        contentBox.getChildren().add(commentSection);
    }
}
