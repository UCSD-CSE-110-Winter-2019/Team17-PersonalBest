package com.example.team17_personalbest;

public class User {
    private int height;
    private int goal;
    private int totalDailySteps;
    private PlannedWalk currentWalk;
    private StepHistory stepHistory;


    /**
     * Constructor
     * Description: Initializes the goal to 5000 and the totalDailySteps to 0. The user's
     *              height is set by the caller.
     * Inputs: height - the user's height
     */
    public User(int height) {
        this.height = height;
        this.goal = 5000;
        totalDailySteps = 0;
    }


    /**
     * Description: Adds the number of steps to totalDailySteps.
     *              If there is a PlannedWalk in progress, adds steps to the PlannedWalk
     * Inputs: steps - the number of steps the user walked
     */
    public void walk(int steps){
        this.totalDailySteps += steps;
        // TODO add steps to planned walk
    }


    /**
     * Description: Starts a PlannedWalk by setting currentWalk to a new PlannedWalk
     */
    public void startPlannedWalk(){
        this.currentWalk = new PlannedWalk();
    }


    /**
     * Description: Ends a PlannedWalk by setting currentWalk to null
     *              Stores the old PlannedWalk in stepHistory
     */
    public void endPlannedWalk(PlannedWalk plannedWalk){
        this.currentWalk = null;
        // TODO store old planned walk to stepHistory
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
    }

    public int getTotalDailySteps() {
        return totalDailySteps;
    }

    public void setTotalDailySteps(int totalDailySteps) {
        this.totalDailySteps = totalDailySteps;
    }

    public PlannedWalk getCurrentWalk() {
        return currentWalk;
    }

    public void setCurrentWalk(PlannedWalk currentWalk) {
        this.currentWalk = currentWalk;
    }

    public StepHistory getStepHistory() {
        return stepHistory;
    }

    public void setStepHistory(StepHistory stepHistory) {
        this.stepHistory = stepHistory;
    }
}
