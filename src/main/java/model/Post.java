package model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private int postID;
    private int postCreatorID;
    private String postContent;
    private String postPictureID;
    private String postDate;
    private int likeCount;
    private int commentCount;
    private List<Integer> likers;
    private boolean likedByUser;

    public Post(int postID, int postCreatorID, String postContent, String postPictureID, String postDate, int likeCount, int commentCount) {
        this.postID = postID;
        this.postCreatorID = postCreatorID;
        this.postContent = postContent;
        this.postPictureID = postPictureID == null ? "" : postPictureID;
        this.postDate = postDate;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.likers = new ArrayList<>();
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

    public List<Integer> getLikers() {
        return likers;
    }

    public void setLikers(List<Integer> likers) {
        this.likers = likers;
    }

    public int getLikeCount() {
        return likers != null ? likers.size() : 0;
    }

    public boolean isLikedByUser(int currentUserId) {
        return likers != null && likers.contains(currentUserId);
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
