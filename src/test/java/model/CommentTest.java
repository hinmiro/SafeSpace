package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    Comment comment;

    @Before
    public void setUp() throws Exception {
        comment = new Comment(0, 0, "tester", "This is comment");
    }

    @After
    public void tearDown() throws Exception {
        comment = null;
    }

    @Test
    public void getCommentID() {
        assertEquals("Should be id of comment", 0, comment.getCommentID());
    }

    @Test
    public void setCommentID() {
        comment.setCommentID(55);
        assertEquals("Id should change to 55", 55, comment.getCommentID());
    }

    @Test
    public void getUserID() {
        assertEquals("User id should be 0", 0, comment.getUserID());
    }

    @Test
    public void setUserID() {
        comment.setUserID(69);
        assertEquals("User id should change to 69", 69, comment.getUserID());
    }

    @Test
    public void getUsername() {
        assertEquals("Should have username tester in it", "tester", comment.getUsername());
    }

    @Test
    public void setUsername() {
        comment.setUsername("Not tester");
        assertEquals("Should change username to Not tester", "Not tester", comment.getUsername());
    }

    @Test
    public void getCommentContent() {
        assertEquals("Should have content in it", "This is comment", comment.getCommentContent());
    }

    @Test
    public void setCommentContent() {
        comment.setCommentContent("ASD");
        assertEquals("Should change content to ", "ASD", comment.getCommentContent());
    }
}