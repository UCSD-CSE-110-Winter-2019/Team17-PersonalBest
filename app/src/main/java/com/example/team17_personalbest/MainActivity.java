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

import com.example.team17_personalbest.Friends.ShowFriendsActivity;
import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.Step.PlannedWalk;
import com.example.team17_personalbest.Step.ProgressService;
import com.example.team17_personalbest.Step.ShowHistoryActivity;
import com.example.team17_personalbest.Step.StepHistory;
import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.GoogleFit.FitnessService;
import com.example.team17_personalbest.GoogleFit.FitnessServiceFactory;
import com.example.team17_personalbest.GoogleFit.TestFitnessService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;
    private User user;
    private FitnessService fitnessService;

    private GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;

    private FirebaseAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null){
            signIn();
        }

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
                                //mTextMessage.setText(R.string.title_home);
                                return true;
                            case R.id.navigation_history:
                                //mTextMessage.setText(R.string.title_dashboard);
                                launchHistory();
                                return true;
                            case R.id.navigation_friends:
                                launchFriends();
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
        user.getStepHistory().printHist();

        // Manage encouragements with ProgressService
        ProgressService progressService = new ProgressService(MainActivity.this, user);
        user.addObserver(progressService);

        // Create fitness service
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity activity) {
                return new TestFitnessService(activity, user);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        //Create database
        FirebaseApp.initializeApp(this);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        db = new FirebaseAdapter(firebaseFirestore);

        // start walk button controller
        walkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PlannedWalk currWalk = user.getCurrentWalk();
                if(currWalk == null) {
                    user.startPlannedWalk(fitnessService.getTime());
                }else{
                    user.endPlannedWalk();
                }
            }
        });a

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

        // add steps testing button controllers
        Button addSteps = findViewById(R.id.add_steps_button);
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.addSteps(500);
            }
        });

        // add time testing button controllers
        Button addTime = findViewById(R.id.add_time_button);
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddTimePrompt();
            }
        });
    }

    // google sign in
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If authentication was required during google fit setup, this will
        // be called after the user authenticates
        if (resultCode == Activity.RESULT_OK || resultCode == RC_SIGN_IN) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    /**
     * if sign in is successfull, add user to firebase database
     * @param completedTask
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(db != null){
                db.addUser(account.getId(), account.getDisplayName(), account.getEmail());
                user.setUserEmail(account.getEmail());
                user.setUserName(account.getDisplayName());
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * Displays step history
     */
    private void launchHistory() {
        Intent intent = new Intent(this, ShowHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * Displays friend list
     */
    private void launchFriends() {
        Intent intent = new Intent(this, ShowFriendsActivity.class);
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

        String stepHist = gson.toJson(user.getStepHistory());
        String plannedWalk = gson.toJson(user.getCurrentWalk());
        String day = gson.toJson(user.getCurrentDayStats());
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
        edit.putString("day", day);
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
            user.setCurrentDayStats(day);

        }
    }

    /**
     * Creates a popup that lets the user add time
     */
    public void displayAddTimePrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add time");

        // Set up user input
        final EditText dialogInput = new EditText(this);
        dialogInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogInput.setHint("Enter number of milliseconds");
        dialogInput.setTextSize(10);
        builder.setView(dialogInput);

        // Confirm button controller
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int millis;
                if (dialogInput.getText().toString().equals("")){
                    millis = 0;
                } else {
                    millis = Integer.parseInt(dialogInput.getText().toString());
                }
                fitnessService.addTime(millis);
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
