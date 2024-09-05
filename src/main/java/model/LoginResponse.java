package model;

public class LoginResponse {
    private String jwt;
    private UserModel user;


    public String getJwt() {
        return jwt;
    }

    public UserModel getUser() {
        return user;
    }

}
