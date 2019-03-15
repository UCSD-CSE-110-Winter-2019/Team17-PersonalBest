package com.example.team17_personalbest.Friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.team17_personalbest.Chat.ChatActivity;
import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.Step.PlannedWalk;
import com.example.team17_personalbest.Step.ShowHistoryActivity;
import com.example.team17_personalbest.Step.StepHistory;
import com.example.team17_personalbest.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ShowFriendsActivity extends AppCompatActivity {
    private String TAG = "ShowFriendsActivity";

    User user;
    FirebaseAdapter cloud;
    FriendManager friendManager;

    HashMap<String, LinearLayout> friendsOnUi = new HashMap<>();
    HashMap<String, LinearLayout> pendingFriendsOnUi = new HashMap<>();
    HashMap<String, LinearLayout> pendingRequestsOnUi = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

        loadUser();
        cloud = new FirebaseAdapter(FirebaseFirestore.getInstance());
        friendManager = new FriendManager(user, cloud);

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
                                return true;
                        }
                        return false;
                    }
                });
        navigation.setSelectedItemId(R.id.navigation_friends);

        final Button addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddFriendPrompt();
            }
        });

        // update friends every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        cloud.saveStepHistory(user.getUserEmail(), gson.toJson(user.getStepHistory()));
                        friendManager.updateFriends();
                        updateFriendsOnUI();
                        saveUser();
                    }
                });
            }
        }, 0, 1000);

    }


    /**
     * updates the friends, pending friends, and requests on the UI to make sure they match the
     * database
     */
    private void updateFriendsOnUI(){
        // update friend list
        ArrayList<String> friends = cloud.getFriends();
        for (String friend:
                friends) {
            if(!friendsOnUi.containsKey(friend)) {
                addFriendToList(friend);
                System.out.println("ADSFFSAFSA");
            }
        }
        Set friendsKey = friendsOnUi.keySet();
        for(Object friend : friendsKey){
            if(!friends.contains(friend)){
                removeFriendFromList((String)friend);
            }
        }
        // update pending friend list
        ArrayList<String> pendingFriends = cloud.getPendingFriends();
        for (String friend:
                pendingFriends) {
            if(!pendingFriendsOnUi.containsKey(friend)) {
                addPendingFriendToList(friend);
            }
        }
        Set pendingFriendsKey = pendingFriendsOnUi.keySet();
        for(Object friend : pendingFriendsKey){
            if(!pendingFriends.contains(friend)){
                removePendingFriendFromList((String)friend);
            }
        }
        // update friend requests
        ArrayList<String> requestedFriends = cloud.getPendingRequests();
        for (String friend:
                requestedFriends) {
            if(!pendingRequestsOnUi.containsKey(friend)) {
                addRequestedFriendToList(friend);
            }
        }
        Set requestedFriendsKey = pendingRequestsOnUi.keySet();
        for(Object friend : requestedFriendsKey){
            if(!requestedFriends.contains(friend)){
                removePendingRequestFromList((String)friend);
            }
        }
    }

    /**
     * Add an item (a friend) to the friend list
     * @param friendEmail
     */
    private void addFriendToList(String friendEmail){
        LinearLayout friendList = findViewById(R.id.friend_list);
        ViewGroup.LayoutParams friendListParams = friendList.getLayoutParams();
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.friend_list_item, friendList, false);
        ViewGroup.LayoutParams itemParams = item.getLayoutParams();

        Button hist_button = item.findViewById(R.id.friend_hist_button);
        ImageButton chat_button = item.findViewById(R.id.friend_chat_button);
        ImageButton remove_button = item.findViewById(R.id.friend_remove_button);
        hist_button.setText(cloud.getUserName(friendEmail));

        // show history
        hist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowFriendsActivity.this,ShowFriendHistActivity.class);
                intent.putExtra("friend_email", friendEmail);
                intent.putExtra("friend_name", cloud.getUserName(friendEmail));
                intent.putExtra("user_email", user.getUserEmail());
                startActivity(intent);
            }
        });
        // go to chat
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to chat
                launchChat(friendEmail);
            }
        });
        // remove friend
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendManager.removeFriend(friendEmail);
                removeFriendFromList(friendEmail);
            }
        });

        // add item to friends list
        System.out.println("paramsa:" + friendListParams.height);
        System.out.println("paramsa:" + itemParams.height);
        friendListParams.height = friendListParams.height + itemParams.height;
        System.out.println("paramsb:" + friendListParams.height);
        friendList.addView(item);
        friendList.setLayoutParams(friendListParams);

        // add item to friendsOnUi list
        friendsOnUi.put(friendEmail, item);

        Log.d(TAG,"Added " + friendEmail + " to friend UI list");
    }

    /**
     * remove a friend from the friend list
     * @param friendEmail
     */
    private void removeFriendFromList(String friendEmail){
        LinearLayout friendList = findViewById(R.id.friend_list);
        ViewGroup.LayoutParams friendListParams = friendList.getLayoutParams();
        LinearLayout item = friendsOnUi.get(friendEmail);
        ViewGroup.LayoutParams itemParams = item.getLayoutParams();

        friendListParams.height = friendListParams.height - itemParams.height;
        friendList.removeView(item);
        friendsOnUi.remove(friendEmail);

        Log.d(TAG,"Removed " + friendEmail + " from friend UI list");
    }

    /**
     * Add an item (a pending friend) to the pending friend list
     * @param  friendEmail the friend being added
     */
    private void addPendingFriendToList(String friendEmail){
        LinearLayout pendingFriendList = findViewById(R.id.pending_list);
        ViewGroup.LayoutParams friendListParams = pendingFriendList.getLayoutParams();
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.pending_list_item, pendingFriendList, false);
        ViewGroup.LayoutParams itemParams = item.getLayoutParams();

        Button hist_button = item.findViewById(R.id.pending_hist_button);
        ImageButton remove_button = item.findViewById(R.id.pending_decline_button);
        hist_button.setText(cloud.getUserName(friendEmail));

        // remove friend
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendManager.removePendingFriend(friendEmail);
                removePendingFriendFromList(friendEmail);
            }
        });

        friendListParams.height = friendListParams.height + itemParams.height;
        pendingFriendList.addView(item);
        pendingFriendList.setLayoutParams(friendListParams);

        pendingFriendsOnUi.put(friendEmail, item);

        Log.d(TAG,"Added " + friendEmail + " to pending friend UI list");
    }

    /**
     * remove a pending friend from the pending friend list
     * @param friendEmail
     */
    private void removePendingFriendFromList(String friendEmail){
        LinearLayout friendList = findViewById(R.id.pending_list);
        ViewGroup.LayoutParams friendListParams = friendList.getLayoutParams();
        LinearLayout item = pendingFriendsOnUi.get(friendEmail);
        ViewGroup.LayoutParams itemParams = item.getLayoutParams();

        friendListParams.height = friendListParams.height - itemParams.height;
        friendList.removeView(item);
        pendingFriendsOnUi.remove(friendEmail);

        Log.d(TAG,"Removed " + friendEmail + " from pending friend UI list");
    }

    /**
     * Add an item (a friend request) to the friend request list
     */
    private void addRequestedFriendToList(String friendEmail){
        LinearLayout friendList = findViewById(R.id.requested_list);
        ViewGroup.LayoutParams friendListParams = friendList.getLayoutParams();
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.requested_list_item, friendList, false);

        pendingRequestsOnUi.put(friendEmail, item);

        Button hist_button = item.findViewById(R.id.requested_hist_button);
        ImageButton accept_button = item.findViewById(R.id.requested_accept_button);
        ImageButton remove_button = item.findViewById(R.id.requested_decline_button);
        hist_button.setText(cloud.getUserName(friendEmail));
        ViewGroup.LayoutParams itemParams = item.getLayoutParams();

        // accept request
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendManager.acceptFriendRequest(friendEmail);
                removePendingRequestFromList(friendEmail);
            }
        });
        // remove friend
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendManager.denyFriendRequest(friendEmail);
                removePendingRequestFromList(friendEmail);
            }
        });

        friendListParams.height = friendListParams.height + itemParams.height;
        friendList.addView(item);
        friendList.setLayoutParams(friendListParams);

        Log.d(TAG,"Added " + friendEmail + " to friend requests UI list");
    }

    /**
     * remove a friend request from the friend request list
     * @param friendEmail
     */
    private void removePendingRequestFromList(String friendEmail){
        LinearLayout friendList = findViewById(R.id.requested_list);
        ViewGroup.LayoutParams friendListParams = friendList.getLayoutParams();
        LinearLayout item = pendingRequestsOnUi.get(friendEmail);
        ViewGroup.LayoutParams itemParams = item.getLayoutParams();

        friendListParams.height = friendListParams.height - itemParams.height;
        friendList.removeView(item);
        pendingRequestsOnUi.remove(friendEmail);

        Log.d(TAG,"Removed " + friendEmail + " from friend requests UI list");
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
     * Displays chat
     */
    private void launchChat(String friendEmail) {
        finish();
        Intent intent = new Intent(this, ChatActivity.class)
                .putExtra("from", user.getUserEmail())
                .putExtra("to", friendEmail);
        startActivity(intent);
    }

    /**
     * Creates a popup that lets the user enter friend's email
     */
    public void displayAddFriendPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Friend");

        // Set up user input
        final EditText dialogInput = new EditText(this);
        dialogInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogInput.setHint("Please enter your friend's email address");
        dialogInput.setTextSize(12);
        builder.setView(dialogInput);

        // Confirm button controller
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = dialogInput.getText().toString();
                if(!cloud.areFriends(email))
                    friendManager.addFriend(email);
            }
        });

        // Cancel button controller
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /**
     * Saves the user settings and history into sharedPreferences
     */
    public void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();

        String stepHist = gson.toJson(user.getStepHistory());
        String plannedWalk = gson.toJson(user.getCurrentWalk());
        String friends = gson.toJson(user.getFriends());
        String pendingFriends = gson.toJson(user.getPendingFriends());
        String pendingRequests = gson.toJson(user.getPendingRequests());
        edit.putInt("height", user.getHeight());
        edit.putInt("goal", user.getGoal());
        edit.putInt("daily_steps", user.getTotalDailySteps());
        edit.putBoolean("encouraged", user.isHasBeenEncouragedToday());
        edit.putBoolean("congratulated", user.isHasBeenCongratulatedToday());
        edit.putString("username", user.getUserName());
        edit.putString("useremail", user.getUserEmail());
        edit.putString("stepHist", stepHist);
        edit.putString("plannedWalk", plannedWalk);
        edit.putString("friends", friends);
        edit.putString("pendingFriends", pendingFriends);
        edit.putString("pendingRequests", pendingRequests);

        edit.apply();
    }

    /**
     * Loads the user settings and history from sharedPreferences
     */
    public void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Gson gson = new Gson();
        int height = sharedPreferences.getInt("height", 0);
        if (height == 0){
            user = null;
        } else {
            user= new User(height, Calendar.getInstance());
            StepHistory stepHistory = gson.fromJson(sharedPreferences.getString("stepHist", ""), StepHistory.class);
            PlannedWalk plannedWalk = gson.fromJson(sharedPreferences.getString("plannedWalk", ""), PlannedWalk.class);
            Day day = gson.fromJson(sharedPreferences.getString("day", ""), Day.class);
            HashMap<String, String> friends = gson.fromJson(sharedPreferences.getString("friends", ""), HashMap.class);
            HashMap<String, String>  pendingFriends = gson.fromJson(sharedPreferences.getString("friends", ""), HashMap.class);
            HashMap<String, String>  pendingRequests = gson.fromJson(sharedPreferences.getString("friends", ""), HashMap.class);
            user.setGoal(sharedPreferences.getInt("goal", 0));
            user.setTotalDailySteps(sharedPreferences.getInt("daily_steps", 0));
            user.setHasBeenEncouragedToday(sharedPreferences.getBoolean("encouraged", false));
            user.setHasBeenCongratulatedToday(sharedPreferences.getBoolean("congratulated",false));
            user.setUserName(sharedPreferences.getString("username", ""));
            user.setUserEmail(sharedPreferences.getString("useremail", ""));
            user.setStepHistory(stepHistory);
            user.setCurrentWalk(plannedWalk);

        }
    }
}
