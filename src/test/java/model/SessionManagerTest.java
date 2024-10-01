package model;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SessionManagerTest {
    private Gson gson = new Gson();
    private SessionManager manager = SessionManager.getInstance();
    private UserModel user;

    @Before
    public void init() {
        user = new UserModel("teaser", 7357, "testingDAy");
    }

    @Test
    public void setLoggedUser() {
        manager.setLoggedUser(user);
        UserModel loggedUser = manager.getLoggedUser();
        assertEquals("teaser", loggedUser.getUsername());
    }

    @After
    public void tearDown() {
        gson = null;
        manager = null;
        user = null;
    }
}