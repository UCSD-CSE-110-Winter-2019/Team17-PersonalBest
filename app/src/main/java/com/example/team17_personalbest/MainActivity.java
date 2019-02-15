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
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "MainActivity";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private TextView mTextMessage;
    private HomeDisplayManager homeDisplayManager;
    private User user;
    private FitnessService fitnessService;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        //TODO Remove? and remove StepCountActivity?
        Button btnGoToSteps = findViewById(R.id.start_walk);
        btnGoToSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStepCountActivity();
            }
        });



        // Manage steps and goal displays
        TextView currSteps = findViewById(R.id.curr_steps);
        TextView dailyGoal = findViewById(R.id.daily_goal);
        homeDisplayManager = new HomeDisplayManager(currSteps, dailyGoal);
        user = new User(100);
        user.addObserver(homeDisplayManager);


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

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }


}
