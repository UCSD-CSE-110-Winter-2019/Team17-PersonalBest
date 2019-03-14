package com.example.team17_personalbest.Time;

import java.util.Calendar;

public interface TimeSubject {
    void addTimeObserver(TimeObserver observer);
    void notifyTimeObservers(Calendar calendar);
}
