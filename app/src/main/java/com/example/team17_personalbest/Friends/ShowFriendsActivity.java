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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.team17_personalbest.Firestore.CloudStroage;
import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.Step.PlannedWalk;
import com.example.team17_personalbest.Step.ShowHistoryActivity;
import com.example.team17_personalbest.Step.StepHistory;
import com.example.team17_personalbest.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ShowFriendsActivity extends AppCompatActivity {

    User user;
    FirebaseAdapter cloud;
    FriendManager friendManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

        loadUser();
        cloud = new FirebaseAdapter(FirebaseFirestore.getInstance());
        friendManager = new FriendManager(user, cloud);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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

        // update normal and planned walk steps every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveUser();
                    }
                });
            }
        }, 0, 1000);

    }

    private void displayFriendsList(HashMap<String, String> list){
        Set keySet = list.keySet();
        for (Object key :
             keySet) {
            addFriendToList((String) key);
        }
    }


//    /**
//     * This is for testing, should have User as parameter
//     */
//    private void addFriendToList(String name){
//        LinearLayout friendList = findViewById(R.id.friend_list);
//        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.friend_list_item, friendList, false);
//
//        Button hist_button = item.findViewById(R.id.friend_hist_button);
//        Button chat_button = item.findViewById(R.id.friend_chat_button);
//        Button remove_button = item.findViewById(R.id.friend_remove_button);
//        hist_button.setText(name);
//        // show history
//        hist_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShowFriendsActivity.this,ShowFriendHistActivity.class);
//                startActivity(intent);
//            }
//        });
//        friendList.addView(item);
//    }

    /**
     * Add an item (a friend) to the friend list
     */
    private void addFriendToList(String friendEmail){
        LinearLayout friendList = findViewById(R.id.friend_list);
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.friend_list_item, friendList, false);

        Button hist_button = item.findViewById(R.id.friend_hist_button);
        ImageButton chat_button = item.findViewById(R.id.friend_chat_button);
        ImageButton remove_button = item.findViewById(R.id.friend_remove_button);
        hist_button.setText(cloud.getUserName(friendEmail));
        // show history
        hist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowFriendsActivity.this,ShowFriendHistActivity.class);
                startActivity(intent);
            }
        });
        // go to chat
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to chat
            }
        });
        // remove friend
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendManager.removeFriend(friendEmail);
            }
        });
        friendList.addView(item);
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
                addFriendToList(email);
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
