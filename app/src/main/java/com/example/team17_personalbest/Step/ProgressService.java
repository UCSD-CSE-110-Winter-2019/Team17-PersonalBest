package com.example.team17_personalbest.Step;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team17_personalbest.User;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class ProgressService implements Observer {

    Activity activity;
    User user;

    public ProgressService(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }

    /**
     * Observing user steps to decide when to give an encouragement message
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        final User user = (User) arg;
        int stepsOfToday = user.getTotalDailySteps();
        int stepsOfYesterday = user.getStepHistory().getYesterdayStep();
        // Progress = (today's step - yesterday's step) / 500
        final int newProgress = (stepsOfToday - stepsOfYesterday) / 500;
        // Whenever reaching a new progress, show Toast message
        if( newProgress >= 1 && !user.isHasBeenEncouragedToday() && !user.getHasFriends() ) {
            user.setHasBeenEncouragedToday(true);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String encouragement = String.format(Locale.US, "You've increased your daily steps by over %d steps. Keep up the good work!", newProgress * 500);
                    Toast.makeText(activity.getApplicationContext(), encouragement, Toast.LENGTH_LONG).show();
                }
            });
        }
        // Whenever reaching a goal, prompt user to set a new goal
        if( user.reachedGoal() && !user.isHasBeenCongratulatedToday() ) {
            user.setHasBeenCongratulatedToday(true);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayGoalPrompt();
                }
            });
        }
    }

    /**
     * Creates a popup that lets the user add time
     */
    public void displayGoalPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Congratulations! Do you want to set a new step goal?");

        // Set up user input
        final EditText dialogInput = new EditText(activity);
        dialogInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogInput.setHint("Enter new goal or accept default: " + (user.getGoal() + 500));
        dialogInput.setTextSize(10);
        builder.setView(dialogInput);

        // Confirm button controller
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newGoal;
                if (dialogInput.getText().toString().equals("")){
                    newGoal = user.getGoal() + 500;
                } else {
                    newGoal = Integer.parseInt(dialogInput.getText().toString());
                }
                user.setGoal(newGoal);
            }
        });

        // Cancel button controller
        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}