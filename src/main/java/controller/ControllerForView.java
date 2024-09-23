package controller;

import javafx.scene.image.Image;
import model.Comment;
import model.Post;
import model.SessionManager;
import model.UserModel;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerForView extends Controller {

    public static BlockingQueue<Post> feedQueue = new LinkedBlockingQueue<>();
    private static ControllerForView INSTANCE;

    private ControllerForView() {}

    public static ControllerForView getInstance() {
        INSTANCE = INSTANCE == null ? new ControllerForView() : INSTANCE;
        return INSTANCE;
    }


    public  UserModel login(String username, String password) {
        try {
            UserModel user = app.login(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                app.startMainFeedThread();
                return user;
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public UserModel register(String username, String password) {
        try {
            UserModel user = app.postRegister(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                app.startMainFeedThread();
                return user;
            }
            //return app.postRegister(username, password);

        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }


    // Returns true if update success, then should render popup in front view example profile updated successfully
    public boolean updateProfile(String username, String password, String bio, File image) throws IOException, InterruptedException {
        String profilePictureId = null;
        if (image != null) {
            profilePictureId = app.postPicture(image, "/profile");
        }
        return app.updateUser(username, password, bio, profilePictureId);
    }

    public boolean sendPost(String text) throws IOException, InterruptedException {
        return app.createNewPost(text);
    }

    public boolean sendPostWithImage(String text, File image) throws IOException, InterruptedException {
        String imageId = app.postPicture(image, "/post");
        return app.createNewPostWithImage(text, imageId);
    }

    public BlockingQueue<Post> getFeed() {
        return feedQueue;
    }

    public Image getProfilePicture() {
        Image img = null;
        try {
            img = app.getProfileImage();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
        return img;
    }

    public Image getPostPicture(String id) {
        Image img = null;
        try {
            img = app.getPostImage(id);
        }catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
        return img;
    }

    public ArrayList<Comment> getPostCommentsById(String id) {
        try {
            return app.getCommentsByPostId(id);
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
