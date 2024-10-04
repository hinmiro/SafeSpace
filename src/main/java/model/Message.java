package model;

import java.util.List;

public class Message {
    private int messageID;
    private String message;
    private int sender;
    private int receiver;
    private String dateOfMessage;
    private List<Message> sentMessages;
    private List<Message> receivedMessages;

    public Message(int messageID, String message, int sender, int receiver, String dateOfMessage) {
        this.messageID = messageID;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.dateOfMessage = dateOfMessage;
    }

    public int getMessageId() {
        return messageID;
    }

    public void setMessageId(int messageId) {
        this.messageID = messageId;
    }

    public String getMessageContent() {
        return message;
    }

    public void setMessageContent(String messageContent) {
        this.message = messageContent;
    }

    public int getSenderId() {
        return sender;
    }

    public void setSenderId(int senderId) {
        this.sender = senderId;
    }

    public int getReceiverId() {
        return receiver;
    }

    public void setReceiverId(int receiverId) {
        this.receiver = receiverId;
    }

    public String getDateOfMessage() {
        return dateOfMessage;
    }

    public void setDateOfMessage(String dateOfMessage) {
        this.dateOfMessage = dateOfMessage;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

}
