package com.example.team17_personalbest.Step;

import java.util.Calendar;

public interface StepSubject {
    public void addStepObserver(StepObserver observer);
    public void notifyStepObservers(int steps, Calendar calendar);
}
