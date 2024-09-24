package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.*;

import java.io.IOException;
import java.util.List;

public class UserProfileController {
    private ControllerForView controllerForView = ControllerForView.getInstance();
    @FXML private Label usernameLabel;
    @FXML private Label bioLabel;
    @FXML private Label registeredLabel;
    @FXML private ImageView profileImageView;
    @FXML private Button followButton;
    @FXML private Button messageButton;
    @FXML private VBox userPostsVBox;
    @FXML private ScrollPane scrollPane;
    @FXML private Label noPostsLabel;

    public void initialize(int userId) {
        fetchUserData(userId);

        profileImageView.setImage(createPlaceholderImage(150, 150));
        makeCircle(profileImageView);
        displayUserPosts(scrollPane, userPostsVBox, noPostsLabel, userId);
    }

    private void fetchUserData(int userId) {
        UserModel user = controllerForView.getUserById(userId);
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            bioLabel.setText(user.getBio());
            registeredLabel.setText(user.getDateOfCreation());
        }
    }

    public void displayUserPosts(ScrollPane scrollPane, VBox userPostsVBox, Label noPostsLabel, int userId) {
        List<Post> posts;
        try {
            posts = controllerForView.getUserPostsUserProfile(userId);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        if (posts.isEmpty()) {
            noPostsLabel.setVisible(true);
            scrollPane.setVisible(false);
        } else {
            noPostsLabel.setVisible(false);
            scrollPane.setVisible(true);
            userPostsVBox.getChildren().clear();

            for (Post post : posts) {
                PostListCell postCell = new PostListCell();
                postCell.updateItem(post, false);
                userPostsVBox.getChildren().add(postCell);
            }

            scrollPane.setContent(userPostsVBox);
            scrollPane.setFitToWidth(true);
        }
    }


    public void handleFollowButton(ActionEvent actionEvent) {

    }

    public void handleMessageButton(ActionEvent actionEvent) {
    }

    private void makeCircle(ImageView imageView) {
        Circle clip = new Circle(
                imageView.getFitWidth() / 2,
                imageView.getFitHeight() / 2,
                Math.min(imageView.getFitWidth(),
                        imageView.getFitHeight()) / 2);
        imageView.setClip(clip);
    }

    private WritableImage createPlaceholderImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillOval(0, 0, width, height);

        gc.getCanvas().snapshot(null, image);
        return image;
    }
}
