package com.example.team17_personalbest.Firestore;

import com.example.team17_personalbest.User;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CloudStroage extends Observable {

    private User user;
    CollectionReference friends;
    CollectionReference pendingFriends;
    CollectionReference requests;


    public CloudStroage(User user){
        this.user = user;

        this.friends = FirebaseFirestore.getInstance()
                .collection("users")
                .document(this.user.getUserName())
                .collection("friends");

        this.pendingFriends = FirebaseFirestore.getInstance()
                .collection("users")
                .document(this.user.getUserName())
                .collection("pendingfriends");

        this.requests = FirebaseFirestore.getInstance()
                .collection("users")
                .document(this.user.getUserName())
                .collection("requests");

        this.initFriendUpdateListener();

    }

    // TODO: getter for user name on cloud
    public String getUserName(String userEmail) {
        return "";
    }

    // TODO: Getters for information about friends on cloud
    public ArrayList<String> getFriends(String username){
        return new ArrayList<String>();
    }
    public ArrayList<String> getPendingFriends(String username){
        return new ArrayList<String>();
    }
    public ArrayList<String> getPendingRequests(String username){
        return new ArrayList<String>();
    }

    // TODO: These methods updates friends information on the cloud
    public void addFriend(String user, String friend){
        // add "friend" to friendList of "user"
    }
    public void addPendingFriend(String user, String friend){
        // add "friend" to pendingFriendList of "user"
    }
    public void addPendingRequest(String user, String friend){
        // add "friend" to requestList of "user"
    }
    public void removeFriend(String user, String friend){
        // remove "friend" from friendList of "user"
    }
    public void removePendingFriend(String user, String friend){
        // remove "friend" from pendingFriendList of "user"
    }
    public void removePendingRequest(String user, String friend){
        // remove "friend" from requestList of "user"
    }

    // TODO: check if user exists
    public boolean userExists(String email) {
        return true;
    }

    /**
     * Initialize listener for the friend
     * Whenever the cloud updates anything about friends, user will also be updated
     */
    private void initFriendUpdateListener() {
        this.friends.addSnapshotListener((newFriendSnapShot, error) -> {
            if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                ArrayList<String> newFriendList = new ArrayList<>();
                List<DocumentChange> documentChanges = newFriendSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    newFriendList.add(document.get("email").toString());
                });
                this.user.setFriends(newFriendList);
            }
        });
        this.pendingFriends.addSnapshotListener((newPendingSnapShot, error) -> {
            if (newPendingSnapShot != null && !newPendingSnapShot.isEmpty()) {
                ArrayList<String> newPendingList = new ArrayList<>();
                List<DocumentChange> documentChanges = newPendingSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    newPendingList.add(document.get("email").toString());
                });
                this.user.setPendingFriends(newPendingList);
            }
        });
        this.requests.addSnapshotListener((newRequestSnapShot, error) -> {
            if (newRequestSnapShot != null && !newRequestSnapShot.isEmpty()) {
                ArrayList<String> newRequestList = new ArrayList<>();
                List<DocumentChange> documentChanges = newRequestSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    newRequestList.add(document.get("email").toString());
                });
                this.user.setPendingRequests(newRequestList);
            }
        });
    }

}
