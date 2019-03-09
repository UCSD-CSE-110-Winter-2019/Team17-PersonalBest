package com.example.team17_personalbest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;

public class User extends Observable {
    private int height;
    private int goal;
    private int totalDailySteps;
    private PlannedWalk currentWalk;
    private StepHistory stepHistory;
    private Day currentDayStats;
    private boolean hasBeenEncouragedToday;
    private boolean hasBeenCongratulatedToday;
    private String userName;

    private ArrayList<String> friends;
    private ArrayList<String> pendingFriends;
    private ArrayList<String> pendingRequests;


    /**
     * Constructor
     * Description: Initializes the goal to 5000 and the totalDailySteps to 0. The user's
     *              height is set by the caller.
     * Inputs: height - the user's height
     */
    public User(int height, Calendar calendar) {
        super();
        this.height = height;
        this.goal = 5000;
        this.totalDailySteps = 0;
        this.stepHistory = new StepHistory();
        this.currentDayStats = new Day(calendar);
        this.stepHistory.updateHist(currentDayStats);
        this.hasBeenEncouragedToday = false;
        this.hasBeenCongratulatedToday = false;


        this.friends = new ArrayList<String>();
    }


    /** Copy constructor */
    public User(User other){
        super();
        this.height = other.height;
        this.goal = other.goal;
        this.totalDailySteps = other.totalDailySteps;
        this.currentWalk = other.currentWalk;
        this.stepHistory = other.stepHistory;
        this.currentDayStats = stepHistory.getCurrentDay();
        this.hasBeenEncouragedToday = other.hasBeenEncouragedToday;
        this.hasBeenCongratulatedToday = other.hasBeenCongratulatedToday;
    }


    /**
     * Description: Adds the number of steps to totalDailySteps.
     *              Update planned steps or normal steps for the day.
     *              If there is a PlannedWalk in progress, adds steps to the PlannedWalk
     * Inputs: steps - the number of steps the user walked
     *         calendar - the current time
     */
    public void walk(int steps, Calendar calendar){
        if(isNewDay(calendar))
            finishDay(calendar);

        this.totalDailySteps += steps;
        if (currentWalk != null){
            currentWalk.walk(steps, calendar);
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
     *         calendar - the current time
     */
    public void updateDailySteps(long totalDailySteps, Calendar calendar){
        if(isNewDay(calendar))
            finishDay(calendar);

        int steps = (int) totalDailySteps - this.totalDailySteps;
        walk(steps, calendar);
    }


    /**
     * Description: Starts a PlannedWalk by initializing a new PlannedWalk
     * Inputs:  calendar - the current time
     */
    public void startPlannedWalk(Calendar calendar) {
        this.currentWalk = new PlannedWalk(this.height, calendar.getTimeInMillis());
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
     * Description: Adds the previous day to stepHistory and starts a new day
     * Inputs:  calendar - the current time
     */
    public void finishDay(Calendar calendar){
        endPlannedWalk();
        currentDayStats = new Day(calendar);
        stepHistory.updateHist(currentDayStats);
        hasBeenEncouragedToday = false;
        hasBeenCongratulatedToday = false;
        totalDailySteps = 0;
    }


    /**
     * Description: Returns true if it's a new day
     * Inputs:  calendar - the current time
     */
    private boolean isNewDay(Calendar calendar){
        int oldDay = currentDayStats.getDay();
        int currDay = calendar.get(Calendar.DAY_OF_WEEK);
        return oldDay != currDay;
    }


    /**
     * Description: Returns true if user reached goal
     */
    public boolean reachedGoal(){
        return totalDailySteps >= goal;
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

    public boolean isHasBeenEncouragedToday() {
        return hasBeenEncouragedToday;
    }

    public void setHasBeenEncouragedToday(boolean hasBeenEncouragedToday) {
        this.hasBeenEncouragedToday = hasBeenEncouragedToday;
    }

    public String getUserName(){ return userName; }

    public void setUserName(String name){ this.userName = name; }

    public boolean isHasBeenCongratulatedToday() {
        return hasBeenCongratulatedToday;
    }

    public void setHasBeenCongratulatedToday(boolean hasBeenCongratulatedToday) {
        this.hasBeenCongratulatedToday = hasBeenCongratulatedToday;
    }

    public ArrayList<String> getFriends(){ return this.friends; }
    public ArrayList<String> getPendingFriends(){ return this.pendingFriends; }
    public ArrayList<String> getPendingRequests(){ return this.pendingFriends; }

    public void setFriends(ArrayList<String> friends){ this.friends = friends; }
    public void setPendingFriends(ArrayList<String> pendingFriends){ this.pendingFriends = pendingFriends; }
    public void setPendingRequests(ArrayList<String> pendingFriends){ this.pendingFriends = pendingFriends; }

}
