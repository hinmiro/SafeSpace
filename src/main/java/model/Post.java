package model;

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

    public int getPostID() {
        return postID;
    }

    public int getPostCreatorID() {
        return postCreatorID;
    }

    public String getPostCreatorName() {
        return postCreatorName;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostPictureID() {
        return postPictureID;
    }

    public void setPostPictureID(String postPictureID) {
        this.postPictureID = postPictureID;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public int getCommentCount() {
        return commentCount;
    }
}
