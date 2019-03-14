package com.example.team17_personalbest.Step;

import android.widget.Button;
import android.widget.TextView;

import com.example.team17_personalbest.MainActivity;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.PlannedWalk;
import com.example.team17_personalbest.User;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class HomeDisplayManager implements Observer {
    private TextView dailySteps;
    private TextView dailyGoal;
    private TextView walkSteps;
    private TextView walkDistance;
    private TextView walkSpeed;
    private TextView walkClock;
    private Button walkButton;
    private MainActivity activity;

    public HomeDisplayManager(TextView dailySteps, TextView dailyGoal, TextView walkSteps,
                              TextView walkDistance, TextView walkSpeed, TextView walkClock,
                              Button walkButton, MainActivity activity){
        this.dailySteps = dailySteps;
        this.dailyGoal = dailyGoal;
        this.walkSteps = walkSteps;
        this.walkDistance = walkDistance;
        this.walkSpeed = walkSpeed;
        this.walkClock = walkClock;
        this.walkButton = walkButton;
        this.activity = activity;
    }

    /**
     * Update step and goal on home page
     * @param o observable class
     * @param arg current User object
     */
    @Override
    public void update(Observable o, Object arg) {
        User user = (User) arg;
        PlannedWalk plannedWalk = user.getCurrentWalk();

        // Daily steps and goal
        dailySteps.setText(String.format(Locale.US,"%d", user.getTotalDailySteps()));
        dailyGoal.setText(String.format(Locale.US,"/%d", user.getGoal()));

        // Planned walk displays
        if (plannedWalk != null){
            startWalk();

            // get planned walk steps
            String currWalkSteps = "" + plannedWalk.getSteps() + " steps";
            walkSteps.setText(currWalkSteps);

            // get and format distance
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(1);
            String currWalkDistance = "" + df.format(plannedWalk.getDistance()) + " miles";
            walkDistance.setText(currWalkDistance);

            // get and format speed
            String currWalkSpeed = "" + df.format(plannedWalk.getSpeed()) + " mph";
            walkSpeed.setText(currWalkSpeed);

            // get and format time
            df.setMinimumIntegerDigits(2);
            long currWalkSeconds = TimeUnit.MILLISECONDS.toSeconds(plannedWalk.getTime());
            long minutes = currWalkSeconds/60;
            long seconds = currWalkSeconds - (minutes * 60);
            String currWalkTime = "" + df.format(minutes) + ":" + df.format(seconds);
            walkClock.setText(currWalkTime);
        } else{
            endWalk();
        }
    }


    /**
     * Sets the Planned Walk displays to visible
     */
    public void startWalk(){
        walkButton.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
        walkButton.setText(activity.getResources().getString(R.string.button_end));
    }


    /**
     * Sets the Planned Walk displays to invisible
     */
    public void endWalk(){
        walkButton.setBackgroundColor(activity.getResources().getColor(R.color.colorGreen));
        walkButton.setText(activity.getResources().getString(R.string.button_start));
        walkSteps.setText("");
        walkDistance.setText("");
        walkSpeed.setText("");
        walkClock.setText("");
    }
}
