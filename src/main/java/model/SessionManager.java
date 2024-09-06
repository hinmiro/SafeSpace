package model;

public class SessionManager {

    private static SessionManager INSTANCE;
    private static UserModel loggedUser;

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
}
