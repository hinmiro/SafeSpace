package model;

import controller.*;

/**
 * Manages the session data including the logged user and selected language.
 */
public class SessionManager {
    private static SessionManager instance;
    private static UserModel loggedUser;
    private static Language selectedLanguage = Language.EN;
    private MainController mainController;
    private ProfileController profileController;

    /**
     * Private constructor to prevent instantiation.
     */
    private SessionManager() {
    }

    /**
     * Returns the singleton instance of the SessionManager.
     *
     * @return the singleton instance
     */
    public static SessionManager getInstance() {
        instance = instance == null ? new SessionManager() : instance;
        return instance;
    }

    /**
     * Sets the logged user.
     *
     * @param user the user to set as logged in
     */
    public static void setLoggedUser(UserModel user) {
        loggedUser = user;
    }

    /**
     * Gets the logged user.
     *
     * @return the logged user
     */
    public UserModel getLoggedUser() {
        return loggedUser;
    }

    /**
     * Closes the session by clearing the logged user and instance.
     */
    public static void closeSession() {
        loggedUser = null;
        instance = null;
    }

    /**
     * Sets the selected language.
     *
     * @param language the language to set
     */
    public static void setLanguage(Language language) {
        selectedLanguage = language;
    }

    /**
     * Gets the selected language.
     *
     * @return the selected language
     */
    public Language getSelectedLanguage() {
        return selectedLanguage;
    }

    /**
     * Sets the main controller.
     *
     * @param mainController the main controller to set
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Gets the main controller.
     *
     * @return the main controller
     */
    public MainController getMainController() {
        return mainController;
    }

    /**
     * Gets the profile controller.
     *
     * @return the profile controller
     */
    public ProfileController getProfileController() {
        return profileController;
    }

    /**
     * Sets the profile controller.
     *
     * @param profileController the profile controller to set
     */
    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }
}