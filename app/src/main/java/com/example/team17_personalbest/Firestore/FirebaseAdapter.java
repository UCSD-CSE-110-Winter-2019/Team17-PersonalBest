package com.example.team17_personalbest.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private String TEST_COLLECTION = "test";
    private String CHAT_COLLECTION = "chats";

    private String USER_ID = "u_id";
    private String USER_NAME = "u_name";
    private String USER_EMAIL = "u_email";

    private String USER_COLLECTION = "users";
    private String MESSAGES_COLLECTION = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";

    private HashMap<String, Pair<String,String>> users;     // Users in database

    /**
     * Constructor that initializes users and database
     * @param firebaseFirestore
     */
    public FirebaseAdapter(FirebaseFirestore firebaseFirestore) {
        db = firebaseFirestore;
        users = new HashMap<>();
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
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        users.put(uid, new Pair<>(name, email));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
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

    /**
     * Used for adding values to test collection
     * @param test1
     * @param test2
     * @param test3
     */
    public void addTest(String test1, String test2, int test3){
        HashMap<String, Object> test = new HashMap<>();

        test.put("test1", test1);
        test.put("test2", test2);
        test.put("test3", test3);

        db.collection(TEST_COLLECTION)
                .add(test)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
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
