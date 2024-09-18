package controller;

import model.SessionManager;
import model.UserModel;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerForView extends Controller {

    public static BlockingQueue<String> feedQueue = new LinkedBlockingQueue<>();
    private static ControllerForView INSTANCE;

    private ControllerForView() {}

    public static ControllerForView getInstance() {
        INSTANCE = INSTANCE == null ? new ControllerForView() : INSTANCE;
        return INSTANCE;
    }


    public  UserModel login(String username, String password) {
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

    // Returns true if update success, then should render popup in front view example profile updated successfully
    public boolean updateProfile(String username, String password, String bio, String profilePictureId) throws IOException, InterruptedException {
        return app.updateUser(username, password, bio, profilePictureId);
    }

    public boolean sendPost(String text) throws IOException, InterruptedException {
        return app.createNewPost(text);
    }

}
