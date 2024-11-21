package model;

import org.junit.*;
import java.util.*;

import static org.junit.Assert.*;

public class PostTest {
    private Locale locale;
    UserModel user;
    Post post;

    @Before
    public void setUp() throws Exception {
        post = new Post(1, 1, "tester", "posted post", "555", "testing day", 1, 1);
        if (SessionManager.getInstance().getSelectedLanguage() == null) {
            SessionManager.getInstance().setLanguage(Language.EN);
        }
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

        user = new UserModel("test", 6, "testing day");
        SessionManager.getInstance().setLoggedUser(user);
    }

    @After
    public void tearDown() throws Exception {
        post = null;
        locale = null;
        user = null;
    }

    @Test
    public void getPostID() {
        assertEquals("Should be 1", 1, post.getPostID());
    }

    @Test
    public void getPostCreatorID() {
        assertEquals("Should be 1", 1, post.getPostCreatorID());
    }

    @Test
    public void getPostCreatorName() {
        assertEquals("Should be tester", "tester", post.getPostCreatorName());
    }

    @Test
    public void getPostContent() {
        assertEquals("Should be posted post", "posted post", post.getPostContent());
    }

    @Test
    public void setPostContent() {
        post.setPostContent("asd");
        assertEquals("should change content ", "asd", post.getPostContent());
    }

    @Test
    public void getPostPictureID() {
        assertEquals("should be 555", "555", post.getPostPictureID());
    }

    @Test
    public void setPostPictureID() {
        post.setPostPictureID("666");
        assertEquals("Should be changed to ", "666", post.getPostPictureID());
    }

    @Test
    public void getPostDate() {
        assertEquals("Should be ", "testing day", post.getPostDate());
    }

    @Test
    public void setLikeCount() {
        post.setLikeCount(5);
        assertEquals("likes should have changed ", 5, post.getLikeCount());
    }

    @Test
    public void getLikeCount() {
        assertEquals("should be " , 1, post.getLikeCount());
    }

    @Test
    public void isLikedByUser() {
        user.addLikedPost(1);
        ArrayList<Integer> likedPosts = SessionManager.getInstance().getLoggedUser().getLikedPosts();
        assertTrue("Should contain post id", likedPosts.contains(1));
    }

    @Test
    public void getCommentCount() {
        assertEquals("Should be ", 1, post.getCommentCount());
    }
}