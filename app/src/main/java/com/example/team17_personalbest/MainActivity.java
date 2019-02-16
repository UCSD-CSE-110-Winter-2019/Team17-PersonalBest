package com.example.team17_personalbest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String m_Text;
    MainActivity m = this;

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


        // Manage steps and goal displays with HomeDisplayManager
        TextView currSteps = findViewById(R.id.curr_steps);
        TextView dailyGoal = findViewById(R.id.daily_goal);
        TextView walkSteps = findViewById(R.id.walk_steps);
        TextView walkDistance = findViewById(R.id.walk_distance);
        TextView walkSpeed = findViewById(R.id.walk_speed);
        Button startWalk = findViewById(R.id.start_walk);
        Button endWalk = findViewById(R.id.end_walk);
        final HomeDisplayManager homeDisplayManager = new HomeDisplayManager(currSteps, dailyGoal,
                walkSteps, walkDistance, walkSpeed, startWalk, endWalk);
        user = new User(100);
        user.addObserver(homeDisplayManager);

        // Manage encouragements with ProgressService
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


        // start walk button controller
        startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: start recording walk
                user.startPlannedWalk();
                homeDisplayManager.startWalk();
            }
        });

        // end walk button controller
        endWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: start recording walk
                user.endPlannedWalk();
                homeDisplayManager.endWalk();
            }
        });

        // set goal button controller
        Button setGoal = findViewById(R.id.create_goal);
        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewGoalPrompt();
            }
        });
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

    /**
     * Creates a popup that lets the user set a new goal
     */
    public void displayNewGoalPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Goal");

        // Set up user input
        final EditText userInput = new EditText(this);
        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(userInput);

        // Set goal button controller
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newGoal = Integer.parseInt(userInput.getText().toString());
                user.setGoal(newGoal);
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
