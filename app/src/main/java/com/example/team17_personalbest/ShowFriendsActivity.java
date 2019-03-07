package com.example.team17_personalbest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
                                //mTextMessage.setText(R.string.title_home);
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
                addFriendToList();
            }
        });

    }

    private void addFriendToList(){
        LinearLayout friendList = findViewById(R.id.friend_list);
        getLayoutInflater().inflate(R.layout.friend_list_item,friendList);
    }


    /**
     * Displays step history
     */
    private void launchHistory() {
        finish();
        Intent intent = new Intent(this, ShowHistoryActivity.class);
        startActivity(intent);
    }
}
