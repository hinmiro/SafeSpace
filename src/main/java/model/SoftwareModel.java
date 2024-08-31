package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import services.ApiClient;

import java.io.IOException;


public class SoftwareModel {
    Gson gson = new Gson();
    String mockUrl = "";

    public String login(String username, String password) throws IOException, InterruptedException {
        Login login = new Login(username, password);

        return ApiClient.postLogin(mockUrl, gson.toJson(login));
    }

    public String postRegister(String username, String password, String contactInfo) throws IOException, InterruptedException {
        Register register = new Register(username, password, contactInfo);

        String res = ApiClient.postRegister(gson.toJson(register));;
        return res;

    }
}

