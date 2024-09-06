package controller;

import model.SessionManager;
import model.UserModel;

import java.io.File;
import java.io.IOException;

public class ControllerForView extends Controller {

    public UserModel login(String username, String password) {
        try {
            UserModel user = app.login(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                return user;
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public UserModel register(String username, String password) {
        try {
            UserModel user = app.postRegister(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedUser(user);
                return user;
            }
            return app.postRegister(username, password);

        } catch (InterruptedException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean uploadProfilePicture(File file) {
        try {
            return app.postPicture(file);
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
