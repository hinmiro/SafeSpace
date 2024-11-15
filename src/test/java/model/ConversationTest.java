package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ConversationTest {
    Conversation conversation;
    UserModel user;
    Message message;

    @Before
    public void setUp() {
        user = new UserModel("username", 1, "testing day");
        conversation = new Conversation(user);
        message = new Message("text", "Hello", "2024-11-14");
    }

    @After
    public void tearDown() {
        conversation = null;
        user = null;
        message = null;
    }

    @Test
    public void testGetWithUser() {
        assertEquals("Should be", user, conversation.getWithUser());
    }

    @Test
    public void testSetWithUser() {
        UserModel newUser = new UserModel("newUser", 2, "new day");
        conversation.setWithUser(newUser);
        assertEquals("Should be", newUser, conversation.getWithUser());
    }

    @Test
    public void testGetMessageCount() {
        assertEquals("Should be", 0, conversation.getMessageCount());
    }

    @Test
    public void testSetMessageCount() {
        conversation.setMessageCount(2);
        assertEquals("Should be", 2, conversation.getMessageCount());
    }

    @Test
    public void testGetMessages() {
        assertEquals("Should be", 0, conversation.getMessages().size());
    }

    @Test
    public void testSetMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        conversation.setMessages(messages);
        assertEquals("Should be", 1, conversation.getMessages().size());
    }

    @Test
    public void testAddMessage() {
        conversation.addMessage(message);
        assertEquals("Should be", 1, conversation.getMessages().size());
    }
}
