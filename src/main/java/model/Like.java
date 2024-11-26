package model;

/**
 * The Like class represents a "like" made by a user on a post.
 */
public class Like {
    private final int postID;
    private final int userID;

    /**
     * Constructs a new Like instance.
     *
     * @param userID the ID of the user who liked the post
     * @param postID the ID of the post that was liked
     */
    public Like(int userID, int postID) {
        this.userID = userID;
        this.postID = postID;
    }

    /**
     * Gets the ID of the user who liked the post.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userID;
    }

    /**
     * Gets the ID of the post that was liked.
     *
     * @return the post ID
     */
    public int getPostId() {
        return postID;
    }
}