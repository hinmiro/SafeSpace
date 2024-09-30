package model;

public class Message {
    private int messageId;
    private String messageContent;
    private int senderId;
    private int receiverId;
    private String dateOfMessage;

    public Message(int messageId, String messageContent, int senderId, int receiverId, String dateOfMessage) {
        this.messageId = messageId;
        this.messageContent = messageContent;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.dateOfMessage = dateOfMessage;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getDateOfMessage() {
        return dateOfMessage;
    }

    public void setDateOfMessage(String dateOfMessage) {
        this.dateOfMessage = dateOfMessage;
    }
}
