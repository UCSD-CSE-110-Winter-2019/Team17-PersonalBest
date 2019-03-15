package com.example.team17_personalbest;

import android.app.AlertDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.ClassRule;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.robolectric.*;
import org.robolectric.Robolectric;
import android.app.*;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

@RunWith(RobolectricTestRunner.class)
public class GoalTest {

    MainActivity activity;
    User user;

    //@ClassRule public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        activity.findViewById(R.id.start_walk).performClick();

        user = activity.user;
    }

    @Test
    public void test_defaultGoalAndDialog() {
        int goal = user.getGoal();

        WindowManager wm = activity.getWindowManager();

        while(user.getTotalDailySteps() < goal - 50) {
            if(activity.dialog != null)
                assertEquals(activity.dialog.isShowing(), true);
            user.walk(50, Calendar.getInstance());
        }

        user.walk(5500, Calendar.getInstance());
        if(activity.dialog != null)
            assertEquals(activity.dialog.isShowing(), false);
        else
            assertEquals(goal, user.getGoal());
    }

    @Test
    public void test_newGoalAndDialog() {
        int goal = user.getGoal();

        WindowManager wm = activity.getWindowManager();

        while(user.getTotalDailySteps() < goal - 50) {
            if(activity.dialog != null)
                assertEquals(activity.dialog.isShowing(), true);
            user.walk(50, Calendar.getInstance());
        }

        user.walk(5500, Calendar.getInstance());

        if(activity.dialog != null)
            assertEquals(activity.dialog.isShowing(), false);
        else
            assertEquals(goal, user.getGoal());

        goal = user.getGoal();

        while(user.getTotalDailySteps() < goal - 50) {
            if(activity.dialog != null)
                assertEquals(activity.dialog.isShowing(), true);
            user.walk(50, Calendar.getInstance());
        }

        user.walk(6000, Calendar.getInstance());

        if(activity.dialog != null)
            assertEquals(activity.dialog.isShowing(), false);
        else
            assertEquals(goal, user.getGoal());
    }
}
