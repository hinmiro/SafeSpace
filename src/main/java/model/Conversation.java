package model;

import java.util.*;

/**
 * The Conversation class represents a conversation between users, containing messages and metadata.
 */
public class Conversation {
    private UserModel withUser;
    private int messageCount;
    private List<Messages> messages;

    /**
     * Constructs a new Conversation instance with the specified user.
     *
     * @param withUser the user involved in the conversation
     */
    public Conversation(UserModel withUser) {
        this.withUser = withUser;
        this.messages = new ArrayList<>();
    }

    /**
     * Gets the user involved in the conversation.
     *
     * @return the user involved in the conversation
     */
    public UserModel getWithUser() {
        return withUser;
    }

    /**
     * Sets the user involved in the conversation.
     *
     * @param withUser the new user involved in the conversation
     */
    public void setWithUser(UserModel withUser) {
        this.withUser = withUser;
    }

    /**
     * Gets the message count in the conversation.
     *
     * @return the message count
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * Sets the message count in the conversation.
     *
     * @param messageCount the new message count
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * Gets the list of messages in the conversation.
     *
     * @return the list of messages
     */
    public List<Messages> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages in the conversation.
     *
     * @param messages the new list of messages
     */
    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    /**
     * Adds a message to the conversation.
     *
     * @param message the message to add
     */
    public void addMessage(Messages message) {
        messages.add(message);
    }
}