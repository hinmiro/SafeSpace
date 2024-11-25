package model;

import java.util.*;

public class UserData {
    private int followingCount;
    private int followersCount;
    private int friendsCount;
    private List<UserModel> following;
    private List<UserModel> followers;
    private List<UserModel> friends;

    public UserData() {
        following = new ArrayList<>();
        followers = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public List<UserModel> getFollowing() {
        return following;
    }

    public List<UserModel> getFollowers() {
        return followers;
    }

    public List<UserModel> getFriends() {
        return friends;
    }

    public void setFollowing(List<UserModel> following) {
        this.following = following;
    }

    public void setFollowers(List<UserModel> followers) {
        this.followers = followers;
    }

    public void setFriends(List<UserModel> friends) {
        this.friends = friends;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public void addFollowing(UserModel followingUser) {
        following.add(followingUser);
        followingCount = following.size();
    }

    public void addFollowers(UserModel followerUser) {
        followers.add(followerUser);
        followersCount = followers.size();
    }

    public List<Integer> getFollowingUserIds() {
        ArrayList<Integer> followingUserIds = new ArrayList<>();
        for (UserModel user : following) {
            followingUserIds.add(user.getUserId());
        }
        return followingUserIds;
    }

    public List<Integer> getFriendsIds() {
        ArrayList<Integer> friendsIds = new ArrayList<>();
        for (UserModel user : friends) {
            friendsIds.add(user.getUserId());
        }
        return friendsIds;
    }
}
