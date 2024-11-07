package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserModelTest {
    UserModel user;

    @Before
    public void setUp() throws Exception {
        user = new UserModel("tester", 1, "testing day");
        user.setJwt("JWTJWT");

    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void getUsername() {
        assertEquals("Should be", "tester", user.getUsername());
    }

    @Test
    public void getJwt() {
        assertEquals("Should be ", "JWTJWT", user.getJwt());
    }

    @Test
    public void setJwt() {
        user.setJwt("COMRAD");
        assertEquals("Should have changed to ", "COMRAD", user.getJwt());
    }

    @Test
    public void setUsername() {
        user.setUsername("ASD");
        assertEquals("Should have changed to", "ASD", user.getUsername());
    }

    @Test
    public void setBio() {
        user.setBio("BIO");
        assertEquals("Should have set to ", "BIO", user.getBio());
    }

    @Test
    public void setProfilePictureUrl() {
        user.setProfilePictureUrl("catImage");
        assertEquals("Should be set to ", "catImage", user.getProfilePictureUrl());
    }

    @Test
    public void getBio() {
        user.setBio("BIO");
        assertEquals("Should have set to ", "BIO", user.getBio());
    }

    @Test
    public void getProfilePictureUrl() {
        user.setProfilePictureUrl("catImage");
        assertEquals("Should be set to ", "catImage", user.getProfilePictureUrl());
    }

    @Test
    public void getUserId() {
        assertEquals("Should be 1", 1, user.getUserId());
    }

    @Test
    public void getDateOfCreation() {
        assertEquals("Should be testing day", "testing day", user.getDateOfCreation());
    }

    @Test
    public void getLikedPosts() {
        user.addLikedPost(2);
        assertEquals("liked post should be", 1, user.getLikedPosts().size());
    }

    @Test
    public void setLikedPosts() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        user.setLikedPosts(list);
        assertEquals("Should have liked posts ", 2, user.getLikedPosts().size());
    }

    @Test
    public void getPosts() {
      assertEquals("Should not have posts", 0, user.getPosts().size());
    }

    @Test
    public void setPosts() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        user.setPosts(list);
        assertEquals("Should have posts ", 2, user.getPosts().size());
    }

    @Test
    public void addLikedPost() {
        user.addLikedPost(2);
        assertEquals("liked post should be", 1, user.getLikedPosts().size());
    }

    @Test
    public void removeLikedPost() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        user.setLikedPosts(list);
        user.removeLikedPost(3);
        assertEquals("Should have only 1 post", 1, user.getLikedPosts().size());
    }

    @Test
    public void getUserData() {
        UserData data = new UserData();
        user.setUserData(data);
        assertEquals("should be same as", data, user.getUserData());
    }

    @Test
    public void setUserData() {
        UserData data = new UserData();
        user.setUserData(data);
        assertEquals("should be same as", data, user.getUserData());
    }
}