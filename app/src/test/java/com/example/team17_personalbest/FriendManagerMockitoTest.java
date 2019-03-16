package com.example.team17_personalbest;

import android.content.Context;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Friends.FriendManager;
import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;


@RunWith(RobolectricTestRunner.class)

public class FriendManagerMockitoTest {

    private FirebaseAdapter db;
    private FriendManager friendManager;
    private String useremail = "user";
    private String friendemail = "friend";


    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
        // set up mock database
        db = mock(FirebaseAdapter.class);
        User user = new User(useremail);
        friendManager = new FriendManager(user, db);
    }

    @Test
    public void FriendRequestSent() {
        friendManager.addFriend(friendemail);
        verify(db, times(1)).addPendingFriend(useremail, friendemail);
        verify(db, times(1)).addPendingRequest(friendemail, useremail);
    }

    @Test
    public void FriendRequestAccepted() {
        friendManager.acceptFriendRequest(friendemail);
        verify(db, times(1)).removePendingRequest(useremail, friendemail);
        verify(db, times(1)).removePendingFriend(friendemail, useremail);
        verify(db, times(1)).addFriend(useremail, friendemail);
        verify(db, times(1)).addFriend(friendemail, useremail);
    }

    @Test
    public void FriendRequestDenied() {
        friendManager.denyFriendRequest(friendemail);
        verify(db, times(1)).removePendingRequest(useremail, friendemail);
        verify(db, times(1)).removePendingFriend(friendemail, useremail);
    }

    @Test
    public void FriendRemoved() {
        friendManager.removeFriend(friendemail);
        verify(db, times(1)).removeFriend(useremail, friendemail);
        verify(db, times(1)).removeFriend(friendemail, useremail);
    }

    @Test
    public void PendingFriendRemoved() {
        friendManager.removePendingFriend(friendemail);
        verify(db, times(1)).removePendingFriend(useremail, friendemail);
        verify(db, times(1)).removePendingRequest(friendemail, useremail);
    }

    @Test
    public void FriendUpdated() {
        friendManager.updateFriends();
        verify(db, times(1)).getUsersFromDB();
        verify(db, times(1)).getFriendsFromDB(useremail);
        verify(db, times(1)).getPendingFriendsFromDB(useremail);
        verify(db, times(1)).getPendingRequestsFromDB(useremail);
    }

}
