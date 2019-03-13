package com.example.team17_personalbest.Time;

import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.User;

import java.util.Calendar;

public class UserNewDayManager implements TimeObserver{
    @Override
    public void updateTime(Calendar calendar, Object object) {
        User user = (User) object;
        if(isNewDay(calendar, user))
            finishDay(calendar, user);
    }

    /**
     * Description: Returns true if it's a new day for user
     * Inputs:  calendar - the current time
     *          user - the user
     */
    private boolean isNewDay(Calendar calendar, User user){
        int oldDay = user.getStepHistory().getCurrentDay().getDay();
        int currDay = calendar.get(Calendar.DAY_OF_WEEK);
        return oldDay != currDay;
    }

    /**
     * Description: Sets fields to start a new day for user
     * Inputs:  calendar - the current time
     *          user - the user
     */
    public void finishDay(Calendar calendar, User user){
        user.setCurrentWalk(null);
        user.getStepHistory().updateHist(new Day(calendar));
        user.setHasBeenEncouragedToday(false);
        user.setHasBeenCongratulatedToday(false);
        user.setTotalDailySteps(0);
    }
}
