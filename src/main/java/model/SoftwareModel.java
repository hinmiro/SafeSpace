package model;

import com.google.gson.*;
import javafx.scene.image.Image;
import services.*;
import java.io.*;
import java.net.http.HttpResponse;
import java.util.*;

// Connection between controllers and Api services

public class SoftwareModel {
    Gson gson = new Gson();

    public UserModel login(String username, String password) throws IOException, InterruptedException {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);
        HttpResponse<String> res = ApiClient.postLogin(gson.toJson(loginData));
        if (res.statusCode() != 200) {
            return null;
        }

        return gson.fromJson(res.body(), UserModel.class);
    }

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

    public String postPicture(File file, String endpoint) throws IOException, InterruptedException {
        String trimmedFileName = file.getName().replace(" ", "");
        File trimmedFile = new File(file.getParent(), trimmedFileName);

        if (!file.renameTo(trimmedFile)) {
            throw new IOException("Failed to rename file");
        }

        HttpResponse<String> res = ApiClient.postPicture(trimmedFile, endpoint);
        return res.body();
    }

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
        System.out.println(updatedUser.getJwt());
        SessionManager.getInstance().setLoggedUser(updatedUser);

        return true;
    }

    public UserModel getUserByName(String name) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getUserByName(name);
        if (res.statusCode() == 200) {
            return gson.fromJson(res.body(), UserModel.class);
        }
        return null;
    }

    public boolean createNewPost(String text) throws IOException, InterruptedException {
        Map<String, String> data = new HashMap<>();
        data.put("post_content", text);
        String jsonData = gson.toJson(data);

        HttpResponse<String> res = ApiClient.createPost(jsonData);
        return res.statusCode() == 201;
    }

    public boolean createNewPostWithImage(String text, String imageId) throws IOException, InterruptedException {
        Map<String, String> data = new HashMap<>();
        data.put("post_content", text);
        data.put("post_pictureID", imageId);
        String jsonData = gson.toJson(data);

        HttpResponse<String> res = ApiClient.createPost(jsonData);
        return res.statusCode() == 201;
    }

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

    public boolean likePost(String postId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.likePost(postId);
        return res.statusCode() == 200;
    }

    public boolean removeLike(String postId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.removeLike(postId);
        return res.statusCode() == 200;
    }

    public Post getPostById(String postId) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getPostById(postId);

        if (res.statusCode() == 200) {
            return gson.fromJson(res.body(), Post.class);
        }
        return null;
    }

    public Image getProfileImage(int userId) throws IOException, InterruptedException {
        HttpResponse<byte[]> res = ApiClient.getProfileImg(userId);

        if (res.statusCode() == 200) {
            byte[] imageBytes = res.body();
            return new Image(new ByteArrayInputStream(imageBytes));
        } else {
            throw new IOException();
        }
    }


    public Image getPostImage(String id) throws IOException, InterruptedException {
        HttpResponse<byte[]> res = ApiClient.getPostImage(id);

        if (res.statusCode() == 200) {
            byte[] imageBytes = res.body();
            return new Image(new ByteArrayInputStream(imageBytes));
        } else {
            throw new IOException();
        }
    }

    public UserModel getUserById(int id) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getUserById(id);

        if (res.statusCode() == 200) {
            UserModel user = gson.fromJson(res.body(), UserModel.class);
            return user;
        }
        return null;
    }

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
            System.out.println("Error: Unable to fetch user data. Status code: " + res.statusCode());
        }
    }

    public ArrayList<Comment> getCommentsByPostId(String id) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getCommentsByPostId(id);

        if (res.statusCode() == 200) {
            Comment[] commentsArray = gson.fromJson(res.body(), Comment[].class);
            return new ArrayList<>(Arrays.asList(commentsArray));
        } else {
            throw new IOException("Failed to fetch comment: " + res.statusCode());
        }
    }

    public void postComment(String comment, String postId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("commentContent", comment);
        String jsonData = gson.toJson(data);

        try {
            HttpResponse<String> res = ApiClient.postComment(jsonData, postId);
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean sendMessage(Message message) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.sendMessage(message.getReceiverId(), message.getMessageContent());

        if (res.statusCode() == 200) {
            return "Message sent successfully!".equals(res.body());
        } else {
            throw new IOException("Sending message failed: " + res.statusCode());
        }
    }

    public List<Conversation> getMessages() throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getMessages();

        JsonObject jsonObject = gson.fromJson(res.body(), JsonObject.class);

        JsonArray sentMessagesArray = jsonObject.getAsJsonArray("sentMessages");
        JsonArray receivedMessagesArray = jsonObject.getAsJsonArray("receivedMessages");

        Map<Integer, Conversation> conversationsMap = new HashMap<>();

        for (JsonElement element : sentMessagesArray) {
            Message message = gson.fromJson(element, Message.class);
            int receiverId = message.getReceiverId();

            UserModel receiverUser = getUserById(receiverId);

            conversationsMap.putIfAbsent(receiverId, new Conversation(receiverUser));
            conversationsMap.get(receiverId).addMessage(message);
        }

        for (JsonElement element : receivedMessagesArray) {
            Message message = gson.fromJson(element, Message.class);
            int senderId = message.getSenderId();

            UserModel senderUser = getUserById(senderId);

            conversationsMap.putIfAbsent(senderId, new Conversation(senderUser));
            conversationsMap.get(senderId).addMessage(message);
        }

        return new ArrayList<>(conversationsMap.values());
    }

    public List<Conversation> getAllConversations() throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.getConversations();

        Conversation[] conversationsArray = gson.fromJson(res.body(), Conversation[].class);
        return Arrays.asList(conversationsArray);
    }

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
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFriend(int id) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.removeFriend(id);
        return res.statusCode() == 200;
    }

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

