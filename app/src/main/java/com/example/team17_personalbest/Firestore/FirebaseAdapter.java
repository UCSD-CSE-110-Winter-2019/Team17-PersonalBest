package com.example.team17_personalbest.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

/**
 * Class that handles interactions with Firestore database
 */
public class FirebaseAdapter {

    FirebaseFirestore db;

    private String CHAT_COLLECTION = "chats";

    private String USER_ID = "u_id";
    private String USER_NAME = "u_name";
    private String USER_EMAIL = "u_email";

    private String USER_COLLECTION = "users";
    private String PENDING_COLLECTION = "pending";
    private String REQUEST_COLLECTION = "requests";
    private String FRIEND_COLLECTION = "friends";
    private String MESSAGES_COLLECTION = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";

    private HashMap<String, String> users;     // Users in database
    private HashMap<String, String> friends;
    private HashMap<String, String> pendingFriends;
    private HashMap<String, String> pendingRequests;

    /**
     * Constructor that initializes users and database
     * @param firebaseFirestore
     */
    public FirebaseAdapter(FirebaseFirestore firebaseFirestore) {
        db = firebaseFirestore;
        users = new HashMap<>();
        friends = new HashMap<>();
        pendingRequests = new HashMap<>();
        pendingFriends = new HashMap<>();
    }

    /**
     * Adds a users information to the database
     * @param uid
     * @param name
     * @param email
     */
    public void addUser(String uid, String name, String email){
        // Checks if user is already in database
        if(users.containsKey(uid)) return;

        HashMap<String, Object> user = new HashMap<>();
        user.put(USER_ID, uid);
        user.put(USER_NAME, name);
        user.put(USER_EMAIL, email);

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
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Gets all documents in a collection in the database
     * @param collection
     * @return List of all documents
     */
    public List<DocumentSnapshot> getDocuments(String collection){

        final List<DocumentSnapshot> result = new ArrayList<>();

        CollectionReference ref = db.collection(collection);
        ref.addSnapshotListener((newChatSnapShot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }

            if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                List<DocumentSnapshot> document = newChatSnapShot.getDocuments();
                result.addAll(document);
            }
        });

        return result;
    }

    // TODO: getter for user name on cloud
    public String getUserName(String userEmail) {
        if(users.containsKey(userEmail)){
            return users.get(userEmail);
        }
        return "";
    }

    // TODO: Getters for information about friends on cloud
    public void getUsersFromDB(){
        db.collection(USER_COLLECTION)
                .addSnapshotListener((newFriendSnapShot, error) -> {
                    if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                        List<DocumentSnapshot> documentChanges = newFriendSnapShot.getDocuments();

                        documentChanges.forEach(change -> {
                            users.put((String) change.getId(), (String) change.get(USER_NAME));
                            System.out.println("USERS: " + change.getId());
                        });
                        //System.out.println("AIZEDAFD: " + users.size());
                    }
                });
    }
    public void getFriendsFromDB(String userEmail){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(FRIEND_COLLECTION)
                .addSnapshotListener((newFriendSnapShot, error) -> {
                    if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                        ArrayList<String> newFriendList = new ArrayList<>();
                        List<DocumentSnapshot> documentChanges = newFriendSnapShot.getDocuments();
                        documentChanges.forEach(change -> {
                            friends.put((String) change.getId(), (String) change.get("status"));
                            System.out.println("FRIENDS: " + change.getId());
                        });
                    }
                });
    }
    public void getPendingFriendsFromDB(String userEmail){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(PENDING_COLLECTION)
                .addSnapshotListener((newFriendSnapShot, error) -> {
                    if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                        ArrayList<String> newFriendList = new ArrayList<>();
                        List<DocumentSnapshot> documentChanges = newFriendSnapShot.getDocuments();
                        documentChanges.forEach(change -> {
                            pendingFriends.put((String) change.getId(), (String) change.get("status"));
                            System.out.println("PENDING: " + change.getId());
                        });
                    }
                });
    }
    public void getPendingRequestsFromDB(String userEmail){
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(REQUEST_COLLECTION)
                .addSnapshotListener((newFriendSnapShot, error) -> {
                    if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                        ArrayList<String> newFriendList = new ArrayList<>();
                        List<DocumentSnapshot> documentChanges = newFriendSnapShot.getDocuments();
                        documentChanges.forEach(change -> {
                            pendingRequests.put((String) change.getId(), (String) change.get("status"));
                            System.out.println("REQUESTS: " + change.getId());
                        });
                    }
                });
    }

    // TODO: These methods updates friends information on the cloud
    public void addFriend(String userEmail, String friendEmail){
        // add "friend" to friendList of "user"
        HashMap<String, String> friend = new HashMap<>();
        friend.put("status", "friends");

        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(FRIEND_COLLECTION)
                .document(friendEmail)
                .set(friend);
    }
    public void addPendingFriend(String userEmail, String friendEmail){
        // add "friend" to pendingFriendList of "user"
        HashMap<String, String> friend = new HashMap<>();
        friend.put("status", "pending");

        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(PENDING_COLLECTION)
                .document(friendEmail)
                .set(friend);
    }
    public void addPendingRequest(String userEmail, String friendEmail){
        // add "friend" to requestList of "user"
        HashMap<String, String> friend = new HashMap<>();
        friend.put("status", "requested");

        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(REQUEST_COLLECTION)
                .document(friendEmail)
                .set(friend);
    }
    public void removeFriend(String userEmail, String friendEmail){
        // remove "friend" from friendList of "user"
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(FRIEND_COLLECTION)
                .document(friendEmail)
                .delete();
    }
    public void removePendingFriend(String userEmail, String friendEmail){
        // remove "friend" from pendingFriendList of "user"
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(PENDING_COLLECTION)
                .document(friendEmail)
                .delete();
    }
    public void removePendingRequest(String userEmail, String friendEmail){
        // remove "friend" from requestList of "user"
        db.collection(USER_COLLECTION)
                .document(userEmail)
                .collection(REQUEST_COLLECTION)
                .document(friendEmail)
                .delete();
    }

    // Getters for friendLists


    public HashMap<String, String> getPendingRequests() {
        return pendingRequests;
    }

    public HashMap<String, String> getPendingFriends() {
        return pendingFriends;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    // TODO: check if user exists
    public boolean userExists(String email) {
        return true;
    }

    /**
     * Gets the collection of chat messages between userID1 and userID2
     * @param userID1
     * @param userID2
     * @return the chats between user1 and user2: the first element is the chat stored under
     *         user1 and the second element is the chat stored under user2
     */
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
     * @param userID1 the user sending the message
     * @param userID2 the user receiving the message
     * @param text the message
     */
    public Task<DocumentReference> sendMessage(String userID1, String userID2, String text){
        //TODO: Add cloud function for timestamps (not here in java code)
        HashMap<String, String> message = new HashMap<>();
        message.put(FROM_KEY, userID1);
        message.put(TEXT_KEY, text);
        CollectionReference[] chats = getChats(userID1, userID2);
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
