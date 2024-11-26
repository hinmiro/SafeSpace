package model;

import java.util.*;

/**
 * Represents a user in the system.
 */
public class UserModel {
    private String username;
    private String jwt;
    private String bio;
    private String profilePictureID;
    private int id;
    private UserData userData;
    private String dateOfCreation;
    private List<Integer> likedPosts;
    private List<Integer> posts;

    /**
     * Constructs a new UserModel with the specified username, id, and date of creation.
     *
     * @param username the username of the user
     * @param id the ID of the user
     * @param dateOfCreation the date the user was created
     */
    public UserModel(String username, int id, String dateOfCreation) {
        this.username = username;
        this.bio = "";
        this.profilePictureID = "";
        this.id = id;
        this.dateOfCreation = dateOfCreation;
        this.userData = new UserData();
        posts = new ArrayList<>();
        likedPosts = new ArrayList<>();
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the JWT of the user.
     *
     * @return the JWT
     */
    public String getJwt() { return jwt; }

    /**
     * Sets the JWT of the user.
     *
     * @param token the JWT to set
     */
    public void setJwt(String token) { jwt = token; }

    /**
     * Sets the username of the user.
     *
     * @param newUsername the new username to set
     */
    public void setUsername(String newUsername) { username = newUsername; }

    /**
     * Sets the bio of the user.
     *
     * @param bio the new bio to set
     */
    public void setBio(String bio) { this.bio = bio; }

    /**
     * Sets the profile picture URL of the user.
     *
     * @param url the new profile picture URL to set
     */
    public void setProfilePictureUrl(String url) { this.profilePictureID = url; }

    /**
     * Returns the bio of the user.
     *
     * @return the bio
     */
    public String getBio() { return bio; }

    /**
     * Returns the profile picture URL of the user.
     *
     * @return the profile picture URL
     */
    public String getProfilePictureUrl() { return profilePictureID; }

    /**
     * Returns the ID of the user.
     *
     * @return the user ID
     */
    public int getUserId() { return id; }

    /**
     * Returns the date of creation of the user.
     *
     * @return the date of creation
     */
    public String getDateOfCreation() { return dateOfCreation; }

    /**
     * Returns the list of liked posts.
     *
     * @return the list of liked posts
     */
    public List<Integer> getLikedPosts() {
        return likedPosts;
    }

    /**
     * Sets the list of liked posts.
     *
     * @param likedPosts the list of liked posts to set
     */
    public void setLikedPosts(List<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    /**
     * Returns the list of posts.
     *
     * @return the list of posts
     */
    public List<Integer> getPosts() {
        return posts;
    }

    /**
     * Sets the list of posts.
     *
     * @param posts the list of posts to set
     */
    public void setPosts(List<Integer> posts) {
        this.posts = posts;
    }

    /**
     * Adds a post ID to the list of liked posts.
     *
     * @param postId the post ID to add
     */
    public void addLikedPost(int postId) {
        likedPosts.add(postId);
    }

    /**
     * Removes a post ID from the list of liked posts.
     *
     * @param postId the post ID to remove
     */
    public void removeLikedPost(int postId) {
        likedPosts.remove((Integer) postId);
    }

    /**
     * Returns the user data.
     *
     * @return the user data
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * Sets the user data.
     *
     * @param userData the user data to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * Sets the lists of liked posts and posts.
     *
     * @param likedPosts the list of liked posts to set
     * @param posts the list of posts to set
     */
    public void setArrays(List<Integer> likedPosts, List<Integer> posts) {
        this.likedPosts = likedPosts;
        this.posts = posts;
    }
}