package com.example.team17_personalbest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.team17_personalbest.fitness.FitnessService;
import com.example.team17_personalbest.fitness.FitnessServiceFactory;
import com.example.team17_personalbest.fitness.GoogleFitAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;
    private User user;
    private FitnessService fitnessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // navigation bar controller
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
            // Switching between home screen and history
            new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_dashboard:
                        mTextMessage.setText(R.string.title_dashboard);
                        launchHistory();
                        return true;
                }
                return false;
            }
        });

        // start walk button controller
        Button startWalking = findViewById(R.id.start_walk);
        startWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: start recording walk
            }
        });

        // Manage steps and goal displays with HomeDisplayManager
        TextView currSteps = findViewById(R.id.curr_steps);
        TextView dailyGoal = findViewById(R.id.daily_goal);
        HomeDisplayManager homeDisplayManager = new HomeDisplayManager(currSteps, dailyGoal);
        user = new User(100);
        user.addObserver(homeDisplayManager);

        ProgressService progressService = new ProgressService(MainActivity.this);
        user.addObserver(progressService);
        //TODO: reset progress in progressService to 0 at midnight

        // Create fitness service
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity activity) {
                return new GoogleFitAdapter(activity, user);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fitnessService.updateStepCount();
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If authentication was required during google fit setup, this will
        // be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    // Display step history
    public void launchHistory() {
        Intent intent = new Intent(this, StepHistory.class);
        startActivity(intent);
    }

    // setter for tests
    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

}
