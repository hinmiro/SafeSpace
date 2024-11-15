package controller;

import model.Language;
import model.SessionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class ControllerForViewTest {
    private Locale locale;


    @Before
    public void setUp() throws Exception {
        if (SessionManager.getInstance().getSelectedLanguage() == null) {
            SessionManager.getInstance().setLanguage(Language.EN);
        }
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    }

    @After
    public void tearDown() throws Exception {
        locale = null;
    }

    @Test
    public void getInstance() {
        ControllerForView inst1 = ControllerForView.getInstance();
        ControllerForView inst2 = ControllerForView.getInstance();
        assertTrue("Should be same instance", inst2 == inst1);
    }

    @Test
    public void login() {
    }

    @Test
    public void register() {
    }

    @Test
    public void updateProfile() {
    }

    @Test
    public void sendPost() {
    }

    @Test
    public void sendPostWithImage() {
    }

    @Test
    public void getFeed() {
    }

    @Test
    public void getProfilePicture() {
    }

    @Test
    public void getPostPicture() {
    }

    @Test
    public void getUserPostsOwnProfile() {
    }

    @Test
    public void getUserById() {
    }

    @Test
    public void getUserByName() {
    }

    @Test
    public void testGetUserPostsOwnProfile() {
    }

    @Test
    public void removeLike() {
    }

    @Test
    public void getPostCommentsById() {
    }

    @Test
    public void addComment() {
    }

    @Test
    public void sendMessage() {
    }

    @Test
    public void getMessages() {
    }

    @Test
    public void getCurrentConversation() {
    }

    @Test
    public void addFriend() {
    }

    @Test
    public void removeFriend() {
    }

    @Test
    public void isFriend() {
    }
}