package model;

/**
 * Represents a Post in the system.
 */
public class Post {
    private int postID;
    private int postCreatorID;
    private String postCreatorName;
    private String postContent;
    private String postPictureID;
    private String postDate;
    private int likeCount;
    private boolean likedByUser;
    private int commentCount;

    /**
     * Constructs a new Post.
     *
     * @param postID the ID of the post
     * @param postCreatorID the ID of the post creator
     * @param postCreatorName the name of the post creator
     * @param postContent the content of the post
     * @param postPictureID the ID of the post picture, or an empty string if null
     * @param postDate the date of the post
     * @param likeCount the number of likes the post has received
     * @param commentCount the number of comments on the post
     */
    public Post(int postID, int postCreatorID, String postCreatorName, String postContent, String postPictureID, String postDate, int likeCount, int commentCount) {
        this.postID = postID;
        this.postCreatorID = postCreatorID;
        this.postCreatorName = postCreatorName;
        this.postContent = postContent;
        this.postPictureID = postPictureID == null ? "" : postPictureID;
        this.postDate = postDate;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    /**
     * Gets the ID of the post.
     *
     * @return the post ID
     */
    public int getPostID() {
        return postID;
    }

    /**
     * Gets the ID of the post creator.
     *
     * @return the post creator ID
     */
    public int getPostCreatorID() {
        return postCreatorID;
    }

    /**
     * Gets the name of the post creator.
     *
     * @return the post creator name
     */
    public String getPostCreatorName() {
        return postCreatorName;
    }

    /**
     * Gets the content of the post.
     *
     * @return the post content
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     * Sets the content of the post.
     *
     * @param postContent the new content of the post
     */
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /**
     * Gets the ID of the post picture.
     *
     * @return the post picture ID
     */
    public String getPostPictureID() {
        return postPictureID;
    }

    /**
     * Sets the ID of the post picture.
     *
     * @param postPictureID the new post picture ID
     */
    public void setPostPictureID(String postPictureID) {
        this.postPictureID = postPictureID;
    }

    /**
     * Gets the date of the post.
     *
     * @return the post date
     */
    public String getPostDate() {
        return postDate;
    }

    /**
     * Sets the number of likes the post has received.
     *
     * @param likeCount the new like count
     */
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * Gets the number of likes the post has received.
     *
     * @return the like count
     */
    public int getLikeCount() {
        return likeCount;
    }

    /**
     * Sets whether the post is liked by the user.
     *
     * @param likedByUser the new liked by user status
     */
    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    /**
     * Gets the number of comments on the post.
     *
     * @return the comment count
     */
    public int getCommentCount() {
        return commentCount;
    }
}