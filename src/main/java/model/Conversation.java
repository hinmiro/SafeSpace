package model;

import java.util.ArrayList;

public class Conversation {
    private UserModel withUser;
    private int messageCount;
    private ArrayList<Message> messages;

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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}



