package com.example.team17_personalbest.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * Class that handles interactions with Firestore database
 */
public class FirebaseAdapter {

    FirebaseFirestore db;

    private String TEST_COLLECTION = "test";

    private String USER_COLLECTION = "users";
    private String USER_ID = "u_id";
    private String USER_NAME = "u_name";
    private String USER_EMAIL = "u_email";

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
}
