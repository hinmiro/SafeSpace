package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import services.ApiClient;

import java.io.IOException;


public class SoftwareModel {
    Gson gson = new Gson();
    String mockUrl = "";

    public UserModel login(String username, String password) throws IOException, InterruptedException {
        Login login = new Login(username, password);
        String user = ApiClient.postLogin(gson.toJson(login));
        UserModel userObject = gson.fromJson(user, UserModel.class);

        return userObject;
    }

    public String postRegister(String username, String password, String contactInfo) throws IOException, InterruptedException {
        Register register = new Register(username, password, contactInfo);

        String res = ApiClient.postRegister(gson.toJson(register));;
        return res;

    }
}

