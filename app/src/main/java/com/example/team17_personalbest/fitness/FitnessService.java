package com.example.team17_personalbest.fitness;

import java.util.Calendar;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void addSteps(int steps);
    void addTime(int millis);
    Calendar getTime();
}
