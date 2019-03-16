package com.example.team17_personalbest;

import android.content.Context;
import android.content.Intent;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Friends.ShowFriendHistActivity;
import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class ShowFriendHistoryTest {

    private FirebaseAdapter db;

    @Before
    public void setUp() {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
        db = mock(FirebaseAdapter.class);
    }

    @Test
    public void testStringExtras(){
        Intent intent = new Intent(RuntimeEnvironment.application, ShowFriendHistActivity.class);
        intent.putExtra("friend_name", "Friend");
        intent.putExtra("friend_email", "Email");
        intent.putExtra("user_email", "User");
        ActivityController<ShowFriendHistActivity> controller = Robolectric.buildActivity(ShowFriendHistActivity.class, intent);
        ShowFriendHistActivity activity = controller.get();
        activity.setFirebaseAdapter(db);
        activity = controller.create().get();
        assertEquals("Friend", activity.getFriendName());
        assertEquals("Email", activity.getFriendEmail());
        assertEquals("User", activity.getUserEmail());

    }
}
