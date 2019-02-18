package com.example.team17_personalbest;

import java.util.ArrayList;
import java.util.Calendar;

public class StepHistory {

    private ArrayList<Day> hist;

    public StepHistory(){
        hist = new ArrayList<>();
    }

    public void updateHist(Day d){
        hist.add(d);
    }

    public ArrayList<Day> getHist(){
        ArrayList<Day> dayList = new ArrayList<>();
        int i = hist.size() - 1;
        while(i < hist.size()){
            dayList.add(hist.get(i));
            if(hist.get(i).getDay() == 0){ // if Sunday is found, break the loop
                break;
            }
        }

        return dayList;
    }

    private int findDay(Day day){
        switch(day.getDayString()){
            case "Sunday":
                return 0;
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            default:
                return 6;
        }
    }

    public void printHist(){
        for(int i = 0; i < hist.size(); i++){
            System.out.println(hist.get(i).getDayString() + ", " + hist.get(i).getPlannedSteps() + ", " + hist.get(i).getNormalSteps());
        }
    }

    public int getYesterdayStep () {
        if (hist.size() < 2)
            return -1;
        Day yesterday = hist.get(hist.size()-2);
        return yesterday.getNormalSteps() + yesterday.getPlannedSteps();
    }

}
