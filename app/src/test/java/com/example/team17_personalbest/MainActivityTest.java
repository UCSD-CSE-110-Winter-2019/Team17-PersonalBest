package com.example.team17_personalbest;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.example.team17_personalbest.GoogleFit.FitnessService;
import com.example.team17_personalbest.GoogleFit.FitnessServiceFactory;
import com.example.team17_personalbest.GoogleFit.TestFitnessService;
import com.example.team17_personalbest.User;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)

public class MainActivityTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private MainActivity activity;
    private TextView textSteps;
    private Button btnAddSteps;
    private Button btnStartWalk;
    private long nextStepCount;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("email");
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestFitnessService(mainActivity, user);
            }
        });

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        textSteps = activity.findViewById(R.id.curr_steps);
        btnAddSteps = activity.findViewById(R.id.add_steps_button);
        btnStartWalk = activity.findViewById(R.id.start_walk);
        nextStepCount = 1500;
    }

    @Test
    public void testStartWalkButton() {
        assertEquals("Loading", textSteps.getText().toString());
        btnStartWalk.performClick();
        //btnAddSteps.performClick();
        assertEquals("0", textSteps.getText().toString());
    }

    @Test
    public void testToast() {
        btnStartWalk.performClick();
        btnAddSteps.performClick();
        assertEquals(null, ShadowToast.getTextOfLatestToast());
    }


}
