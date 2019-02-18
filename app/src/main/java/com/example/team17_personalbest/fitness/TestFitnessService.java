package com.example.team17_personalbest.fitness;

import com.example.team17_personalbest.MainActivity;
import com.example.team17_personalbest.User;

import java.util.Calendar;

public class TestFitnessService implements FitnessService{
    private int additionalSteps;
    private int totalSteps;
    private int additionalTime;
    private MainActivity activity;
    private User user;
    private Calendar calendar;


    public TestFitnessService(MainActivity activity, User user) {
        this.activity = activity;
        this.user = user;
        additionalSteps = 0;
        calendar = Calendar.getInstance();
        additionalTime = 0;
        totalSteps = user.getTotalDailySteps();
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {

    }

    @Override
    public void updateStepCount() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.MILLISECOND, additionalTime);
        if (newCalendar.get(Calendar.DAY_OF_WEEK) != calendar.get(Calendar.DAY_OF_WEEK)) {
            totalSteps = 0;
            additionalSteps = 0;
        }
        calendar = newCalendar;
        user.updateDailySteps(totalSteps + additionalSteps, calendar);
    }

    @Override
    public void addSteps(int steps) {
        additionalSteps += steps;
    }

    @Override
    public void addTime(int millis) {
        additionalTime += millis;
    }

    @Override
    public Calendar getTime() {
        return calendar;
    }
}
