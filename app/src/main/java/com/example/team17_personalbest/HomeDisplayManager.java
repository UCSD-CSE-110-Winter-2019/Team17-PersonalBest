package com.example.team17_personalbest;

import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class HomeDisplayManager implements Observer {
    private TextView dailySteps;
    private TextView dailyGoal;

    public HomeDisplayManager(TextView dailySteps, TextView dailyGoal){
        this.dailySteps = dailySteps;
        this.dailyGoal = dailyGoal;
    }

    /**
     * Update step and goal on home page
     * @param o observable class
     * @param arg current User object
     */
    @Override
    public void update(Observable o, Object arg) {
        User user = (User) arg;
        //TODO: use format string instead
        dailySteps.setText(Integer.toString(user.getTotalDailySteps()));
        dailyGoal.setText("/" + Integer.toString(user.getGoal()));
    }
}
