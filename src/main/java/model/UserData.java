package model;

import java.util.ArrayList;

public class UserData {
    private int followingCount;
    private int followersCount;
    private int friendsCount;
    private ArrayList<UserModel> following;
    private ArrayList<UserModel> followers;
    private ArrayList<UserModel> friends;

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

    public ArrayList<UserModel> getFollowing() {
        return following;
    }

    public ArrayList<UserModel> getFollowers() {
        return followers;
    }

    public ArrayList<UserModel> getFriends() {
        return friends;
    }

    public void setFollowing(ArrayList<UserModel> following) {
        this.following = following;
    }

    public void setFollowers(ArrayList<UserModel> followers) {
        this.followers = followers;
    }

    public void setFriends(ArrayList<UserModel> friends) {
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

    public void addFriends(UserModel friend) {
        friends.add(friend);
        friendsCount = friends.size();
    }
}
