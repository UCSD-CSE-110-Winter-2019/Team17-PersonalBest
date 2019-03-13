package com.example.team17_personalbest.Step;

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
        for(int x = 0; x < 28; x++){
            dayList.add(new Day(x + 1));
        }
        while(i < hist.size() && i >= 0){
            Day currDay = hist.get(i);
            dayList.set(currDay.getDay() - 1, currDay);
            if(currDay.getDay() == Calendar.SUNDAY){ // if Sunday is found, break the loop
                //break;
            }
            i--;
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

    public Day getCurrentDay(){
        return hist.get(hist.size() - 1);
    }
}
