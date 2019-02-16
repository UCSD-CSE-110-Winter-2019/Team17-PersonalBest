package com.example.team17_personalbest;

import android.app.Activity;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class ProgressService implements Observer {

    Activity activity;

    private int progress = 0;

    public ProgressService(Activity activity) {
        this.activity = activity;
    }

    /**
     * Setter for progress. Need to set progress to 0 everyday at midnight
     * @param progress 0
     */
    public void setProgress( int progress ){
        this.progress = progress;
    }

    @Override
    public void update(Observable o, Object arg) {
        User user = (User) arg;
        int stepsOfToday = user.getTotalDailySteps();
        int stepsOfYesterday = user.getStepHistory().getYesterdayStep();
        // Progress = (today's step - yesterday's step) / 500
        int newProgress = (stepsOfToday - stepsOfYesterday) / 500;
        // Whenever reaching a new progress, show Toast message
        if( newProgress > progress ) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO: use format string to set the number of steps in the message, which should be equal to 500 * newProgress
                    Toast.makeText(activity.getApplicationContext(), "You've increased your daily steps by over 500 steps. Keep up the good work!", Toast.LENGTH_LONG).show();
                }
            });
        }
        progress = newProgress;
    }

}