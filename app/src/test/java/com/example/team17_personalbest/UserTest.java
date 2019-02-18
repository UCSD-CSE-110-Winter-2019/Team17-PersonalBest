package com.example.team17_personalbest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class UserTest {

    /*@Test
    public void test_walk(){
        // Walking 100 steps
        User user = new User(100, Calendar.getInstance());
        assertEquals(user.getTotalDailySteps(), 0);
        user.walk(100, Calendar.getInstance());
        assertEquals(user.getTotalDailySteps(), 100);
    }

    @Test
    public void test_plannedWalk(){
        // Walking 100 planned steps
        User user = new User(100, Calendar.getInstance());
        user.startPlannedWalk(Calendar.getInstance());
        user.walk(100, Calendar.getInstance());
        PlannedWalk plannedWalk = user.getCurrentWalk();
        assertEquals(plannedWalk.getSteps(), 100);

        // Ending walk
        user.endPlannedWalk();
        assertEquals(user.getCurrentWalk(), null);

    }

    @Test
    public void test_updateDailySteps(){
        // Updating to 1000 steps
        User user = new User(100, Calendar.getInstance());
        user.walk(100, Calendar.getInstance());
        user.updateDailySteps(1000, Calendar.getInstance());
        assertEquals(user.getTotalDailySteps(), 1000);
    }

    @Test
    public void test_finishDay(){
        Calendar tuesday = Calendar.getInstance();
        tuesday.set(2000, 3, 4);

        // Walking 100 normal steps and 150 planned steps and calling finishDay updates stepHistory
        // and resets hasBeenEncouragedToday
        User user = new User(100, tuesday);
        assertEquals(user.isHasBeenEncouragedToday(), false);
        assertEquals(user.isHasBeenCongratulatedToday(), false);
        user.setHasBeenEncouragedToday(true);
        user.setHasBeenCongratulatedToday(true);
        assertEquals(user.isHasBeenEncouragedToday(), true);
        assertEquals(user.isHasBeenCongratulatedToday(), true);
        user.walk(100, tuesday);
        user.startPlannedWalk(tuesday);
        user.walk(150, tuesday);
        user.finishDay(tuesday);
        StepHistory stepHistory = user.getStepHistory();
        assertEquals(stepHistory.getHist().get(2).getNormalSteps(), 100);
        assertEquals(stepHistory.getHist().get(2).getPlannedSteps(), 150);
        assertEquals(user.isHasBeenEncouragedToday(), false);
        assertEquals(user.isHasBeenCongratulatedToday(), false);
    }


    @Test
    public void test_walkNewDay(){
        Calendar tuesday = Calendar.getInstance();
        tuesday.set(2000, 3, 4);

        Calendar wednesday = Calendar.getInstance();
        wednesday.set(2000, 3, 5);

        Calendar thursday = Calendar.getInstance();
        thursday.set(2000, 3, 6);

        // Walking on new days updates stepHistory
        User user = new User(100, tuesday);
        user.walk(100, tuesday);
        user.walk(200, wednesday);
        user.startPlannedWalk(wednesday);
        user.walk(250, wednesday);
        user.walk(300, thursday);
        StepHistory stepHistory = user.getStepHistory();
        assertEquals(stepHistory.getHist().get(2).getNormalSteps(), 100);
        assertEquals(stepHistory.getHist().get(3).getNormalSteps(), 200);
        assertEquals(stepHistory.getHist().get(3).getPlannedSteps(), 250);
        assertEquals(stepHistory.getHist().get(4).getNormalSteps(), 300);
        assertEquals(user.getCurrentWalk(), null);

    }*/

}