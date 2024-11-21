package model;

public class Comment {
    private int commentID;
    private int userID;
    private String username;
    private String commentContent;

    public Comment(int commentID, int userID, String username, String commentContent) {
        this.commentID = commentID;
        this.userID = userID;
        this.username = username;
        this.commentContent = commentContent;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
