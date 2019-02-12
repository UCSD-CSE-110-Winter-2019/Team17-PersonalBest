package com.example.team17_personalbest;

import java.util.ArrayList;
import java.util.Arrays;

public class StepHistory {

    private ArrayList<Day> hist;

    public StepHistory(){
        hist = new ArrayList<>();
    }

    public void updateHist(Day d){
        hist.add(d);
    }

    public Day[] getHist(){
        Day[] result = new Day[7];
        int DAY_NUM = 7;

        Day init = new Day();
        for(int i = 0; i < DAY_NUM; i++){
            result[i] = new Day(i+1);
        }

        int size = hist.size();
        if(size > DAY_NUM){
            size = DAY_NUM;
        }

        for(int i = 0; i < size; i++){
            Day day = hist.get(hist.size()-1-i);
            int d = findDay(day);
            result[d] = day;
        }

        return result;
    }

    private int findDay(Day day){
        switch(day.getDay()){
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
            System.out.println(hist.get(i).getDay() + ", " + hist.get(i).getPlannedSteps() + ", " + hist.get(i).getNormalSteps());
        }
    }

}
