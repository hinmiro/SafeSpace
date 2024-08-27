package model;

import com.google.gson.Gson;
import controller.Controller;
import controller.ControllerForModel;


public class SoftwareModel {
    Gson gson = new Gson();

    public String login(String username, String password) {
        Login login = new Login(username, password);
        return gson.toJson(login);
    }
}
