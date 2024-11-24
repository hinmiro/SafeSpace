package model;

public class Message {
    private int messageID;
    private String messageText;
    private int sender;
    private int receiver;
    private String date;
    private final String type;

    public Message(String type, String message, String dateOfMessage) {
        this.messageText = message;
        this.date = dateOfMessage;
        this.type = type;
    }

    public int getMessageId() {
        return messageID;
    }

    public void setMessageId(int messageId) {
        this.messageID = messageId;
    }

    public String getMessageContent() {
        return messageText;
    }

    public void setMessageContent(String messageContent) {
        this.messageText = messageContent;
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
        return date;
    }

    public void setDateOfMessage(String dateOfMessage) {
        this.date = dateOfMessage;
    }

    public String getType() {
        return type;
    }
}
