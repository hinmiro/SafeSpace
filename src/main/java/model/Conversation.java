package model;

import java.util.*;

public class Conversation {
    private UserModel withUser;
    private int messageCount;
    private List<Message> messages;

    public Conversation(UserModel withUser) {
        this.withUser = withUser;
        this.messages = new ArrayList<>();
    }

    public UserModel getWithUser() {
        return withUser;
    }

    public void setWithUser(UserModel withUser) {
        this.withUser = withUser;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}



