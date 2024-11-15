package model;

import controller.MainController;

public class SessionManager {

    private static SessionManager INSTANCE;
    private static UserModel loggedUser;
    private static Language selectedLanguage;
    private MainController mainController;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        INSTANCE = INSTANCE == null ? new SessionManager() : INSTANCE;
        return INSTANCE;
    }

    public void setLoggedUser(UserModel user) {
        loggedUser = user;
    }

    public UserModel getLoggedUser() {
        return loggedUser;
    }

    public void closeSession() {
        loggedUser = null;
        INSTANCE = null;
    }

    public void setLanguage(Language language) {
        selectedLanguage = language;
    }

    public Language getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }
}
