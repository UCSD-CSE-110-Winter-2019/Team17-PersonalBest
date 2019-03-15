package com.example.team17_personalbest.Friends;

import android.util.Log;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.User;

import static android.support.constraint.Constraints.TAG;

public class FriendManager {

    private User user;
    private FirebaseAdapter cloud;

    /**
     * Constructor
     * @param user current user
     * @param cloud firestore database
     */
    public FriendManager(User user, FirebaseAdapter cloud){
        this.user = user;
        this.cloud = cloud;
    }

    /**
     * Send a friend request to the User with given email
     * @param friendEmail friend's email address
     */
    public void addFriend(String friendEmail){
        if(friendEmail.equals(user.getUserEmail())){
            Log.d(TAG, "Can't add yourself as a friend");
            return;
        }
        // add this email address to current user's pending friend list
        cloud.addPendingFriend(this.user.getUserEmail(), friendEmail);
        // add this request to the target friend's request list
        cloud.addPendingRequest(friendEmail, this.user.getUserEmail());
    }

    /**
     * Remove a friend from current user's friend list
     * @param friend friend's email address
     */
    public void removeFriend(String friend){
        cloud.removeFriend(this.user.getUserEmail(), friend);
        cloud.removeFriend(friend, this.user.getUserEmail());
    }

    public void removePendingFriend(String friend){
        cloud.removePendingFriend(this.user.getUserEmail(), friend);
        cloud.removePendingRequest(friend, this.user.getUserEmail());
    }

    /**
     * Accept a request from the User with given email
     * @param friend friend's email address
     */
    public void acceptFriendRequest(String friend){
        cloud.removePendingRequest(this.user.getUserEmail(), friend);
        cloud.addFriend(this.user.getUserEmail(), friend);
        cloud.removePendingFriend(friend, this.user.getUserEmail());
        cloud.addFriend(friend, this.user.getUserEmail());
    }

    /**
     * Deny a request from the User with given email
     * @param friend friend's email address
     */
    public void denyFriendRequest(String friend){
        cloud.removePendingFriend(friend, this.user.getUserEmail());
        cloud.removePendingRequest(this.user.getUserEmail(), friend);
    }

    /**
     * updates the friend lists from the cloud
     */
    public void updateFriends(){
        cloud.getUsersFromDB();
        cloud.getFriendsFromDB(user.getUserEmail());
        cloud.getPendingFriendsFromDB(user.getUserEmail());
        cloud.getPendingRequestsFromDB(user.getUserEmail());
        if(cloud.getFriends().size() > 0) {
            this.user.setHasFriends(true);
        } else {
            this.user.setHasFriends(false);
        }
    }
}
