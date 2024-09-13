package model;

import com.google.gson.Gson;
import controller.ControllerForView;
import services.ApiClient;
import services.Feed;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


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

        Feed feed = new Feed(ControllerForView.feedQueue);
        Thread feedThread = new Thread(feed);
        feedThread.start();

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

    public UserModel updateUser(String username, String password, String bio, String profilePictureId) throws IOException, InterruptedException {
        Map<String, String> dataMap = new HashMap<>();
        if (username != null) { dataMap.put("username", username); }
        if (password != null) { dataMap.put("password", password); }
        if (bio != null) { dataMap.put("bio", bio); }
        if (profilePictureId != null) { dataMap.put("profilePictureID", profilePictureId); }

        String jsonData = gson.toJson(dataMap);

        HttpResponse<String> res = ApiClient.updateUser(jsonData);

        if (res.statusCode() != 200) {
            return null;
        }
        UserModel updatedUser = gson.fromJson(res.body(), UserModel.class);
        updatedUser.setJwt(SessionManager.getInstance().getLoggedUser().getJwt());    // Setting jwt token to be token of updated user
        SessionManager.getInstance().setLoggedUser(updatedUser);

        return updatedUser;
    }

}

