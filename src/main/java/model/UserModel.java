package model;

public class UserModel {
    private String username;
    private String password;
    private String jwt;
    private String bio, profilePictureId;
    private String userId;


    public UserModel(String username, String password, String userId) {
        this.username = username;
        this.password = password;
        this.bio = "";
        this.profilePictureId = "";
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getJwt() { return jwt; }

    public void setJwt(String token) {jwt = token;}

    public void setUsername (String newUsername) { username = newUsername; }

    public void setBio(String bio) { this.bio = bio; }

    public void setProfilePictureId(String id) { this.profilePictureId = id; }

    public String getBio() { return bio; }

    public String getProfilePictureId() { return profilePictureId; }

    public String getUserId() { return userId; }

}
