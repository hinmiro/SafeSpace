package model;

import com.google.gson.*;
import javafx.scene.image.Image;
import services.*;
import java.io.*;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

/**
 * Manages the connection between controllers and API services.
 */
public class SoftwareModel {
    Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(SoftwareModel.class.getName());

    /**
     * Logs in a user with the given username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the logged-in user model, or null if login failed
     * @throws InterruptedException if the thread is interrupted
     */
    public UserModel login(String username, String password) throws InterruptedException {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);
        HttpResponse<String> res = ApiClient.postLogin(gson.toJson(loginData));
        if (res.statusCode() != 200) {
            return null;
        }

        return gson.fromJson(res.body(), UserModel.class);
    }

    /**
     * Registers a new user with the given username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the registered user model, or null if registration failed
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public UserModel postRegister(String username, String password) throws IOException, InterruptedException {
        Map<String, String> registerData = new HashMap<>();
        registerData.put("username", username);
        registerData.put("password", password);

        HttpResponse<String> res = ApiClient.postRegister(gson.toJson(registerData));
        if (res.statusCode() != 201) {
            return null;
        }

        return gson.fromJson(res.body(), UserModel.class);
    }

    /**
     * Uploads a picture to the specified endpoint.
     *
     * @param file the file to upload
     * @param endpoint the endpoint to upload the file to
     * @return the response body from the server
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public String postPicture(File file, String endpoint) throws IOException, InterruptedException {
        String trimmedFileName = file.getName().replace(" ", "");
        File trimmedFile = new File(file.getParent(), trimmedFileName);

        if (!file.renameTo(trimmedFile)) {
            throw new IOException("Failed to rename file");
        }

        HttpResponse<String> res = ApiClient.postPicture(trimmedFile, endpoint);
        return res.body();
    }

    /**
     * Updates the user information.
     *
     * @param username the new username, or null to keep the current username
     * @param password the new password, or null to keep the current password
     * @param bio the new bio, or null to keep the current bio
     * @param profilePictureId the new profile picture ID, or null to keep the current profile picture
     * @return true if the update was successful, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean updateUser(String username, String password, String bio, String profilePictureId) throws IOException, InterruptedException {
        Map<String, String> dataMap = new HashMap<>();
        if (username != null) {
            dataMap.put("username", username);
        }
        if (password != null) {
            dataMap.put("password", password);
        }
        if (bio != null) {
            dataMap.put("bio", bio);
        }
        if (profilePictureId != null) {
            dataMap.put("profilePictureID", profilePictureId);
        }

        String jsonData = gson.toJson(dataMap);

        HttpResponse<String> res = ApiClient.updateUser(jsonData);

        if (res.statusCode() != 200) {
            return false;
        }
        UserModel updatedUser = gson.fromJson(res.body(), UserModel.class);
        logger.info(updatedUser.getJwt());
        SessionManager.getInstance().setLoggedUser(updatedUser);

        return true;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param name the username of the user
     * @return the user model, or null if the user was not found
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public UserModel getUserByName(String name) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getUserByName(name);
        if (res.statusCode() == 200) {
            return gson.fromJson(res.body(), UserModel.class);
        }
        return null;
    }

    /**
     * Creates a new post with the given text content.
     *
     * @param text the text content of the post
     * @return true if the post was created successfully, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean createNewPost(String text) throws IOException, InterruptedException {
        Map<String, String> data = new HashMap<>();
        data.put("post_content", text);
        String jsonData = gson.toJson(data);

        HttpResponse<String> res = ApiClient.createPost(jsonData);
        return res.statusCode() == 201;
    }

    /**
     * Creates a new post with the given text content and image ID.
     *
     * @param text the text content of the post
     * @param imageId the ID of the image to include in the post
     * @return true if the post was created successfully, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean createNewPostWithImage(String text, String imageId) throws IOException, InterruptedException {
        Map<String, String> data = new HashMap<>();
        data.put("post_content", text);
        data.put("post_pictureID", imageId);
        String jsonData = gson.toJson(data);

        HttpResponse<String> res = ApiClient.createPost(jsonData);
        return res.statusCode() == 201;
    }

    /**
     * Starts a thread to fetch and display the main feed.
     *
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public void startMainFeedThread() throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getPosts();
        if (res.statusCode() == 200) {
            Post[] posts = gson.fromJson(res.body(), Post[].class);
            for (Post p : posts) {
                SharedData.getInstance().addEvent(p);
            }

            Feed feed = new Feed();
            Thread feedThread = new Thread(feed);
            feedThread.start();
        }
    }

    /**
     * Likes a post with the given post ID.
     *
     * @param postId the ID of the post to like
     * @return true if the post was liked successfully, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean likePost(String postId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.likePost(postId);
        return res.statusCode() == 200;
    }

    /**
     * Removes a like from a post with the given post ID.
     *
     * @param postId the ID of the post to remove the like from
     * @return true if the like was removed successfully, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean removeLike(String postId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.removeLike(postId);
        return res.statusCode() == 200;
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param postId the ID of the post
     * @return the post model, or null if the post was not found
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public Post getPostById(String postId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getPostById(postId);

        if (res.statusCode() == 200) {
            return gson.fromJson(res.body(), Post.class);
        }
        return null;
    }

    /**
     * Retrieves the profile image of a user by their ID.
     *
     * @param userId the ID of the user
     * @return the profile image
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public Image getProfileImage(int userId) throws IOException, InterruptedException {
        HttpResponse<byte[]> res = ApiClient.getProfileImg(userId);

        if (res.statusCode() == 200) {
            byte[] imageBytes = res.body();
            return new Image(new ByteArrayInputStream(imageBytes));
        } else {
            throw new IOException();
        }
    }

    /**
     * Retrieves the image of a post by its ID.
     *
     * @param id the ID of the post
     * @return the post image
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public Image getPostImage(String id) throws IOException, InterruptedException {
        HttpResponse<byte[]> res = ApiClient.getPostImage(id);

        if (res.statusCode() == 200) {
            byte[] imageBytes = res.body();
            return new Image(new ByteArrayInputStream(imageBytes));
        } else {
            throw new IOException();
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user model, or null if the user was not found
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public UserModel getUserById(int id) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getUserById(id);

        if (res.statusCode() == 200) {
            return gson.fromJson(res.body(), UserModel.class);
        }
        return null;
    }

    /**
     * Retrieves and updates the user arrays for the logged-in user.
     *
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public void getUserArrays() throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();
        HttpResponse<String> res = ApiClient.getUserById(user.getUserId());

        if (res.statusCode() == 200) {
            UserModel userFromId = gson.fromJson(res.body(), UserModel.class);

            if (userFromId.getUserData() == null) {
                userFromId.setUserData(new UserData());
            }

            if (user.getUserData() == null) {
                user.setUserData(new UserData());
            }

            user.getUserData().setFollowing(userFromId.getUserData().getFollowing());
            user.getUserData().setFollowers(userFromId.getUserData().getFollowers());
            user.getUserData().setFriends(userFromId.getUserData().getFriends());

            user.setArrays(userFromId.getLikedPosts(), userFromId.getPosts());
        } else {
            logger.info("Error: Unable to fetch user data. Status code: " + res.statusCode());
        }
    }

    /**
     * Retrieves comments for a post by its ID.
     *
     * @param id the ID of the post
     * @return a list of comments for the post
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public ArrayList<Comment> getCommentsByPostId(String id) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getCommentsByPostId(id);

        if (res.statusCode() == 200) {
            Comment[] commentsArray = gson.fromJson(res.body(), Comment[].class);
            return new ArrayList<>(Arrays.asList(commentsArray));
        } else {
            throw new IOException("Failed to fetch comment: " + res.statusCode());
        }
    }

    /**
     * Posts a comment on a post.
     *
     * @param comment the content of the comment
     * @param postId the ID of the post to comment on
     */
    public void postComment(String comment, String postId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("commentContent", comment);
        String jsonData = gson.toJson(data);

        try {
            ApiClient.postComment(jsonData, postId);
        } catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    /**
     * Sends a message to a user.
     *
     * @param message the message to send
     * @return true if the message was sent successfully, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean sendMessage(Messages message) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.sendMessage(message.getReceiverId(), message.getMessageContent());

        if (res.statusCode() == 200) {
            return "Message sent successfully!".equals(res.body());
        } else {
            throw new IOException("Sending message failed: " + res.statusCode());
        }
    }

    /**
     * Retrieves messages for the logged-in user.
     *
     * @return a list of conversations
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public List<Conversation> getMessages() throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getMessages();

        JsonObject jsonObject = gson.fromJson(res.body(), JsonObject.class);

        JsonArray sentMessagesArray = jsonObject.getAsJsonArray("sentMessages");
        JsonArray receivedMessagesArray = jsonObject.getAsJsonArray("receivedMessages");

        Map<Integer, Conversation> conversationsMap = new HashMap<>();

        for (JsonElement element : sentMessagesArray) {
            Messages message = gson.fromJson(element, Messages.class);
            int receiverId = message.getReceiverId();

            UserModel receiverUser = getUserById(receiverId);

            conversationsMap.putIfAbsent(receiverId, new Conversation(receiverUser));
            conversationsMap.get(receiverId).addMessage(message);
        }

        for (JsonElement element : receivedMessagesArray) {
            Messages message = gson.fromJson(element, Messages.class);
            int senderId = message.getSenderId();

            UserModel senderUser = getUserById(senderId);

            conversationsMap.putIfAbsent(senderId, new Conversation(senderUser));
            conversationsMap.get(senderId).addMessage(message);
        }

        return new ArrayList<>(conversationsMap.values());
    }

    /**
     * Retrieves all conversations for the logged-in user.
     *
     * @return a list of conversations
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public List<Conversation> getAllConversations() throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getConversations();

        Conversation[] conversationsArray = gson.fromJson(res.body(), Conversation[].class);
        return Arrays.asList(conversationsArray);
    }

    /**
     * Adds a friend for the logged-in user.
     *
     * @param userId the ID of the logged-in user
     * @param friendId the ID of the friend to add
     * @return true if the friend was added successfully, false otherwise
     */
    public boolean addFriend(int userId, int friendId) {
        try {
            UserModel user = SessionManager.getInstance().getLoggedUser();

            HttpResponse<String> res = ApiClient.addFriend(friendId);

            if (res.statusCode() == 200) {
                UserModel friend = getUserById(friendId);

                if (friend != null) {
                    if (friend.getUserData() == null) {
                        friend.setUserData(new UserData());
                    }
                    if (user.getUserData() == null) {
                        user.setUserData(new UserData());
                    }

                    user.getUserData().addFollowing(friend);
                    friend.getUserData().addFollowers(user);

                    user.getUserData().setFriendsCount(user.getUserData().getFriendsCount() + 1);
                    friend.getUserData().setFriendsCount(friend.getUserData().getFriendsCount() + 1);

                    user.getUserData().setFollowersCount(user.getUserData().getFollowersCount());
                    friend.getUserData().setFollowingCount(friend.getUserData().getFollowingCount());
                    return true;
                }
            }
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Removes a friend for the logged-in user.
     *
     * @param id the ID of the friend to remove
     * @return true if the friend was removed successfully, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean removeFriend(int id) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.removeFriend(id);
        return res.statusCode() == 200;
    }

    /**
     * Checks if a user is a friend of the logged-in user.
     *
     * @param userId the ID of the logged-in user
     * @param friendId the ID of the friend to check
     * @return true if the user is a friend, false otherwise
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public boolean isFriend(int userId, int friendId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getAllFriends();
        if (res.statusCode() == 200) {
            String responseBody = res.body();
            Integer[] friendsArray = gson.fromJson(responseBody, Integer[].class);
            List<Integer> friendsList = Arrays.asList(friendsArray);
            return friendsList.contains(friendId);
        }
        return false;
    }
}