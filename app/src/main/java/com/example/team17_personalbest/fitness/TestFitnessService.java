package com.example.team17_personalbest.fitness;

import com.example.team17_personalbest.MainActivity;
import com.example.team17_personalbest.User;

import java.util.Calendar;

public class TestFitnessService extends GoogleFitAdapter{
    private int additionalSteps;
    private int totalSteps;
    private int additionalTime;
    private MainActivity activity;
    private User user;
    private Calendar calendar;
    private boolean useGoogleFitData;


    public TestFitnessService(MainActivity activity, User user) {
        super(activity, user);
        this.activity = activity;
        this.user = user;
        additionalSteps = 0;
        calendar = Calendar.getInstance();
        additionalTime = 0;
        totalSteps = user.getTotalDailySteps();
        useGoogleFitData = true;
    }

    @Override
    public void updateStepCount() {
        if(useGoogleFitData)
            super.updateStepCount();
        else {
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.add(Calendar.MILLISECOND, additionalTime);
            if (newCalendar.get(Calendar.DAY_OF_WEEK) != calendar.get(Calendar.DAY_OF_WEEK)) {
                totalSteps = 0;
                additionalSteps = 0;
            }
            calendar = newCalendar;
            user.updateDailySteps(totalSteps + additionalSteps, calendar);
        }
    }

    @Override
    public void addSteps(int steps) {
        additionalSteps += steps;
        if (useGoogleFitData)
            stopUsingGoogleFit();
    }

    @Override
    public void addTime(int millis) {
        additionalTime += millis;
        if (useGoogleFitData)
            stopUsingGoogleFit();
    }

    @Override
    public Calendar getTime() {
        if(useGoogleFitData)
            return super.getTime();
        else
            return calendar;
    }

    private void stopUsingGoogleFit(){
        useGoogleFitData = false;
        totalSteps = user.getTotalDailySteps();
    }
}
