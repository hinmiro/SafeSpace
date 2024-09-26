package model;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import services.ApiClient;
import services.Feed;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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
        HttpResponse<String> res = ApiClient.postPicture(file, endpoint);
        return res.body();
    }

    public boolean updateUser(String username, String password, String bio, String profilePictureId) throws IOException, InterruptedException {
        Map<String, String> dataMap = new HashMap<>();
        if (username != null) { dataMap.put("username", username); }
        if (password != null) { dataMap.put("password", password); }
        if (bio != null) { dataMap.put("bio", bio); }
        if (profilePictureId != null) { dataMap.put("profilePictureID", profilePictureId); }

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

//    public String getUsernameByPostCreatorID(int userID) throws IOException, InterruptedException {
//        HttpResponse<String> response = ApiClient.getUsernameByUserID(userID);
//
//        if (response.statusCode() == 200) {
//            Gson gson = new Gson();
//            try {
//                UserModel userResponse = gson.fromJson(response.body(), UserModel.class);
//                return userResponse.getUsername();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        } else {
//            return null;
//        }
//    }

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

    public Image getProfileImage() throws IOException, InterruptedException {
        HttpResponse<byte[]> res = ApiClient.getProfileImg();
        if (res.statusCode() == 200) {
            byte[] imageBytes = res.body();
            return new Image(new ByteArrayInputStream(imageBytes));
        }else {
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
            user.setArrays(userFromId.getLikedPosts(), userFromId.getPosts(), userFromId.getFriends());
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
}

