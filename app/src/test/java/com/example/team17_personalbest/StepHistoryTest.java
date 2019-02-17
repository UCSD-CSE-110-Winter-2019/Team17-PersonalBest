package com.example.team17_personalbest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class StepHistoryTest {

    @Test
    public void test_History(){
        StepHistory stepHistory = new StepHistory();
        int numDays = 7;

        for(int i = 0; i < numDays; i++){
            Day day = new Day((i%7) + 1);
            day.setPlannedSteps(i+1);
            day.setNormalSteps(i);
            stepHistory.updateHist(day);
        }

        Day[] hist = stepHistory.getHist();

        int j = 6;
        for(int i = numDays; i > numDays-7; i--){
            assertEquals(hist[j].getNormalSteps(), i-1);
            assertEquals(hist[j].getPlannedSteps(), i);
            j--;
        }

    }

    @Test
    public void test_getYesterdayStep(){
        StepHistory stepHistory = new StepHistory();
        int numDays = 7;

        for(int i = 0; i < numDays; i++){
            Day day = new Day((i%7) + 1);
            day.setPlannedSteps(i+1);
            day.setNormalSteps(i);
            stepHistory.updateHist(day);
        }

        assertEquals(stepHistory.getYesterdayStep(), 11);
    }


}