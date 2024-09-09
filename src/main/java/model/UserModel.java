package model;

public class UserModel {
    private String username;
    private String password;
    private String jwt;
    private String bio, profilePictureID;
    private String userID;


    public UserModel(String username, String password, String userId) {
        this.username = username;
        this.password = password;
        this.bio = "";
        this.profilePictureID = "";
        this.userID = userId;
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

}
