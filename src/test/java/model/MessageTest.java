package model;

import org.junit.*;
import static org.junit.Assert.*;

public class MessageTest {
    Message message;

    @Before
    public void setUp() throws Exception {
        message = new Message("text", "Hello", "2024-11-14");
    }

    @After
    public void tearDown() throws Exception {
        message = null;
    }

    @Test
    public void getMessageId() {
        assertEquals("Message ID should be default", 0, message.getMessageId());
    }

    @Test
    public void setMessageId() {
        message.setMessageId(101);
        assertEquals("Message ID should change to 101", 101, message.getMessageId());
    }

    @Test
    public void getMessageContent() {
        assertEquals("Should have content 'Hello'", "Hello", message.getMessageContent());
    }

    @Test
    public void setMessageContent() {
        message.setMessageContent("New Message");
        assertEquals("Message content should change to 'New Message'", "New Message", message.getMessageContent());
    }

    @Test
    public void getSenderId() {
        assertEquals("Sender ID should be default", 0, message.getSenderId());
    }

    @Test
    public void setSenderId() {
        message.setSenderId(55);
        assertEquals("Sender ID should change to 55", 55, message.getSenderId());
    }

    @Test
    public void getReceiverId() {
        assertEquals("Receiver ID should be default", 0, message.getReceiverId());
    }

    @Test
    public void setReceiverId() {
        message.setReceiverId(99);
        assertEquals("Receiver ID should change to 99", 99, message.getReceiverId());
    }

    @Test
    public void getDateOfMessage() {
        assertEquals("Date should be '2024-11-14'", "2024-11-14", message.getDateOfMessage());
    }

    @Test
    public void setDateOfMessage() {
        message.setDateOfMessage("2024-11-15");
        assertEquals("Date should change to '2024-11-15'", "2024-11-15", message.getDateOfMessage());
    }

    @Test
    public void getType() {
        assertEquals("Type should be 'text'", "text", message.getType());
    }
}

