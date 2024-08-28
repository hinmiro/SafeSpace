package model;

import com.google.gson.Gson;
import controller.Controller;
import controller.ControllerForModel;
import services.ApiClient;

import java.io.IOException;


public class SoftwareModel {
    Gson gson = new Gson();
    String mockUrl = "https://8af3ee45-282a-463e-8a0f-69b634acd1e8.mock.pstmn.io/createUser";

    public String login(String username, String password) throws IOException, InterruptedException {
        Login login = new Login(username, password);

        return ApiClient.postLogin(mockUrl, gson.toJson(login));
    }
}
