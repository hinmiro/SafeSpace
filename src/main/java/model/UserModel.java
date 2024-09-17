package model;

import java.util.ArrayList;

public class UserModel {
    private String username;
    private String jwt;
    private String bio;
    private String profilePictureID;
    private String userID;
    private String dateOfCreation;
    private ArrayList<UserModel> friends;
    private ArrayList<Post> likedPosts;
    private ArrayList<Post> posts;



    public UserModel(String username, String userId, String dateOfCreation) {
        this.username = username;
        this.bio = "";
        this.profilePictureID = "";
        this.userID = userId;
        this.dateOfCreation = dateOfCreation;
        friends = new ArrayList<>();
        posts = new ArrayList<>();
        likedPosts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getJwt() { return jwt; }

    public void setJwt(String token) {jwt = token;}

    public void setUsername (String newUsername) { username = newUsername; }

    public void setBio(String bio) { this.bio = bio; }

    public void setProfilePictureUrl(String url) { this.profilePictureID = url; }

    public String getBio() { return bio; }

    public String getProfilePictureUrl() { return profilePictureID; }

    public String getUserId() { return userID; }

    public String getDateOfCreation() { return dateOfCreation; }

    public ArrayList<UserModel> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<UserModel> friends) {
        this.friends = friends;
    }

    public ArrayList<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(ArrayList<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

}
