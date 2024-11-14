package controller;

import javafx.scene.image.Image;
import model.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    void setApp(SoftwareModel app) {
        this.app = app;
    }

    public UserModel login(String username, String password) {
        try {
            UserModel user = app.login(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                app.getUserArrays();
                //app.getMe();
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
                app.getUserArrays();
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

    public Image getProfilePicture(int userId) {
        Image img = null;
        try {
            img = app.getProfileImage(userId);
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

    public List<Post> getUserPostsOwnProfile() throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();
        UserModel updatedUser = app.getUserById(user.getUserId());
        if (updatedUser != null) {
            user.setPosts(updatedUser.getPosts());
            List<Post> posts = new ArrayList<>();
            for (Integer postId : user.getPosts()) {
                Post post = app.getPostById(String.valueOf(postId));
                if (post != null) {
                    posts.add(post);
                }
            }
            return posts;
        }
        return new ArrayList<>();
    }

    public UserModel getUserById(int userId) {
        try {
            return app.getUserById(userId);
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching user: " + e.getMessage());
            return null;
        }
    }

    public UserModel getUserByName(String username) {
        try {
            return app.getUserByName(username);
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public List<Post> getUserPostsOwnProfile(int userId) throws IOException, InterruptedException {
        UserModel user = app.getUserById(userId);
        if (user != null) {
            List<Post> posts = new ArrayList<>();
            for (Integer postId : user.getPosts()) {
                Post post = app.getPostById(String.valueOf(postId));
                if (post != null) {
                    posts.add(post);
                }
            }
            return posts;
        }
        return new ArrayList<>();
    }

    public void removeLike(int postId) {
        try {
            app.removeLike(String.valueOf(postId));
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Comment> getPostCommentsById(String id) {
        try {
            return app.getCommentsByPostId(id);
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addComment(String comment, int postId) {
        app.postComment(comment, String.valueOf(postId));
    }

    public boolean sendMessage(Message message) throws IOException, InterruptedException {
        return app.sendMessage(message);
    }

    public List<Conversation> getMessages() {
        try {
            return app.getMessages();
        } catch (IOException | InterruptedException e) {
            return new ArrayList<>();
        }
    }

    public Conversation getCurrentConversation(int userId) {
        try {
            List<Conversation> conversations = app.getAllConversations();

            for (Conversation conversation : conversations) {
                if (conversation.getWithUser().getUserId() == userId) {
                    return conversation;
                }
            }
            return null;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public boolean addFriend(int userId, int friendId) {
        return app.addFriend(userId, friendId);
    }

    public boolean removeFriend(int userId) {
        try {
            return app.removeFriend(userId);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public boolean isFriend(int userId, int friendId) {
        try {
            return app.isFriend(userId, friendId);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
