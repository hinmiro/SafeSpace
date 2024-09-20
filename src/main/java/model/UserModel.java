package model;

import java.util.ArrayList;

public class UserModel {
    private String username;
    private String jwt;
    private String bio;
    private String profilePictureID;
    private String userID;
    private String dateOfCreation;
    private ArrayList<Integer> friends;
    private ArrayList<Integer> likedPosts;
    private ArrayList<Integer> posts;

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

    public ArrayList<Integer> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Integer> friends) {
        this.friends = friends;
    }

    public ArrayList<Integer> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(ArrayList<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public ArrayList<Integer> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Integer> posts) {
        this.posts = posts;
    }

}
