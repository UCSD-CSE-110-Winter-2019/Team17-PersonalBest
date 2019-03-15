package com.example.team17_personalbest.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Notifications;
import com.example.team17_personalbest.Friends.ShowFriendsActivity;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.ShowHistoryActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    String TAG = ChatActivity.class.getSimpleName();

    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    public MyCollectionReference chat;
    public String from;
    public String to;
    public boolean test = false;
    public FirebaseAdapter firebase;
    public ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        scrollView = findViewById(R.id.scroll_view);

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        setupChat();
        setupMessaging();
        subscribeToNotificationsTopic();
        setupNavigation();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        EditText messageView = findViewById(R.id.text_message);

        firebase.sendMessage(from, to, messageView.getText().toString())
                .addOnSuccessListener(result -> {
                    messageView.setText("");
                }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }
	

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

    private void subscribeToNotificationsTopic() {
        //TODO: Add firebase notifications cloud function (not in java code)
		NotificationFactory notificationFactory = NotificationFactory.getInstance();
        String key = getIntent().getStringExtra(NOTIFICATION_SERVICE_EXTRA);
        INotification inote = notificationFactory.getOrDefault(key, FirebaseMessagingAdapter::getInstance);
        inote.subscribeToTopic(DOCUMENT_KEY, task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
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
