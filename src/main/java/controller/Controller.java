package controller;

import model.SoftwareModel;
import model.User;

public class Controller implements ControllerForView, ControllerForModel {
    User user = new User();
    SoftwareModel app = new SoftwareModel();

    public String login(String username, String password) {
        return app.login(username, password);
    }
}
