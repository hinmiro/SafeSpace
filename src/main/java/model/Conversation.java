package model;

import java.util.*;

public class Conversation {
    private UserModel withUser;
    private int messageCount;
    private List<Messages> messages;

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

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public void addMessage(Messages message) {
        messages.add(message);
    }
}



