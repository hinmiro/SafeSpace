package controller;

import com.google.gson.JsonObject;
import model.UserModel;

import java.io.IOException;

public class ControllerForView extends Controller {

    public UserModel login(String username, String password) {
        try {
            String user = app.login(username, password);
            System.out.println(user);
        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public String register(String username, String password, String contactInfo) {
        try {
            return app.postRegister(username, password, contactInfo);

        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
