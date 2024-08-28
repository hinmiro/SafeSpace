package controller;

import model.SoftwareModel;
import model.User;

import java.io.IOException;

public class Controller implements ControllerForView, ControllerForModel {
    User user = new User();
    SoftwareModel app = new SoftwareModel();

    public String login(String username, String password) throws IOException, InterruptedException {
        return app.login(username, password);
    }
}
