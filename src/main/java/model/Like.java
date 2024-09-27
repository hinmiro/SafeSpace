package model;

public class Like {
    private int postID;
    private int userID;

    public Like(int userID, int postID) {
        this.userID = userID;
        this.postID = postID;
    }

    public int getUserId() {
        return userID;
    }

    public int getPostId() {
        return postID;
    }
}
