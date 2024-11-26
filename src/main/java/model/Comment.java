package model;

/**
 * The Comment class represents a comment made by a user.
 */
public class Comment {
    private int commentID;
    private int userID;
    private String username;
    private String commentContent;

    /**
     * Constructs a new Comment instance.
     *
     * @param commentID the ID of the comment
     * @param userID the ID of the user who made the comment
     * @param username the username of the user who made the comment
     * @param commentContent the content of the comment
     */
    public Comment(int commentID, int userID, String username, String commentContent) {
        this.commentID = commentID;
        this.userID = userID;
        this.username = username;
        this.commentContent = commentContent;
    }

    /**
     * Gets the ID of the comment.
     *
     * @return the comment ID
     */
    public int getCommentID() {
        return commentID;
    }

    /**
     * Sets the ID of the comment.
     *
     * @param commentID the new comment ID
     */
    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    /**
     * Gets the ID of the user who made the comment.
     *
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the user who made the comment.
     *
     * @param userID the new user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the username of the user who made the comment.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user who made the comment.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the content of the comment.
     *
     * @return the comment content
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * Sets the content of the comment.
     *
     * @param commentContent the new comment content
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}