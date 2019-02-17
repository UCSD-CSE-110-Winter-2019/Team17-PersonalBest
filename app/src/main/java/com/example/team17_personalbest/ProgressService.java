package com.example.team17_personalbest;

import android.app.Activity;
import android.widget.Toast;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class ProgressService implements Observer {

    Activity activity;

    public ProgressService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void update(Observable o, Object arg) {
        final User user = (User) arg;
        int stepsOfToday = user.getTotalDailySteps();
        int stepsOfYesterday = user.getStepHistory().getYesterdayStep();
        // Progress = (today's step - yesterday's step) / 500
        final int newProgress = (stepsOfToday - stepsOfYesterday) / 500;
        // Whenever reaching a new progress, show Toast message
        if( newProgress >= 1 && !user.isHasBeenEncouragedToday() ) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String encouragement = String.format(Locale.US, "You've increased your daily steps by over %d steps. Keep up the good work!", newProgress * 500);
                    Toast.makeText(activity.getApplicationContext(), encouragement, Toast.LENGTH_LONG).show();
                    user.setHasBeenEncouragedToday(true);
                }
            });
        }
    }

}