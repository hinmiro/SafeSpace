package model;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SessionManagerTest {
    private SessionManager manager = SessionManager.getInstance();
    private UserModel user;

    @Ignore
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