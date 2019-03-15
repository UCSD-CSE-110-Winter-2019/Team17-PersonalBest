package com.example.team17_personalbest.Step;

import java.util.Calendar;

public interface StepSubject {
    void addStepObserver(StepObserver observer);
    void notifyStepObservers(int steps, Calendar calendar);
}
