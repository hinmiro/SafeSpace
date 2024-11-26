package model;

import java.util.*;

public class UserData {
    private int followingCount;
    private int followersCount;
    private int friendsCount;
    private List<UserModel> following;
    private List<UserModel> followers;
    private List<UserModel> friends;

    /**
     * Constructs a new UserData object with empty lists for following, followers, and friends.
     */
    public UserData() {
        following = new ArrayList<>();
        followers = new ArrayList<>();
        friends = new ArrayList<>();
    }

    /**
     * Returns the count of users being followed.
     *
     * @return the following count
     */
    public int getFollowingCount() {
        return followingCount;
    }

    /**
     * Returns the count of followers.
     *
     * @return the followers count
     */
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * Returns the count of friends.
     *
     * @return the friends count
     */
    public int getFriendsCount() {
        return friendsCount;
    }

    /**
     * Returns the list of users being followed.
     *
     * @return the list of following users
     */
    public List<UserModel> getFollowing() {
        return following;
    }

    /**
     * Returns the list of followers.
     *
     * @return the list of followers
     */
    public List<UserModel> getFollowers() {
        return followers;
    }

    /**
     * Returns the list of friends.
     *
     * @return the list of friends
     */
    public List<UserModel> getFriends() {
        return friends;
    }

    /**
     * Sets the list of users being followed.
     *
     * @param following the list of following users
     */
    public void setFollowing(List<UserModel> following) {
        this.following = following;
    }

    /**
     * Sets the list of followers.
     *
     * @param followers the list of followers
     */
    public void setFollowers(List<UserModel> followers) {
        this.followers = followers;
    }

    /**
     * Sets the list of friends.
     *
     * @param friends the list of friends
     */
    public void setFriends(List<UserModel> friends) {
        this.friends = friends;
    }

    /**
     * Sets the count of users being followed.
     *
     * @param followingCount the following count
     */
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    /**
     * Sets the count of followers.
     *
     * @param followersCount the followers count
     */
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * Sets the count of friends.
     *
     * @param friendsCount the friends count
     */
    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    /**
     * Adds a user to the following list and updates the following count.
     *
     * @param followingUser the user to add to the following list
     */
    public void addFollowing(UserModel followingUser) {
        following.add(followingUser);
        followingCount = following.size();
    }

    /**
     * Adds a user to the followers list and updates the followers count.
     *
     * @param followerUser the user to add to the followers list
     */
    public void addFollowers(UserModel followerUser) {
        followers.add(followerUser);
        followersCount = followers.size();
    }

    /**
     * Returns a list of IDs of users being followed.
     *
     * @return the list of following user IDs
     */
    public List<Integer> getFollowingUserIds() {
        ArrayList<Integer> followingUserIds = new ArrayList<>();
        for (UserModel user : following) {
            followingUserIds.add(user.getUserId());
        }
        return followingUserIds;
    }

    /**
     * Returns a list of IDs of friends.
     *
     * @return the list of friends IDs
     */
    public List<Integer> getFriendsIds() {
        ArrayList<Integer> friendsIds = new ArrayList<>();
        for (UserModel user : friends) {
            friendsIds.add(user.getUserId());
        }
        return friendsIds;
    }
}