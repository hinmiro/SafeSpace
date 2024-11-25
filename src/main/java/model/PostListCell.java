package model;

import controller.ControllerForView;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import services.Theme;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class PostListCell extends ListCell<Post> {
    private final SoftwareModel softwareModel = new SoftwareModel();
    private final ResourceBundle buttons;
    private final ResourceBundle labels;
    private final ResourceBundle fields;
    private Locale locale;
    private static final Logger logger = Logger.getLogger(PostListCell.class.getName());

    public PostListCell() {
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        buttons = ResourceBundle.getBundle("buttons", locale);
        labels = ResourceBundle.getBundle("labels", locale);
        fields = ResourceBundle.getBundle("fields", locale);
    }

    @Override
    public void updateItem(Post item, boolean empty) {
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

            hBox.setOnMousePressed(evt ->
                openPostDetailModal(item, (Stage) hBox.getScene().getWindow())
            );

            hBox.getChildren().addAll(contentBox, spacer);
            hBox.getStyleClass().add("post-cell");

            setGraphic(hBox);
        }
    }

    private void openPostDetailModal(Post item, Stage primaryStage) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);

        InputStream logoStream = getClass().getResourceAsStream("/kuvat/safespacelogo.png");
        if (logoStream != null) {
            primaryStage.getIcons().add(new Image(logoStream));
        } else {
            logger.warning("Logo stream is null");
        }

        VBox contentBox = new VBox();
        contentBox.setSpacing(5);
        contentBox.setPadding(new Insets(10));
        contentBox.setId("detailedPost-contentBox");

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setSpacing(10);
        contentBox.setId("detailedPost-contentBox");

        Button closeButton = new Button("―");
        closeButton.setOnAction(e -> modalStage.close());
        closeButton.getStyleClass().add("close-button");
        buttonBox.getChildren().add(closeButton);

        contentBox.getChildren().add(buttonBox);

        VBox usernameBox = new VBox();
        usernameBox.setPadding(new Insets(5));
        usernameBox.getStyleClass().add("detail-username-box");
        Label usernameLabel = SharedData.createClickableUsername(item.getPostCreatorName(), item.getPostCreatorID(), primaryStage, modalStage);
        usernameLabel.getStyleClass().add("detail-username");

        usernameBox.getChildren().add(usernameLabel);
        contentBox.getChildren().add(usernameBox);

        Text postDate = new Text(item.getPostDate());
        postDate.getStyleClass().add("detail-post-date");
        contentBox.getChildren().add(postDate);

        if (item.getPostPictureID() != null) {
            addPostImage(contentBox, item);
        }

        Text postContent = new Text(item.getPostContent());
        postContent.setWrappingWidth(200);
        postContent.getStyleClass().add("detail-post-content");
        VBox.setMargin(postContent, new Insets(10, 0, 20, 0));
        contentBox.getChildren().add(postContent);

        addBottomSectionModal(contentBox, item);
        getComments(contentBox, item);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 300, 550);

        URL themeUrl = SharedData.class.getResource(Theme.getTheme());
        if (themeUrl != null) {
            scene.getStylesheets().add(themeUrl.toExternalForm());
        } else {
            logger.warning("Theme URL is null");
        }

        modalStage.setScene(scene);
        modalStage.setTitle(labels.getString("postDetailTitle"));
        modalStage.showAndWait();
    }

    private void addUserInfo(VBox contentBox, Post item) {
        VBox usernameBox = new VBox();
        usernameBox.setPadding(new Insets(5));
        usernameBox.getStyleClass().add("detail-username-box");

        Text usernameText = new Text(item.getPostCreatorName());
        usernameText.getStyleClass().add("detail-username");
        usernameBox.getChildren().add(usernameText);

        contentBox.getChildren().add(usernameBox);
    }

    private void addPostDetails(VBox contentBox, Post item) {
        Text postDate = new Text(item.getPostDate());
        postDate.getStyleClass().add("detail-post-date");
        contentBox.getChildren().add(postDate);

        if (item.getPostPictureID() != null) {
            addPostImage(contentBox, item);
        }

        Text postContent = new Text(item.getPostContent());
        postContent.setWrappingWidth(300);

        postContent.getStyleClass().add("detail-post-content");
        VBox.setMargin(postContent, new Insets(10, 0, 20, 0));
        contentBox.getChildren().add(postContent);
    }

    private void addBottomSection(VBox contentBox, Post item) {
        VBox bottomBox = createBottomSection(item, true);
        contentBox.getChildren().add(bottomBox);
    }

    private void addBottomSectionModal(VBox contentBox, Post item) {
        VBox bottomBox = createBottomSection(item, false);
        contentBox.getChildren().add(bottomBox);
    }

    private VBox createBottomSection(Post item, boolean isModal) {
        VBox bottomBox = new VBox();
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setSpacing(10);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setSpacing(20);

        HBox likeBox = createLikeButton(item);
        HBox commentBox = createCommentButton(item);

        buttonBox.getChildren().addAll(likeBox, commentBox);
        bottomBox.getChildren().add(buttonBox);

        VBox commentInputBox = new VBox();
        commentInputBox.setSpacing(10);
        commentInputBox.setVisible(!isModal);
        commentInputBox.setPrefHeight(isModal ? 0 : Region.USE_COMPUTED_SIZE);
        commentInputBox.setMinHeight(isModal ? 0 : Region.USE_COMPUTED_SIZE);

        TextField commentInput = new TextField();
        commentInput.setPromptText(fields.getString("commentHere"));

        Button addCommentButton = new Button(buttons.getString("addComment"));
        addCommentButton.getStyleClass().add("comment-button2");

        addCommentButton.setOnAction(event -> {
            String comment = commentInput.getText().trim();
            if (!comment.isEmpty()) {
                handleComment(comment, item.getPostID());
                commentInput.clear();
            }

            commentInputBox.setVisible(false);
            commentInputBox.setPrefHeight(0);
            commentInputBox.setMinHeight(0);
            bottomBox.requestLayout();
        });

        commentInputBox.getChildren().addAll(commentInput, addCommentButton);
        bottomBox.getChildren().add(commentInputBox);

        Separator separator = new Separator();
        bottomBox.getChildren().add(separator);

        ((Button) commentBox.getChildren().get(0)).setOnAction(event -> {
            boolean isVisible = !commentInputBox.isVisible();
            commentInputBox.setVisible(isVisible);
            commentInputBox.setPrefHeight(isVisible ? Region.USE_COMPUTED_SIZE : 0);
            commentInputBox.setMinHeight(isVisible ? Region.USE_COMPUTED_SIZE : 0);
            bottomBox.requestLayout();
        });

        return bottomBox;
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
                if (SessionManager.getInstance().getLoggedUser().getLikedPosts().contains(item.getPostID())) {
                    boolean likeRemoved = softwareModel.removeLike(String.valueOf(item.getPostID()));
                    if (likeRemoved) {
                        SessionManager.getInstance().getLoggedUser().removeLikedPost(item.getPostID());
                        likes.setText(String.valueOf(item.getLikeCount()));
                        likeButton.setStyle("-fx-background-color: linear-gradient(to bottom, #85e4a4, #57b657);");
                    }
                } else {
                    boolean liked = softwareModel.likePost(String.valueOf(item.getPostID()));
                    if (liked) {
                        SessionManager.getInstance().getLoggedUser().addLikedPost(item.getPostID());
                        likes.setText(String.valueOf(item.getLikeCount() + 1));
                        item.setLikedByUser(true);
                    }
                }
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
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

    private void getComments(VBox contenbox, Post item) {
        VBox commentSection = new VBox();
        commentSection.setSpacing(10);
        commentSection.setPadding(new Insets(10));
        commentSection.getStyleClass().add("comment-section");
        commentSection.setPrefWidth(200);

        ArrayList<Comment> comments = ControllerForView.getInstance().getPostCommentsById(String.valueOf(item.getPostID()));
        if (comments != null && !comments.isEmpty()) {
            comments.forEach(comment -> {
                Text commentContent = new Text(comment.getCommentContent());
                commentContent.getStyleClass().add("comment-text");
                commentContent.setWrappingWidth(200);

                VBox usernameBox = new VBox();
                usernameBox.setPadding(new Insets(5));
                usernameBox.getStyleClass().add("username-box");

                Text commentAuthor = new Text("- " + comment.getUsername());
                commentAuthor.getStyleClass().add("comment-author");
                usernameBox.getChildren().add(commentAuthor);

                VBox commentBox = new VBox();
                commentBox.getChildren().addAll(commentContent, usernameBox);
                commentBox.setPadding(new Insets(5));
                commentBox.getStyleClass().add("comment-box");

                VBox.setMargin(usernameBox, new Insets(10, 0, 0, 0));

                commentSection.getChildren().add(commentBox);
            });
        } else {
            Text commentText = new Text(labels.getString("noComments"));
            commentText.getStyleClass().add("comment-text");
            commentSection.getChildren().add(commentText);
        }

        contenbox.getChildren().add(commentSection);
    }

    private void handleComment(String text, int postId) {
        ControllerForView.getInstance().addComment(text,postId);
    }

    private void addPostImage(VBox contentBox, Post item) {
        StackPane imageSection = new StackPane();
        Image image = ControllerForView.getInstance().getPostPicture(item.getPostPictureID());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        imageSection.getStyleClass().add("image-section");

        imageSection.getChildren().add(imageView);
        contentBox.getChildren().add(imageSection);
    }

}
