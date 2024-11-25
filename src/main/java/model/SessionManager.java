package model;

import controller.*;

public class SessionManager {
    private static SessionManager instance;
    private static UserModel loggedUser;
    private static Language selectedLanguage = Language.EN;
    private MainController mainController;
    private ProfileController profileController;

    // Data class to save logged userdata for session and selected localization

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        instance = instance == null ? new SessionManager() : instance;
        return instance;
    }

    public static void setLoggedUser(UserModel user) {
        loggedUser = user;
    }

    public UserModel getLoggedUser() {
        return loggedUser;
    }

    public static void closeSession() {
        loggedUser = null;
        instance = null;
    }

    public static void setLanguage(Language language) {
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

    public ProfileController getProfileController() {
        return profileController;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }
}
