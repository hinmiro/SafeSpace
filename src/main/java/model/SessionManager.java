package model;

public class SessionManager {

    private static SessionManager INSTANCE;
    private static UserModel loggedUser;
    private String fullName;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void closeSession() {
        loggedUser = null;
        INSTANCE = null;
    }
}
