package model;

import org.junit.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class UserDataTest {
    private UserData userData;

    @Before
    public void setUp(){
        userData = new UserData();
    }

    @After
    public void tearDown() {
        userData = null;
    }

    @Test
    public void testGetFollowingCount() {
        assertEquals("Following count should be 0", 0, userData.getFollowingCount());
        userData.setFollowingCount(1);
        assertEquals("Following count should be 1", 1, userData.getFollowingCount());
    }

    @Test
    public void testGetFollowersCount() {
        assertEquals("Followers count should be 0", 0, userData.getFollowersCount());
    }

    @Test
    public void testGetFriendsCount() {
        assertEquals("Friends count should be 0", 0, userData.getFriendsCount());
    }

    @Test
    public void testGetFollowing() {
        assertNotNull("Following list should not be null", userData.getFollowing());
    }

    @Test
    public void testGetFollowers() {
        assertNotNull("Followers list should not be null", userData.getFollowers());
    }

    @Test
    public void testGetFriends() {
        assertNotNull("Friends list should not be null", userData.getFriends());
    }

    @Test
    public void testSetFollowing() {
        ArrayList<UserModel> following = new ArrayList<>();
        userData.setFollowing(following);
        assertSame("Following list should be set", following, userData.getFollowing());
    }

    @Test
    public void testSetFollowers() {
        ArrayList<UserModel> followers = new ArrayList<>();
        userData.setFollowers(followers);
        assertSame("Followers list should be set", followers, userData.getFollowers());
    }

    @Test
    public void testSetFriends() {
        ArrayList<UserModel> friends = new ArrayList<>();
        userData.setFriends(friends);
        assertSame("Friends list should be set", friends, userData.getFriends());
    }

    @Test
    public void testSetFollowingCount() {
        userData.setFollowingCount(5);
        assertEquals("Following count should be 5", 5, userData.getFollowingCount());
    }

    @Test
    public void testSetFollowersCount() {
        userData.setFollowersCount(10);
        assertEquals("Followers count should be 10", 10, userData.getFollowersCount());
    }

    @Test
    public void testSetFriendsCount() {
        userData.setFriendsCount(15);
        assertEquals("Friends count should be 15", 15, userData.getFriendsCount());
    }

    @Test
    public void testAddFollowing() {
        UserModel user = new UserModel("tester", 2, "testing day");
        userData.addFollowing(user);
        assertTrue("Following list should contain the user", userData.getFollowing().contains(user));
        assertEquals("Following count should be 1", 1, userData.getFollowingCount());
    }

    @Test
    public void testAddFollowers() {
        UserModel user = new UserModel("tester", 2, "testing day");
        userData.addFollowers(user);
        assertTrue("Followers list should contain the user", userData.getFollowers().contains(user));
        assertEquals("Followers count should be 1", 1, userData.getFollowersCount());
    }
}