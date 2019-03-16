package com.example.team17_personalbest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.team17_personalbest.Chat.ChatActivity;
import com.example.team17_personalbest.Chat.MyCollectionReference;
import com.example.team17_personalbest.Chat.MyFirebaseMessaging;
import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Friends.ShowFriendHistActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class ChatActivityTest {

    private FirebaseAdapter db;
    private MyFirebaseMessaging fm;
    private MyCollectionReference chat;
    private Task<DocumentReference> test1 = new Task<DocumentReference>() {
        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public DocumentReference getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> DocumentReference getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return this;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Before
    public void setUp() {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
        db = mock(FirebaseAdapter.class);
        fm = mock(MyFirebaseMessaging.class);
        chat = mock(MyCollectionReference.class);
        when(db.sendMessage(anyString(), anyString(), anyString())).thenReturn(test1);
    }

    @Test
    public void testStringExtras(){
        Intent intent = new Intent(RuntimeEnvironment.application, ChatActivity.class);
        intent.putExtra("fromuser", "user");
        intent.putExtra("to", "friend");
        ActivityController<ChatActivity> controller = Robolectric.buildActivity(ChatActivity.class, intent);
        ChatActivity activity = controller.get();
        activity.firebase = db;
        activity.firebaseMessaging = fm;
        activity.chat = chat;
        activity = controller.create().get();
        assertEquals("user", activity.from);
        assertEquals("friend", activity.to);

        activity.messageView.setText("mymessage");
        activity.sendButton.performClick();

        verify(db).sendMessage(activity.from, activity.to, "mymessage");
    }

    @Test
    public void testSendMessage() {
        Intent intent = new Intent(RuntimeEnvironment.application, ChatActivity.class);
        intent.putExtra("fromuser", "user");
        intent.putExtra("to", "friend");
        ActivityController<ChatActivity> controller = Robolectric.buildActivity(ChatActivity.class, intent);
        ChatActivity activity = controller.get();
        activity.firebase = db;
        activity.firebaseMessaging = fm;
        activity.chat = chat;
        activity = controller.create().get();

        activity.messageView.setText("mymessage");
        assertEquals(activity.messageView.getText().toString(), "mymessage");
        activity.sendButton.performClick();
        verify(db).sendMessage(activity.from, activity.to, "mymessage");
    }
}
