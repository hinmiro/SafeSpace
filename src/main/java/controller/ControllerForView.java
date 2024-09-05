package controller;

import model.UserModel;
import java.io.IOException;

public class ControllerForView extends Controller {

    public UserModel login(String username, String password) {
        try {
            UserModel user = app.login(username, password);
            return user;
        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public UserModel register(String username, String password) {
        try {
            return app.postRegister(username, password);

        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
