package com.example.team17_personalbest;

import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.Step.PlannedWalk;
import com.example.team17_personalbest.Step.StepHistory;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Robolectric;

import android.graphics.drawable.ColorDrawable;
import android.widget.*;
import org.robolectric.RuntimeEnvironment;

import android.content.Intent;
import android.app.Application;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class PlannedWalkTest {

    MainActivity activity;
    User user;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
        activity.findViewById(R.id.start_walk).performClick();

        user = activity.user;
    }

    @Test
    public void test_userPlannedWalk() {
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
        activity.findViewById(R.id.start_walk).performClick();

        User user = activity.user;

        assertNotNull(user.getCurrentWalk());
        user.walk(50, Calendar.getInstance());
        assertEquals(user.getCurrentWalk().getSteps(), 50);
    }

    @Test
    public void test_endPlannedWalk() {
        user.walk(500, Calendar.getInstance());
        int oldSteps = user.getCurrentWalk().getSteps();

        activity.findViewById(R.id.start_walk).performClick();

        assertNull(user.getCurrentWalk());
    }

    @Test
    public void test_plannedWalkUI() {
        Button walk = activity.findViewById(R.id.start_walk);
        user.walk(500, Calendar.getInstance());
        assertEquals(((ColorDrawable) walk.getBackground()).getColor(), activity.getResources().getColor(R.color.colorAccent));

        walk.performClick();

        assertNotEquals(((ColorDrawable) walk.getBackground()).getColor(), activity.getResources().getColor(R.color.colorAccent));
    }

    @Test
    public void test_plannedWalkTextViews() {
        TextView steps = activity.findViewById(R.id.curr_steps);
        TextView goal = activity.findViewById(R.id.daily_goal);
        TextView walk_steps = activity.findViewById(R.id.walk_steps);

        user.walk(500, Calendar.getInstance());

        assertEquals(steps.getText(), "500");
        assertEquals(goal.getText(), "/" + user.getGoal());
        assertTrue(walk_steps.getText().equals("0 steps") || walk_steps.getText().equals("500 steps"));
    }
}