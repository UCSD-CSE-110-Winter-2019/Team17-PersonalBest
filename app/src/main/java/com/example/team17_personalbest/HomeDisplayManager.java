package com.example.team17_personalbest;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class HomeDisplayManager implements Observer {
    private TextView dailySteps;
    private TextView dailyGoal;
    private TextView walkSteps;
    private TextView walkDistance;
    private TextView walkSpeed;
    private Button startWalk;
    private Button endWalk;

    public HomeDisplayManager(TextView dailySteps, TextView dailyGoal, TextView walkSteps,
                              TextView walkDistance, TextView walkSpeed, Button startWalk,
                              Button endWalk){
        this.dailySteps = dailySteps;
        this.dailyGoal = dailyGoal;
        this.walkSteps = walkSteps;
        this.walkDistance = walkDistance;
        this.walkSpeed = walkSpeed;
        this.startWalk = startWalk;
        this.endWalk = endWalk;
    }

    /**
     * Update step and goal on home page
     * @param o observable class
     * @param arg current User object
     */
    @Override
    public void update(Observable o, Object arg) {
        User user = (User) arg;
        IPlannedWalk plannedWalk = user.getCurrentWalk();
        //TODO: use format string instead
        dailySteps.setText(String.format(Locale.US,"%d", user.getTotalDailySteps()));
        dailyGoal.setText(String.format(Locale.US,"/%d", user.getGoal()));
        if (plannedWalk != null){
            walkSteps.setText(String.format(Locale.US,"%d Steps", plannedWalk.getSteps()));
            walkDistance.setText(String.format(Locale.US,"%.2f Miles", plannedWalk.getDistance()));
            walkSpeed.setText(String.format(Locale.US,"%.2f MPH", plannedWalk.getSpeed()));
        }
        else{
            walkSteps.setText(String.format(Locale.US,"0 Steps"));
            walkDistance.setText(String.format(Locale.US,"0.00 Miles"));
            walkSpeed.setText(String.format(Locale.US,"0.00 MPH"));
        }
    }


    /**
     * Sets the Planned Walk displays to visible
     */
    public void startWalk(){
        walkSteps.setVisibility(View.VISIBLE);
        walkDistance.setVisibility(View.VISIBLE);
        walkSpeed.setVisibility(View.VISIBLE);
        startWalk.setVisibility(View.INVISIBLE);
        endWalk.setVisibility(View.VISIBLE);
    }


    /**
     * Sets the Planned Walk displays to invisible
     */
    public void endWalk(){
        walkSteps.setVisibility(View.INVISIBLE);
        walkDistance.setVisibility(View.INVISIBLE);
        walkSpeed.setVisibility(View.INVISIBLE);
        startWalk.setVisibility(View.VISIBLE);
        endWalk.setVisibility(View.INVISIBLE);
    }
}
