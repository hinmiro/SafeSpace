package model;

import com.google.gson.Gson;
import services.ApiClient;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class SoftwareModel {
    Gson gson = new Gson();

    public UserModel login(String username, String password) throws IOException, InterruptedException {
        Login login = new Login(username, password);
        HttpResponse<String> res = ApiClient.postLogin(gson.toJson(login));
        if (res.statusCode() != 200) {
            return null;
        }
        LoginResponse loginResponse = gson.fromJson(res.body(), LoginResponse.class);

        UserModel user = loginResponse.getUser();
        user.setJwt(loginResponse.getJwt());

        return user;
    }

    public UserModel postRegister(String username, String password) throws IOException, InterruptedException {
        Register register = new Register(username, password);

        HttpResponse<String> res = ApiClient.postRegister(gson.toJson(register));
        if (res.statusCode() != 201) {
            return null;
        }
        LoginResponse loginResponse = gson.fromJson(res.body(), LoginResponse.class);
        UserModel user = loginResponse.getUser();
        user.setJwt(loginResponse.getJwt());

        return user;
    }

    public boolean postPicture(File file) throws IOException, InterruptedException {
        HttpResponse<String> res = ApiClient.postPicture(file);
        return res.statusCode() == 200;
    }
}

