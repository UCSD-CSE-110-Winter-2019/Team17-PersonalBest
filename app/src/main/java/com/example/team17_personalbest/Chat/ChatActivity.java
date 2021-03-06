package com.example.team17_personalbest.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Friends.ShowFriendsActivity;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.ShowHistoryActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    String TAG = ChatActivity.class.getSimpleName();

    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    public MyCollectionReference chat;
    public String from;
    public String to;
    public boolean test = false;
    public MyFirebaseMessaging firebaseMessaging;
    public FirebaseAdapter firebase;
    public ScrollView scrollView;
    public Button sendButton;
    public EditText messageView;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        scrollView = findViewById(R.id.scroll_view);

        from = getIntent().getStringExtra("fromuser");
        to = getIntent().getStringExtra("to");

        setupChat();
        setupMessaging();
        setupNavigation();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());
        sendButton = findViewById(R.id.btn_send);
        messageView = findViewById(R.id.text_message);
        textView = findViewById(R.id.chat);
    }

    /**
     * Send a message to firebase
     */
    private void sendMessage() {
        EditText messageView = findViewById(R.id.text_message);

        firebase.sendMessage(from, to, messageView.getText().toString())
                .addOnSuccessListener(result -> {
                    messageView.setText("");
                }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    /**
     * Set up function for updating the message history on screen
     */
    private void initMessageUpdateListener() {
        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                        documentChanges.forEach(change -> {
                            QueryDocumentSnapshot document = change.getDocument();
                            sb.append(document.get(FROM_KEY));
                            sb.append(":\n");
                            sb.append(document.get(TEXT_KEY));
                            sb.append("\n");
                            sb.append("---\n");
                        });


                        TextView chatView = findViewById(R.id.chat);
                        chatView.append(sb.toString());
                    }
                });
    }

    public void setupChat(){
        if(chat == null && firebase == null) {
            firebase = new FirebaseAdapter(FirebaseFirestore.getInstance());
            chat = new CollectionReferenceAdapter(firebase.getChats(from, to)[0]);
            firebase.getUsersFromDB();
            initMessageUpdateListener();
        }
    }

    public void setupMessaging(){
        if (firebaseMessaging == null){
            firebaseMessaging = new FirebaseMessagingAdapter(FirebaseMessaging.getInstance());
        }
    }

    /**
     * setup navigation menu
     */
    public void setupNavigation(){
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                // Switching between home screen and history
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                finish();
                                return true;
                            case R.id.navigation_history:
                                launchHistory();
                                return true;
                            case R.id.navigation_friends:
                                launchFriends();
                                return true;
                        }
                        return false;
                    }
                });
    }

    /**
     * Displays step history
     */
    private void launchHistory() {
        finish();
        Intent intent = new Intent(this, ShowHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * Displays friend list
     */
    private void launchFriends() {
        finish();
        Intent intent = new Intent(this, ShowFriendsActivity.class);
        startActivity(intent);
    }
}
