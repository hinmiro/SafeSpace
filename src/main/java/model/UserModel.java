package model;

public class UserModel {
    private String username;
    private String password;
    private String contactInfo;

    public UserModel(String username, String password, String contactInfo) {
        this.username = username;
        this.password = password;
        this.contactInfo = contactInfo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
