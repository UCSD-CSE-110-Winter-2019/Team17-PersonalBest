package com.example.team17_personalbest;

import java.util.Observable;
import java.util.Observer;
import java.util.Calendar;

public class PlannedWalk implements IPlannedWalk, Observer {

    int steps;
    float distance;
    long startTime;
    float strideLength;
    long time;
    float speed;

    /**
     *
     * @param height - must be in inches, used to calculate stride length
     * @param startTime - current time when walk is started being recorded
     */
    public PlannedWalk(int height, long startTime) {
        this.steps = 0;
        this.strideLength = calculateStride(height);
        this.startTime = startTime;
        this.time = Calendar.getInstance().getTimeInMillis();
        this.speed = 0;
        this.distance = 0;
    }

    public void walk(int steps) {
        this.steps += steps;
        this.distance += calculateDistance(steps);
        this.time = Calendar.getInstance().getTimeInMillis();
        this.speed = calculateSpeed();
    }

    public void refreshValues() {
        this.time = Calendar.getInstance().getTimeInMillis();
        this.speed = calculateSpeed();
    }

    // returns distance in miles
    private float calculateDistance(int steps) {
        return this.strideLength * steps;
    }

    // returns speed in miles per hour
    private float calculateSpeed() {
        this.time = Calendar.getInstance().getTimeInMillis();
        return distance / (((float)(time - startTime) / (1000*60*60)));
    }

    // convert height to stride length in miles
    private float calculateStride(int height) {
        double temp = (height * 0.413); // convert from height to stride length (inches)
        temp *= 1.5783e-5; // convert from inches to miles
        return (float) temp;
    }

    public int getSteps() {
        return this.steps;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getDistance() {
        return this.distance;
    }

    @Override
    public void update(Observable o, Object arg) {
        User user = (User) arg;
    }
}
