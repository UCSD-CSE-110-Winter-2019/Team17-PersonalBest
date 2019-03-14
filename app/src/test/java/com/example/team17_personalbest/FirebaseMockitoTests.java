package com.example.team17_personalbest;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.Friends.FriendManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class FirebaseMockitoTests {

    @Mock
    CollectionReference userCollection;

    // Collections and Documents for current user
    @Mock
    DocumentReference userDocument;
    @Mock
    CollectionReference user_Friends;
    @Mock
    CollectionReference user_PendingFriends;
    @Mock
    CollectionReference user_RequestedFriends;
    @Mock
    DocumentReference user_FriendDocument;
    @Mock
    DocumentReference user_PendingFriendDocument;
    @Mock
    DocumentReference user_RequestedFriendDocument;

    // Collections and Documents for friend
    @Mock
    DocumentReference friendDocument;
    @Mock
    CollectionReference friend_Friends;
    @Mock
    CollectionReference friend_PendingFriends;
    @Mock
    CollectionReference friend_RequestedFriends;
    @Mock
    DocumentReference friend_FriendDocument;
    @Mock
    DocumentReference friend_PendingFriendDocument;
    @Mock
    DocumentReference friend_RequestedFriendDocument;

    @Mock
    Task<QuerySnapshot> task;
    @Mock
    DocumentSnapshot userSnapshot;

    @Mock
    FirebaseFirestore firebaseFirestore;

    @Mock
    User user;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FriendManager friendManager;
    private FirebaseAdapter firebaseAdapter;

    private String USER_TEST_EMAIL = "test1@ucsd.edu";
    private String FRIEND_EMAIL = "test2@ucsd.edu";

    @Before
    public void setUp(){
        firebaseAdapter = new FirebaseAdapter(firebaseFirestore);
        friendManager = new FriendManager(user, firebaseAdapter);

        Mockito.when(user.getUserEmail()).thenReturn(USER_TEST_EMAIL);
        Mockito.when(firebaseFirestore.collection("users")).thenReturn(userCollection);
    }

    @Test
    public void test_addFriend(){
        // User collection and document methods
        Mockito.when(userCollection.document(USER_TEST_EMAIL)).thenReturn(userDocument);
        Mockito.when(userDocument.collection("pending")).thenReturn(user_PendingFriends);
        Mockito.when(user_PendingFriends.document(FRIEND_EMAIL)).thenReturn(user_PendingFriendDocument);

        // Friend collection and document methods
        Mockito.when(userCollection.document(FRIEND_EMAIL)).thenReturn(friendDocument);
        Mockito.when(friendDocument.collection("requests")).thenReturn(friend_RequestedFriends);
        Mockito.when(friend_RequestedFriends.document(USER_TEST_EMAIL)).thenReturn(friend_RequestedFriendDocument);

        // Argument captors for values being set
        ArgumentCaptor<HashMap> user_ArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<HashMap> friend_ArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);

        // Method being tested
        friendManager.addFriend(FRIEND_EMAIL);

        // Verify right method is called
        Mockito.verify(user_PendingFriendDocument, Mockito.times(1)).set(user_ArgumentCaptor.capture());
        Mockito.verify(friend_RequestedFriendDocument, Mockito.times(1)).set(friend_ArgumentCaptor.capture());

        // Verify right arguments method is called with
        HashMap user_CaptorValue = user_ArgumentCaptor.getValue();
        Assert.assertEquals("pending", user_CaptorValue.get("status"));

        HashMap friend_CaptorValue = friend_ArgumentCaptor.getValue();
        Assert.assertEquals("requested", friend_CaptorValue.get("status"));
    }

    @Test
    public void test_removeFriend(){
        // User collection and document methods
        Mockito.when(userCollection.document(USER_TEST_EMAIL)).thenReturn(userDocument);
        Mockito.when(userDocument.collection("friends")).thenReturn(user_Friends);
        Mockito.when(user_Friends.document(FRIEND_EMAIL)).thenReturn(user_FriendDocument);

        // Friend collection and document methods
        Mockito.when(userCollection.document(FRIEND_EMAIL)).thenReturn(friendDocument);
        Mockito.when(friendDocument.collection("friends")).thenReturn(friend_Friends);
        Mockito.when(friend_Friends.document(USER_TEST_EMAIL)).thenReturn(friend_FriendDocument);

        // Method being tested
        friendManager.removeFriend(FRIEND_EMAIL);

        // Verify right methods are called
        Mockito.verify(user_FriendDocument, Mockito.times(1)).delete();
        Mockito.verify(friend_FriendDocument, Mockito.times(1)).delete();
    }

    @Test
    public void test_removePendingFriend(){
        // User collection and document methods
        Mockito.when(userCollection.document(USER_TEST_EMAIL)).thenReturn(userDocument);
        Mockito.when(userDocument.collection("pending")).thenReturn(user_PendingFriends);
        Mockito.when(user_PendingFriends.document(FRIEND_EMAIL)).thenReturn(user_PendingFriendDocument);

        // Friend collection and document methods
        Mockito.when(userCollection.document(FRIEND_EMAIL)).thenReturn(friendDocument);
        Mockito.when(friendDocument.collection("requests")).thenReturn(friend_RequestedFriends);
        Mockito.when(friend_RequestedFriends.document(USER_TEST_EMAIL)).thenReturn(friend_RequestedFriendDocument);

        // Method being tested
        friendManager.removePendingFriend(FRIEND_EMAIL);

        // Verify right methods are called
        Mockito.verify(user_PendingFriendDocument, Mockito.times(1)).delete();
        Mockito.verify(friend_RequestedFriendDocument, Mockito.times(1)).delete();
    }

    @Test
    public void test_acceptFriendRequest(){
        // User collection and document methods
        Mockito.when(userCollection.document(USER_TEST_EMAIL)).thenReturn(userDocument);
        Mockito.when(userDocument.collection("friends")).thenReturn(user_Friends);
        Mockito.when(user_Friends.document(FRIEND_EMAIL)).thenReturn(user_FriendDocument);
        Mockito.when(userDocument.collection("requests")).thenReturn(user_RequestedFriends);
        Mockito.when(user_RequestedFriends.document(FRIEND_EMAIL)).thenReturn(user_RequestedFriendDocument);

        // Friend collection and document methods
        Mockito.when(userCollection.document(FRIEND_EMAIL)).thenReturn(friendDocument);
        Mockito.when(friendDocument.collection("friends")).thenReturn(friend_Friends);
        Mockito.when(friend_Friends.document(USER_TEST_EMAIL)).thenReturn(friend_FriendDocument);
        Mockito.when(friendDocument.collection("pending")).thenReturn(friend_PendingFriends);
        Mockito.when(friend_PendingFriends.document(USER_TEST_EMAIL)).thenReturn(friend_PendingFriendDocument);

        // Argument captors for values being set
        ArgumentCaptor<HashMap> user_ArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<HashMap> friend_ArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);

        // Method being tested
        friendManager.acceptFriendRequest(FRIEND_EMAIL);

        // Verify right method is called
        Mockito.verify(user_RequestedFriendDocument, Mockito.times(1)).delete();
        Mockito.verify(user_FriendDocument, Mockito.times(1)).set(user_ArgumentCaptor.capture());

        Mockito.verify(friend_PendingFriendDocument, Mockito.times(1)).delete();
        Mockito.verify(friend_FriendDocument, Mockito.times(1)).set(friend_ArgumentCaptor.capture());

        // Verify right arguments method is called with
        HashMap user_CaptorValue = user_ArgumentCaptor.getValue();
        Assert.assertEquals("friends", user_CaptorValue.get("status"));

        HashMap friend_CaptorValue = friend_ArgumentCaptor.getValue();
        Assert.assertEquals("friends", friend_CaptorValue.get("status"));
    }

    @Test
    public void test_denyFriendRequest(){
        // User collection and document methods
        Mockito.when(userCollection.document(USER_TEST_EMAIL)).thenReturn(userDocument);
        Mockito.when(userDocument.collection("requests")).thenReturn(user_RequestedFriends);
        Mockito.when(user_RequestedFriends.document(FRIEND_EMAIL)).thenReturn(user_RequestedFriendDocument);

        // Friend collection and document methods
        Mockito.when(userCollection.document(FRIEND_EMAIL)).thenReturn(friendDocument);
        Mockito.when(friendDocument.collection("pending")).thenReturn(friend_PendingFriends);
        Mockito.when(friend_PendingFriends.document(USER_TEST_EMAIL)).thenReturn(friend_PendingFriendDocument);

        // Actual method being tested
        friendManager.denyFriendRequest(FRIEND_EMAIL);

        // Verify right method is called
        Mockito.verify(user_RequestedFriendDocument, Mockito.times(1)).delete();
        Mockito.verify(friend_PendingFriendDocument, Mockito.times(1)).delete();
    }

    @Test
    public void test_getFriends(){
        // User collection and document methods
        Mockito.when(userCollection.document(USER_TEST_EMAIL)).thenReturn(userDocument);
        Mockito.when(userDocument.collection("friends")).thenReturn(user_Friends);
        Mockito.when(userDocument.collection("pending")).thenReturn(user_PendingFriends);
        Mockito.when(userDocument.collection("requests")).thenReturn(user_RequestedFriends);
        Mockito.when(user_Friends.get()).thenReturn(task);
        Mockito.when(user_PendingFriends.get()).thenReturn(task);
        Mockito.when(user_RequestedFriends.get()).thenReturn(task);

        firebaseAdapter.getFriendsFromDB(user.getUserEmail());
        firebaseAdapter.getPendingFriendsFromDB(user.getUserEmail());
        firebaseAdapter.getPendingRequestsFromDB(user.getUserEmail());

        Mockito.verify(user_Friends, Mockito.times(1)).get();
        Mockito.verify(user_PendingFriends, Mockito.times(1)).get();
        Mockito.verify(user_RequestedFriends, Mockito.times(1)).get();
    }

}
