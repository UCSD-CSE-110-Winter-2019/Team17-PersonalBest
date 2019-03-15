package com.example.team17_personalbest.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.team17_personalbest.Step.StepHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.support.constraint.Constraints.TAG;

/**
 * Class that handles interactions with Firestore database
 */
public class FirebaseAdapter implements IDatabase {

    FirebaseFirestore db;

    private String CHAT_COLLECTION = "chats";

    private String USER_ID = "u_id";
    private String USER_NAME = "u_name";
    private String USER_EMAIL = "u_email";
    private String USER_STEPHIST = "u_stephist";

    private String USER_COLLECTION = "users";
    private String PENDING_COLLECTION = "pending";
    private String REQUEST_COLLECTION = "requests";
    private String FRIEND_COLLECTION = "friends";
    private String MESSAGES_COLLECTION = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";

    private HashMap<String, String> users;     // Users in database
    private ArrayList<String> friends;
    private ArrayList<String> pendingFriends;
    private ArrayList<String> pendingRequests;

    /**
     * Constructor that initializes users and database
     * @param firebaseFirestore
     */
    public FirebaseAdapter(FirebaseFirestore firebaseFirestore) {
        db = firebaseFirestore;
        users = new HashMap<>();
        friends = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        pendingFriends = new ArrayList<>();
    }

    /**
     * Adds a users information to the database
     * @param uid
     * @param name
     * @param email
     */
    @Override
    public void addUser(String uid, String name, String email){
        HashMap<String, Object> user = new HashMap<>();
        user.put(USER_ID, uid);
        user.put(USER_NAME, name);
        user.put(USER_EMAIL, email);

        // Check if user exists and adds it to the database
        db.collection(USER_COLLECTION)
                .document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "User exists!");
                            } else {
                                Log.d(TAG, "User does not exist!");
                                db.collection(USER_COLLECTION)
                                        .document(email)
                                        .set(user);
                            }
                            getUsersFromDB();
                            getFriendsFromDB(email);
                            getPendingFriendsFromDB(email);
                            getPendingRequestsFromDB(email);
                        } else {
                            Log.e(TAG, "Failed with: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Get the username of a user given their email
     * @param userEmail
     * @return
     */
    @Override
    public String getUserName(String userEmail) {
        if(users.containsKey(userEmail)){
            return users.get(userEmail);
        }
        return "";
    }

    /**
     * Gets the users from the database
     */
    public void getUsersFromDB(){
        db.collection(USER_COLLECTION)
                .addSnapshotListener((newFriendSnapShot, error) -> {
                    if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                        List<DocumentSnapshot> documentChanges = newFriendSnapShot.getDocuments();
                        documentChanges.forEach(change -> {
                            if(!users.containsKey((String) change.getId())) {
                                users.put((String) change.getId(), (String) change.get(USER_NAME));
                                Log.d(TAG, "User " + change.getId() + " added!");
                            }
                        });
                    }
                });
    }

    /**
     * Getters for the friends if a user
     * @param userEmail the email of the user
     */
    public void getFriendsFromDB(String userEmail){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(FRIEND_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        friends.clear();
                        List<DocumentSnapshot> documentChanges = queryDocumentSnapshots.getDocuments();
                        documentChanges.forEach(change -> {
                            if(!friends.contains((String) change.getId())) {
                                friends.add((String) change.getId());
                                Log.d(TAG, "Friend " + change.getId() + " added!");
                            }
                        });
                    }
                });
    }

    /**
     * Getters for the pending friends of a user
     * @param userEmail the email of the user
     */
    public void getPendingFriendsFromDB(String userEmail){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(PENDING_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        pendingFriends.clear();
                        List<DocumentSnapshot> documentChanges = queryDocumentSnapshots.getDocuments();
                        documentChanges.forEach(change -> {
                            if(!pendingFriends.contains((String) change.getId())) {
                                pendingFriends.add((String) change.getId());
                                Log.d(TAG, "Pending friend " + change.getId() + " added!");
                            }
                        });
                    }
                });
    }

    /**
     * Getters for the friend requests of a user
     * @param userEmail the email of the user
     */
    public void getPendingRequestsFromDB(String userEmail){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(REQUEST_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        pendingRequests.clear();
                        List<DocumentSnapshot> documentChanges = queryDocumentSnapshots.getDocuments();
                        documentChanges.forEach(change -> {
                            if(!pendingRequests.contains((String) change.getId())) {
                                pendingRequests.add((String) change.getId());
                                Log.d(TAG, "Friend request " + change.getId() + " added!");
                            }
                        });
                    }
                });
    }

    /**
     * Update friend information on the database
     * @param userEmail
     * @param friendEmail
     */
    @Override
    public void addFriend(String userEmail, String friendEmail){
        // add "friend" to friendList of "user"
        HashMap<String, String> friend = new HashMap<>();
        friend.put("status", "friends");

        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(FRIEND_COLLECTION)
                .document(friendEmail)
                .set(friend);

        Log.d(TAG,"Added friends " + friendEmail + " to " + userEmail);
    }

    @Override
    public void addPendingFriend(String userEmail, String friendEmail){
        // add "friend" to pendingFriendList of "user"
        HashMap<String, String> friend = new HashMap<>();
        friend.put("status", "pending");

        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(PENDING_COLLECTION)
                .document(friendEmail)
                .set(friend);
        Log.d(TAG,"Added pending friends " + friendEmail + " to " + userEmail);
    }
    @Override
    public void addPendingRequest(String userEmail, String friendEmail){
        // add "friend" to requestList of "user"
        HashMap<String, String> friend = new HashMap<>();
        friend.put("status", "requested");

        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(REQUEST_COLLECTION)
                .document(friendEmail)
                .set(friend);

        Log.d(TAG,"Added friend request " + friendEmail + " to " + userEmail);
    }
    @Override
    public void removeFriend(String userEmail, String friendEmail){
        // remove "friend" from friendList of "user"
        friends.remove(friendEmail);
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(FRIEND_COLLECTION)
                .document(friendEmail)
                .delete();

        Log.d(TAG,"Removed friend " + friendEmail + " from " + userEmail);
    }
    @Override
    public void removePendingFriend(String userEmail, String friendEmail){
        // remove "friend" from pendingFriendList of "user"
        pendingFriends.remove(friendEmail);
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(PENDING_COLLECTION)
                .document(friendEmail)
                .delete();
        Log.d(TAG,"Removed pending friend " + friendEmail + " from " + userEmail);
    }
    @Override
    public void removePendingRequest(String userEmail, String friendEmail){
        // remove "friend" from requestList of "user"
        pendingRequests.remove(friendEmail);
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(REQUEST_COLLECTION)
                .document(friendEmail)
                .delete();
        Log.d(TAG,"Removed friend request " + friendEmail + " from " + userEmail);
    }

    /**
     * Getters for friend lists
     */
    @Override
    public ArrayList<String> getPendingRequests() {
        return pendingRequests;
    }
    @Override
    public ArrayList<String> getPendingFriends() {
        return pendingFriends;
    }
    @Override
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Checks whether a user exists and if they're already friends with the user
     * @param friendEmail the email of the friend
     * @return
     */
    @Override
    public boolean areFriends(String friendEmail) {
        // Checks if friend is user
        if(!users.containsKey(friendEmail)){
            Log.d(TAG, "User " + friendEmail + " does not exist!");
            return true;
        }

        // Checks if friend is already in friend list
        if(friends.contains(friendEmail) || pendingRequests.contains(friendEmail)
                || pendingFriends.contains(friendEmail)){
            Log.d(TAG, "User " + friendEmail + " is already a friend!");
            return true;
        }

        Log.d(TAG, "User " + friendEmail + " is not a friend!");
        return false;
    }

    @Override
    public void saveStepHistory(String userEmail, String stepHistory){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                db.collection(USER_COLLECTION)
                                        .document(userEmail)
                                        .update(USER_STEPHIST, stepHistory);
                                Log.d(TAG, "Updated " + userEmail + " step history");
                            }else{
                                Log.d(TAG, "User does not exist");
                            }
                        } else {
                            Log.e(TAG, "Failed with: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void getStepHistory(String userEmail, StepHistory stepHistory) {
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Gson gson = new Gson();
                            stepHistory.setHist(gson.fromJson((String)documentSnapshot.get(USER_STEPHIST), StepHistory.class));
                            Log.d(TAG, userEmail + " step history gotten");
                        }else{
                            Log.e(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Gets the collection of chat messages between userID1 and userID2
     * @param userID1
     * @param userID2
     * @return the chats between user1 and user2: the first element is the chat stored under
     *         user1 and the second element is the chat stored under user2
     */
    @Override
    public CollectionReference[] getChats(String userID1, String userID2){
        CollectionReference[] chats = new CollectionReference[2];
        chats[0] = db.collection(USER_COLLECTION)
                .document(userID1)
                .collection(CHAT_COLLECTION)
                .document(userID2)
                .collection(MESSAGES_COLLECTION);
        chats[1] = db.collection(USER_COLLECTION)
                .document(userID2)
                .collection(CHAT_COLLECTION)
                .document(userID1)
                .collection(MESSAGES_COLLECTION);
        return chats;

    }

    /**
     * Adds a message into the chats stored under each user
     * @param user1Email the user sending the message
     * @param user2Email the user receiving the message
     * @param text the message
     */
    public Task<DocumentReference> sendMessage(String user1Email, String user2Email, String text){
        //TODO: Add cloud function for timestamps (not here in java code)
        HashMap<String, String> message = new HashMap<>();
        message.put(FROM_KEY, getUserName(user1Email));
        message.put(TEXT_KEY, text);
        CollectionReference[] chats = getChats(user1Email, user2Email);
        Task<DocumentReference> task = null;
        for (CollectionReference chat : chats) {
            task = chat.add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Message added to chat");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding message to chat", e);
                        }
                    });
        }
        return task;
    }
}
