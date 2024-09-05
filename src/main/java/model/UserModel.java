package model;

public class UserModel {
    private String username;
    private String password;
    private String jwt;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getJwt() { return jwt; }
    public void setJwt(String token) {jwt = token;}

}
