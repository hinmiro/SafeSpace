package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LikeTest {
    Like like;

    @Before
    public void setUp() {
        like = new Like(1, 2);
    }

    @After
    public void tearDown() {
        like = null;
    }

    @Test
    public void testGetUserId() {
        assertEquals("User ID should be 1", 1, like.getUserId());
    }

    @Test
    public void testGetPostId() {
        assertEquals("Post ID should be 2", 2, like.getPostId());
    }
}
