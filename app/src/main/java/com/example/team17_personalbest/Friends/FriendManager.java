package com.example.team17_personalbest.Friends;

import android.util.Pair;

import com.example.team17_personalbest.Firestore.CloudStroage;
import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        cloud.getUsersFromDB();
        cloud.getPendingFriendsFromDB(user.getUserEmail());
        cloud.getPendingRequestsFromDB(user.getUserEmail());
    }

    /**
     * Send a friend request to the User with given email
     * @param friendEmail friend's email address
     */
    public void addFriend(String friendEmail){
        // If email address doesn't exist, display
        /*if(!cloud.userExists(friend)) {
            // TODO: log error;
            return;
        }*/

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
        cloud.removeFriend(this.user.getUserName(), friend);
        cloud.removeFriend(friend, this.user.getUserName());
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
        cloud.removePendingFriend(friend, this.user.getUserName());
        cloud.removePendingRequest(this.user.getUserName(), friend);
    }

}
