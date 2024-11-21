package model;

import org.junit.*;
import static org.junit.Assert.*;

public class SessionManagerTest {
    private SessionManager manager = SessionManager.getInstance();
    private UserModel user;

    @Test
    public void setLoggedUser() {
        user = new UserModel("teaser", 7357, "testingDAy");
        manager.setLoggedUser(user);
        UserModel loggedUser = manager.getLoggedUser();
        assertEquals("teaser", loggedUser.getUsername());
    }

    @After
    public void tearDown() {
        manager = null;
        user = null;
    }
}