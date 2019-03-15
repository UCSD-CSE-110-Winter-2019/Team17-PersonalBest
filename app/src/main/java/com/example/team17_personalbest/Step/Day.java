package com.example.team17_personalbest.Step;


import java.util.Calendar;

public class Day {

    private int day;
    private int plannedSteps;
    private int normalSteps;


    public Day(Calendar calendar){
        day = calendar.get(Calendar.DAY_OF_WEEK);
        plannedSteps = 0;
        normalSteps = 0;
    }

    public Day(int testDay){
        day = testDay;
        plannedSteps = 0;
        normalSteps = 0;
    }

    public String getDayString(){
        String currDay;
        currDay = "ERROR!";
        switch(day) {
            case Calendar.SUNDAY:
                currDay = "Sunday";
                break;
            case Calendar.MONDAY:
                currDay = "Monday";
                break;
            case Calendar.TUESDAY:
                currDay = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                currDay = "Wednesday";
                break;
            case Calendar.THURSDAY:
                currDay = "Thursday";
                break;
            case Calendar.FRIDAY:
                currDay = "Friday";
                break;
            case Calendar.SATURDAY:
                currDay = "Saturday";
                break;
        }

        return currDay;
    }

    public int getNormalSteps() {
        return normalSteps;
    }

    public int getPlannedSteps() {
        return plannedSteps;
    }

    public void setNormalSteps(int normalSteps) {
        this.normalSteps = normalSteps;
    }

    public void setPlannedSteps(int plannedSteps) {
        this.plannedSteps = plannedSteps;
    }

    public void addNormalSteps(int normalSteps) {
        this.normalSteps += normalSteps;
    }

    public void addPlannedSteps(int plannedSteps) {
        this.plannedSteps += plannedSteps;
    }

    public int getDay() {
        return day;
    }
}
