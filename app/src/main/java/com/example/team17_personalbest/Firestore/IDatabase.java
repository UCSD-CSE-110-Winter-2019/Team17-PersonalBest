package com.example.team17_personalbest.Firestore;

import com.example.team17_personalbest.Step.StepHistory;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public interface IDatabase {
    void addUser(String uid, String name, String email);
    String getUserName(String userEmail);
    void addFriend(String userEmail, String friendEmail);
    void addPendingFriend(String userEmail, String friendEmail);
    void addPendingRequest(String userEmail, String friendEmail);
    void removeFriend(String userEmail, String friendEmail);
    void removePendingFriend(String userEmail, String friendEmail);
    void removePendingRequest(String userEmail, String friendEmail);
    ArrayList<String> getPendingRequests();
    ArrayList<String> getPendingFriends();
    ArrayList<String> getFriends();
    boolean areFriends(String friendEmail);
    void saveStepHistory(String userEmail, String stepHistory);
    void getStepHistory(String userName, StepHistory stepHistory);
    CollectionReference[] getChats(String userID1, String userID2);
}
