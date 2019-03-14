package com.example.team17_personalbest;

import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.Step.PlannedWalk;
import com.example.team17_personalbest.Step.StepHistory;
import com.example.team17_personalbest.Step.StepObserver;
import com.example.team17_personalbest.Step.StepSubject;
import com.example.team17_personalbest.Time.TimeObserver;
import com.example.team17_personalbest.Time.TimeSubject;
import com.example.team17_personalbest.Time.UserNewDayManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Observable;

public class User extends Observable implements StepSubject, TimeSubject {

    // basic information
    private String userName;
    private String userEmail;
    private int height;

    // Step-related information
    private int goal;
    private int totalDailySteps;
    private PlannedWalk currentWalk;
    private StepHistory stepHistory;
    private boolean hasBeenEncouragedToday;
    private boolean hasBeenCongratulatedToday;

    // friend information
    private HashMap<String, String> friends;
    private HashMap<String, String>  pendingFriends;
    private HashMap<String, String>  pendingRequests;

    // observers
    private ArrayList<StepObserver> stepObservers;
    private ArrayList<TimeObserver> timeObservers;

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
        this.stepHistory.updateHist(new Day(calendar));
        this.hasBeenEncouragedToday = false;
        this.hasBeenCongratulatedToday = false;
        this.stepObservers = new ArrayList<StepObserver>();
        addStepObserver(this.stepHistory);
        this.timeObservers = new ArrayList<TimeObserver>();
        addTimeObserver(new UserNewDayManager());
    }

    /** Copy constructor */
    public User(User other){
        super();
        this.height = other.height;
        this.goal = other.goal;
        this.totalDailySteps = other.totalDailySteps;
        this.currentWalk = other.currentWalk;
        this.stepHistory = other.stepHistory;
        this.hasBeenEncouragedToday = other.hasBeenEncouragedToday;
        this.hasBeenCongratulatedToday = other.hasBeenCongratulatedToday;
        this.stepObservers = new ArrayList<StepObserver>();
        addStepObserver(this.stepHistory);
        this.timeObservers = new ArrayList<TimeObserver>();

    }


    /**
     * Description: Walks some steps at the specified time
     * Inputs: steps - the number of steps the user walked
     *         calendar - the current time
     */
    public void walk(int steps, Calendar calendar){
        this.totalDailySteps += steps;

        notifyTimeObservers(calendar);
        setChanged();
        notifyObservers(this);
        notifyStepObservers(steps, calendar);
    }

    /**
     * Description: Simulates walking enough to reach totalDailySteps
     * Inputs: totalDailySteps - the number of steps the user walked today
     *         calendar - the current time
     */
    public void updateDailySteps(long totalDailySteps, Calendar calendar){
        notifyTimeObservers(calendar);
        int steps = (int) totalDailySteps - this.totalDailySteps;
        walk(steps, calendar);
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
    public void setTotalDailySteps(int totalDailySteps) { this.totalDailySteps = totalDailySteps; }

    public StepHistory getStepHistory() {
        return stepHistory;
    }
    public void setStepHistory(StepHistory stepHistory) {
        this.stepObservers.remove(this.stepHistory);
        this.stepHistory = stepHistory;
        addStepObserver(this.stepHistory);
    }

    public PlannedWalk getCurrentWalk() {
        return currentWalk;
    }
    public void setCurrentWalk(PlannedWalk currentWalk) {
        this.stepObservers.remove(this.currentWalk);
        this.currentWalk = currentWalk;
        addStepObserver(this.currentWalk);
        setChanged();
        notifyObservers(this);
    }

    public String getUserName(){ return userName; }
    public void setUserName(String name){ this.userName = name; }

    public String getUserEmail(){ return userEmail; }
    public void setUserEmail(String email){ this.userEmail = email; }

    public HashMap<String, String>  getFriends(){ return this.friends; }
    public HashMap<String, String>  getPendingFriends(){ return this.pendingFriends; }
    public HashMap<String, String>  getPendingRequests(){ return this.pendingRequests; }
    public void setFriends(HashMap<String, String>  friends){ this.friends = friends; }
    public void setPendingFriends(HashMap<String, String>  pendingFriends){ this.pendingFriends = pendingFriends; }
    public void setPendingRequests(HashMap<String, String>  pendingRequests){ this.pendingRequests = pendingRequests; }


    public boolean isHasBeenCongratulatedToday() {
        return hasBeenCongratulatedToday;
    }
    public void setHasBeenCongratulatedToday(boolean hasBeenCongratulatedToday) {
        this.hasBeenCongratulatedToday = hasBeenCongratulatedToday;
    }
    public boolean isHasBeenEncouragedToday() {
        return hasBeenEncouragedToday;
    }
    public void setHasBeenEncouragedToday(boolean hasBeenEncouragedToday) {
        this.hasBeenEncouragedToday = hasBeenEncouragedToday;
    }


    @Override
    public void addStepObserver(StepObserver observer) {
        if (observer != null)
            this.stepObservers.add(observer);
    }

    @Override
    public void notifyStepObservers(int steps, Calendar calendar) {
        for (StepObserver observer: stepObservers) {
            observer.updateSteps(steps, this, calendar);
        }
    }

    @Override
    public void addTimeObserver(TimeObserver observer) {
        timeObservers.add(observer);
    }

    @Override
    public void notifyTimeObservers(Calendar calendar) {
        for (TimeObserver observer: timeObservers) {
            observer.updateTime(calendar, this);
        }
    }
}
