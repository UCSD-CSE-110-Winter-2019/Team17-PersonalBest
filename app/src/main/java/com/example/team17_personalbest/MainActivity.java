/**
 * Uses GSON Library with Apache License
 */

package com.example.team17_personalbest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

        // Manage steps and goal displays with HomeDisplayManager
        TextView currSteps = findViewById(R.id.curr_steps);
        TextView dailyGoal = findViewById(R.id.daily_goal);
        TextView walkSteps = findViewById(R.id.walk_steps);
        TextView walkDistance = findViewById(R.id.walk_distance);
        TextView walkSpeed = findViewById(R.id.walk_speed);
        TextView walkClock = findViewById(R.id.clock);
        Button walkButton = findViewById(R.id.start_walk);
        final HomeDisplayManager homeDisplayManager = new HomeDisplayManager(currSteps, dailyGoal,
                walkSteps, walkDistance, walkSpeed, walkClock, walkButton, this);

        // Create user and add observers
        loadUser();
        if (user == null) {
            user = new User(70, Calendar.getInstance());
            displayHeightPrompt();
        }
        user.addObserver(homeDisplayManager);

        // Manage encouragements with ProgressService
        ProgressService progressService = new ProgressService(MainActivity.this);
        user.addObserver(progressService);

        // Create fitness service
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity activity) {
                return new GoogleFitAdapter(activity, user);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        // start walk button controller
        walkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PlannedWalk currWalk = user.getCurrentWalk();
                if(currWalk == null) {
                    user.startPlannedWalk(Calendar.getInstance());
                }else{
                    user.endPlannedWalk();
                }
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

        // update normal and planned walk steps every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fitnessService.updateStepCount();
                        saveUser();
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

    /**
     * Creates a popup that lets the user set a new goal
     */
    public void displayNewGoalPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Goal");

        // Set up user input
        final EditText dialogInput = new EditText(this);
        dialogInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogInput.setHint("Enter number of steps / Confirm the default 5000 steps");
        dialogInput.setTextSize(10);
        builder.setView(dialogInput);

        // Confirm button controller
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newGoal;
                if (dialogInput.getText().toString().equals("")){
                    newGoal = 5000;
                } else {
                    newGoal = Integer.parseInt(dialogInput.getText().toString());
                }
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

    /**
     * Creates a popup for setting height
     */
    public void displayHeightPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Height");

        // Set up user input
        final EditText dialogInput = new EditText(this);
        dialogInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogInput.setHint("Enter height in inches / Confirm the default 70 inches");
        dialogInput.setTextSize(10);
        builder.setView(dialogInput);

        // Confirm button controller
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int height;
                if (dialogInput.getText().toString().equals("")){
                    height = 70;
                } else {
                    height = Integer.parseInt(dialogInput.getText().toString());
                }
                user.setHeight(height);
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
        String userjson = gson.toJson(user);
        edit.putString("user", userjson);
        edit.apply();
    }

    /**
     * Loads the user settings and history from sharedPreferences
     */
    public void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Gson gson = new Gson();
        String userjson = sharedPreferences.getString("user", "");
        if (userjson.equals("")){
            user = null;
        } else {
            user = new User(gson.fromJson(userjson, User.class));
        }
    }

}
