package model;

/**
 * The Messages class represents a message sent between users.
 */
public class Messages {
    private int messageID;
    private String message;
    private int sender;
    private int receiver;
    private String date;
    private final String type;

    /**
     * Constructs a new Messages instance.
     *
     * @param type the type of the message
     * @param message the content of the message
     * @param dateOfMessage the date the message was sent
     */
    public Messages(String type, String message, String dateOfMessage) {
        this.message = message;
        this.date = dateOfMessage;
        this.type = type;
    }

    /**
     * Gets the ID of the message.
     *
     * @return the message ID
     */
    public int getMessageId() {
        return messageID;
    }

    /**
     * Sets the ID of the message.
     *
     * @param messageId the new message ID
     */
    public void setMessageId(int messageId) {
        this.messageID = messageId;
    }

    /**
     * Gets the content of the message.
     *
     * @return the message content
     */
    public String getMessageContent() {
        return message;
    }

    /**
     * Sets the content of the message.
     *
     * @param messageContent the new message content
     */
    public void setMessageContent(String messageContent) {
        this.message = messageContent;
    }

    /**
     * Gets the ID of the sender of the message.
     *
     * @return the sender ID
     */
    public int getSenderId() {
        return sender;
    }

    /**
     * Sets the ID of the sender of the message.
     *
     * @param senderId the new sender ID
     */
    public void setSenderId(int senderId) {
        this.sender = senderId;
    }

    /**
     * Gets the ID of the receiver of the message.
     *
     * @return the receiver ID
     */
    public int getReceiverId() {
        return receiver;
    }

    /**
     * Sets the ID of the receiver of the message.
     *
     * @param receiverId the new receiver ID
     */
    public void setReceiverId(int receiverId) {
        this.receiver = receiverId;
    }

    /**
     * Gets the date the message was sent.
     *
     * @return the date of the message
     */
    public String getDateOfMessage() {
        return date;
    }

    /**
     * Sets the date the message was sent.
     *
     * @param dateOfMessage the new date of the message
     */
    public void setDateOfMessage(String dateOfMessage) {
        this.date = dateOfMessage;
    }

    /**
     * Gets the type of the message.
     *
     * @return the message type
     */
    public String getType() {
        return type;
    }
}