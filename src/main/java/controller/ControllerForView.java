package controller;

import javafx.scene.image.Image;
import model.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controller class for managing the view in the application.
 * This class acts as a singleton controller between the software model and JavaFX controllers.
 */
public class ControllerForView extends Controller {

    // Singleton instance of the controller
    private static ControllerForView instance;
    // Logger instance for logging information and errors
    private static final Logger logger = Logger.getLogger(ControllerForView.class.getName());

    // Private constructor to prevent instantiation
    private ControllerForView() {
    }

    /**
     * Returns the singleton instance of the controller.
     *
     * @return the singleton instance of ControllerForView
     */
    public static ControllerForView getInstance() {
        instance = instance == null ? new ControllerForView() : instance;
        return instance;
    }

    /**
     * Logs in a user with the given username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the logged-in user model, or null if login fails
     */
    public UserModel login(String username, String password) {
        try {
            UserModel user = app.login(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                app.getUserArrays();
                app.startMainFeedThread();
                return user;
            }
        } catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.info("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Registers a new user with the given username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the registered user model, or null if registration fails
     */
    public UserModel register(String username, String password) {
        try {
            UserModel user = app.postRegister(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                app.getUserArrays();
                app.startMainFeedThread();
                return user;
            }
        } catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.info("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates the profile of the logged-in user.
     *
     * @param username the new username
     * @param password the new password
     * @param bio      the new bio
     * @param image    the new profile image file
     * @return true if the update is successful, false otherwise
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean updateProfile(String username, String password, String bio, File image) throws IOException, InterruptedException {
        String profilePictureId = null;
        if (image != null) {
            profilePictureId = app.postPicture(image, "/profile");
        }
        return app.updateUser(username, password, bio, profilePictureId);
    }

    /**
     * Sends a new post with the given text.
     *
     * @param text the text of the post
     * @return true if the post is sent successfully, false otherwise
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean sendPost(String text) throws IOException, InterruptedException {
        return app.createNewPost(text);
    }

    /**
     * Sends a new post with the given text and image.
     *
     * @param text  the text of the post
     * @param image the image file of the post
     * @return true if the post is sent successfully, false otherwise
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean sendPostWithImage(String text, File image) throws IOException, InterruptedException {
        String imageId = app.postPicture(image, "/post");
        return app.createNewPostWithImage(text, imageId);
    }

    /**
     * Retrieves the profile picture of the user with the given ID.
     *
     * @param userId the ID of the user
     * @return the profile picture as an Image object, or null if an error occurs
     */
    public Image getProfilePicture(int userId) {
        Image img = null;
        try {
            img = app.getProfileImage(userId);
        } catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.warning(e.getMessage());
        }
        return img;
    }

    /**
     * Retrieves the post picture with the given ID.
     *
     * @param id the ID of the post picture
     * @return the post picture as an Image object, or null if an error occurs
     */
    public Image getPostPicture(String id) {
        Image img = null;
        try {
            img = app.getPostImage(id);
        } catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.warning(e.getMessage());
        }
        return img;
    }

    /**
     * Retrieves the posts of the logged-in user for their own profile.
     *
     * @return a list of posts, or an empty list if an error occurs
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
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

    /**
     * Retrieves the posts of the user with the given ID for their profile.
     *
     * @param userId the ID of the user
     * @return a list of posts, or an empty list if an error occurs
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
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

    /**
     * Retrieves the user with the given ID.
     *
     * @param userId the ID of the user
     * @return the user model, or null if an error occurs
     */
    public UserModel getUserById(int userId) {
        try {
            return app.getUserById(userId);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.info("Error fetching user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the user with the given username.
     *
     * @param username the username of the user
     * @return the user model, or null if an error occurs
     */
    public UserModel getUserByName(String username) {
        try {
            return app.getUserByName(username);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the comments for the post with the given ID.
     *
     * @param id the ID of the post
     * @return a list of comments, or an empty list if an error occurs
     */
    public ArrayList<Comment> getPostCommentsById(String id) {
        try {
            return app.getCommentsByPostId(id);
        } catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Adds a comment to the post with the given ID.
     *
     * @param comment the text of the comment
     * @param postId  the ID of the post
     */
    public void addComment(String comment, int postId) {
        app.postComment(comment, String.valueOf(postId));
    }

    /**
     * Sends a message.
     *
     * @param message the message to be sent
     * @return true if the message is sent successfully, false otherwise
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean sendMessage(Messages message) throws IOException, InterruptedException {
        return app.sendMessage(message);
    }

    /**
     * Retrieves the messages for the logged-in user.
     *
     * @return a list of conversations, or an empty list if an error occurs
     */
    public List<Conversation> getMessages() {
        try {
            return app.getMessages();
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves the current conversation with the user with the given ID.
     *
     * @param userId the ID of the user
     * @return the current conversation, or null if an error occurs
     */
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
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a friend with the given user ID and friend ID.
     *
     * @param userId   the ID of the user
     * @param friendId the ID of the friend
     * @return true if the friend is added successfully, false otherwise
     */
    public boolean addFriend(int userId, int friendId) {
        return app.addFriend(userId, friendId);
    }

    /**
     * Removes a friend with the given user ID.
     *
     * @param userId the ID of the user
     * @return true if the friend is removed successfully, false otherwise
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean removeFriend(int userId) {
        try {
            return app.removeFriend(userId);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the user with the given user ID is friends with the user with the given friend ID.
     *
     * @param userId   the ID of the user
     * @param friendId the ID of the friend
     * @return true if they are friends, false otherwise
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean isFriend(int userId, int friendId) {
        try {
            return app.isFriend(userId, friendId);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the user arrays in the application.
     *
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public void setUserArrays() throws IOException, InterruptedException {
        app.getUserArrays();
    }
}