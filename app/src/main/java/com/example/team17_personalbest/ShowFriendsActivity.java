package com.example.team17_personalbest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ShowFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);

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

    }


    /**
     * This is for testing, should have User as parameter
     */
    private void addFriendToList(String name){
        LinearLayout friendList = findViewById(R.id.friend_list);
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.friend_list_item, friendList, false);

        Button hist_button = item.findViewById(R.id.friend_hist_button);
        Button chat_button = item.findViewById(R.id.friend_chat_button);
        Button remove_button = item.findViewById(R.id.friend_remove_button);
        hist_button.setText(name);
        // show history
        hist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowFriendsActivity.this,ShowFriendHistActivity.class);
                startActivity(intent);
            }
        });
        friendList.addView(item);
    }

    /**
     * Add an item (a friend) to the friend list
     */
    private void addFriendToList(User user){
        LinearLayout friendList = findViewById(R.id.friend_list);
        LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.friend_list_item, friendList, false);

        Button hist_button = item.findViewById(R.id.friend_hist_button);
        Button chat_button = item.findViewById(R.id.friend_chat_button);
        Button remove_button = item.findViewById(R.id.friend_remove_button);
        hist_button.setText("user.getname");
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

            }
        });
        // remove friend
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
