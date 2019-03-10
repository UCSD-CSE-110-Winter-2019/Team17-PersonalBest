package com.example.team17_personalbest.Friends;

import com.example.team17_personalbest.Firestore.CloudStroage;
import com.example.team17_personalbest.User;

public class FriendManager {

    private User user;
    private CloudStroage cloud;

    /**
     * Constructor
     * @param user current user
     * @param cloud firestore database
     */
    public FriendManager(User user, CloudStroage cloud){
        this.user = user;
        this.cloud = cloud;
    }

    /**
     * Send a friend request to the User with given email
     * @param friend friend's email address
     */
    public void addFriend(String friend){
        // If email address doesn't exist, display
        if(!cloud.userExists(friend)) {
            // TODO: log error;
            return;
        }

        // add this email address to current user's pending friend list
        cloud.addPendingFriend(this.user.getUserName(), friend);
        // add this request to the target friend's request list
        cloud.addPendingRequest(friend, this.user.getUserName());
    }

    /**
     * Remove a friend from current user's friend list
     * @param friend friend's email address
     */
    public void removeFriend(String friend){
        cloud.removeFriend(this.user.getUserName(), friend);
        cloud.removeFriend(friend, this.user.getUserName());
    }

    /**
     * Accept a request from the User with given email
     * @param friend friend's email address
     */
    public void acceptFriendRequest(String friend){
        cloud.removePendingFriend(this.user.getUserName(), friend);
        cloud.addFriend(this.user.getUserName(), friend);

        cloud.removePendingFriend(friend, this.user.getUserName());
        cloud.addFriend(friend, this.user.getUserName());
    }

    /**
     * Deny a request from the User with given email
     * @param friend friend's email address
     */
    public void denyFriendRequest(String friend){
        cloud.removePendingFriend(friend, this.user.getUserName());
        cloud.removePendingRequest(this.user.getUserName(), friend);
    }

}
