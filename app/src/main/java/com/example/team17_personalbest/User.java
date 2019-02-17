package com.example.team17_personalbest;

import java.util.Calendar;
import java.util.Observable;

public class User extends Observable {
    private int height;
    private int goal;
    private int totalDailySteps;
    private PlannedWalk currentWalk;
    private StepHistory stepHistory;
    private Day currentDayStats;

    /**
     * Constructor
     * Description: Initializes the goal to 5000 and the totalDailySteps to 0. The user's
     *              height is set by the caller.
     * Inputs: height - the user's height
     */
    public User(int height) {
        super();
        this.height = height;
        this.goal = 5000;
        this.totalDailySteps = 0;
        this.stepHistory = new StepHistory();
        this.currentDayStats = new Day();
    }


    /** Copy constructor */
    public User(User other){
        super();
        this.height = other.height;
        this.goal = other.goal;
        this.totalDailySteps = other.totalDailySteps;
        this.currentWalk = other.currentWalk;
        this.stepHistory = other.stepHistory;
        this.currentDayStats = other.currentDayStats;

    }


    /**
     * Description: Adds the number of steps to totalDailySteps.
     *              Update planned steps or normal steps for the day.
     *              If there is a PlannedWalk in progress, adds steps to the PlannedWalk
     * Inputs: steps - the number of steps the user walked
     */
    public void walk(int steps){
        this.totalDailySteps += steps;
        if (currentWalk != null){
            currentWalk.walk(steps);
            currentDayStats.addPlannedSteps(steps);
        }
        else{
            currentDayStats.addNormalSteps(steps);
        }
        setChanged();
        notifyObservers(this);
    }


    /**
     * Description: Simulates walking enough to reach totalDailySteps
     * Inputs: totalDailySteps - the number of steps the user walked today
     */
    public void updateDailySteps(long totalDailySteps){
        int steps = (int) totalDailySteps - this.totalDailySteps;
        walk(steps);
    }


    /**
     * Description: Starts a PlannedWalk by initializing a new PlannedWalk
     */
    public void startPlannedWalk() {
        this.currentWalk = new PlannedWalk(this.height, Calendar.getInstance().getTimeInMillis());
        setChanged();
        notifyObservers(this);
    }


    /**
     * Description: Ends a PlannedWalk by setting currentWalk to null
     */
    public void endPlannedWalk() {
        this.currentWalk = null;
        setChanged();
        notifyObservers(this);
    }


    /**
     * Getters and setters
     */
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        setChanged();
        notifyObservers(this);
    }

    public int getTotalDailySteps() {
        return totalDailySteps;
    }

    public PlannedWalk getCurrentWalk() {
        return currentWalk;
    }

    public StepHistory getStepHistory() {
        return stepHistory;
    }

}
