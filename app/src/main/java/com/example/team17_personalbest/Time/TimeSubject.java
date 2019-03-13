package com.example.team17_personalbest.Time;

import java.util.Calendar;

public interface TimeSubject {
    public void addTimeObserver(TimeObserver observer);
    public void notifyTimeObservers(Calendar calendar);
}
