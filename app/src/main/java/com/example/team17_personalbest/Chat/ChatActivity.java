package com.example.team17_personalbest.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Notifications;
import com.example.team17_personalbest.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
	public static final String NOTIFICATION_SERVICE_EXTRA = "NOTIFICATION_SERVICE";
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
    public MyFirebaseMessaging firebaseMessaging;
    public EditText nameView;
    public FirebaseAdapter firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //TODO: Get usernames
        from = "user1";
        to = "user2";

        setupChat();
        setupMessaging();
        subscribeToNotificationsTopic();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());

        nameView = findViewById((R.id.user_name));
        nameView.setText(from);
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
		String key = getIntent().getStringExtra(NOTIFICATION_SERVICE_EXTRA);
		INotification in = NotificationFactory.getInstance().getOrDefault(key, FirebaseMessagingAdapter::getInstance)
        in.subscribeToNotificationsTopic(DOCUMENT_KEY)
                .addOnCompleteListener(task -> {
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
            initMessageUpdateListener();
        }
    }

    public void setupMessaging(){
        if (firebaseMessaging == null){
            firebaseMessaging = new FirebaseMessagingAdapter(FirebaseMessaging.getInstance());
        }
    }
}
