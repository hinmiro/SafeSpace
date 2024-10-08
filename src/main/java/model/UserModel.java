package model;

import java.util.ArrayList;

public class UserModel {
    private String username;
    private String jwt;
    private String bio;
    private String profilePictureID;
    private int id;
    private UserData userData;
    private String dateOfCreation;
    private ArrayList<Integer> likedPosts;
    private ArrayList<Integer> posts;
    private ArrayList<Conversation> conversations;

    public UserModel(String username, int id, String dateOfCreation) {
        this.username = username;
        this.bio = "";
        this.profilePictureID = "";
        this.id = id;
        this.dateOfCreation = dateOfCreation;
        this.userData = new UserData();
        posts = new ArrayList<>();
        likedPosts = new ArrayList<>();
        conversations = new ArrayList<>();
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

    public int getUserId() { return id; }

    public String getDateOfCreation() { return dateOfCreation; }

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

    public void addLikedPost(int postId) {
        likedPosts.add(postId);
    }

    public void removeLikedPost(int postId) {
        likedPosts.remove((Integer) postId);
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void addConversation(Conversation conversation) {
        this.conversations.add(conversation);
    }

    public void removeConversation(Conversation conversation) {
        this.conversations.remove(conversation);
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public void setArrays(ArrayList<Integer> likedPosts, ArrayList<Integer> posts) {
        this.likedPosts = likedPosts;
        this.posts = posts;
    }

}
