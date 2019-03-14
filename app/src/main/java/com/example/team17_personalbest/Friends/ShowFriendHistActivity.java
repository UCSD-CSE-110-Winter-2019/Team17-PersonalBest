package com.example.team17_personalbest.Friends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.StepHistory;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowFriendHistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend_hist);

        FirebaseAdapter cloud = new FirebaseAdapter(FirebaseFirestore.getInstance());
        StepHistory friendHistory = new StepHistory();
        String friendEmail = getIntent().getStringExtra("email");

        cloud.getStepHistory(friendEmail, friendHistory);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
