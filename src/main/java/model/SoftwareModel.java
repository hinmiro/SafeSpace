package model;

import com.google.gson.Gson;
import services.ApiClient;

import java.io.IOException;
import java.net.http.HttpResponse;


public class SoftwareModel {
    Gson gson = new Gson();

    public UserModel login(String username, String password) throws IOException, InterruptedException {
        Login login = new Login(username, password);
        String user = ApiClient.postLogin(gson.toJson(login));
        UserModel userObject = gson.fromJson(user, UserModel.class);

        return userObject;
    }

    public UserModel postRegister(String username, String password) throws IOException, InterruptedException {
        Register register = new Register(username, password);

        HttpResponse<String> res = ApiClient.postRegister(gson.toJson(register));
        if (res.statusCode() != 200) {
            return null;
        }
        LoginResponse loginResponse = gson.fromJson(res.body(), LoginResponse.class);
        UserModel user = loginResponse.getUser();
        user.setJwt(loginResponse.getJwt());

        System.out.println("user: " + user.getUsername() + user.getJwt());
        return user;

    }
}

