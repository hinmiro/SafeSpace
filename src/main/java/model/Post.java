package model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private int postID;
    private int postCreatorID;
    private String postCreatorName;
    private String postContent;
    private String postPictureID;
    private String postDate;
    private int likeCount;
    private List<Integer> likers;
    private boolean likedByUser;

    public Post(int postID, int postCreatorID,String postCreatorName, String postContent, String postPictureID, String postDate, int likeCount) {
        this.postID = postID;
        this.postCreatorID = postCreatorID;
        this.postCreatorName = postCreatorName;
        this.postContent = postContent;
        this.postPictureID = postPictureID == null ? "" : postPictureID;
        this.postDate = postDate;
        this.likeCount = likeCount;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(int postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public String getPostCreatorName() { return postCreatorName; }

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

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLikedByUser(Post item) {
        ArrayList<Integer> likedPosts = SessionManager.getInstance().getLoggedUser().getLikedPosts();
        return likedPosts != null && !likedPosts.contains(item.postID);
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }
}
