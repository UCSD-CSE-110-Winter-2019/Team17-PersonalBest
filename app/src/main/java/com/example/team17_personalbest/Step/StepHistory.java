package com.example.team17_personalbest.Step;

import com.example.team17_personalbest.User;

import java.util.ArrayList;
import java.util.Calendar;

public class StepHistory implements StepObserver {

    private ArrayList<Day> hist;

    public StepHistory(){
        hist = new ArrayList<>();
    }

    public void updateHist(Day d){
        hist.add(d);
    }

    public ArrayList<Day> getHist(){
        ArrayList<Day> dayList = new ArrayList<>();

        for(int x = 0; x < 28; x++){
            dayList.add(new Day(x + 1));
        }
        int i = hist.size() - 1;
        int j = 0;
        while(j < dayList.size() && i >= 0){
            Day currDay = hist.get(i);
            dayList.set(j, currDay);
            i--;
            j++;
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
            System.out.println(hist.get(i).getDayString() + ", "
                    + hist.get(i).getPlannedSteps() + ", " + hist.get(i).getNormalSteps());
        }
        if(hist.size() == 0) {
            System.out.println("History is empty");
        }
    }

    public int getYesterdayStep () {
        if (hist.size() < 2)
            return -1;
        Day yesterday = hist.get(hist.size()-2);
        return yesterday.getNormalSteps() + yesterday.getPlannedSteps();
    }

    /**
     * Setter and getter
     * @param stepHistory an stepHistory object to copy
     */
    public void setHist(ArrayList<Day> stepHistory){
        //if(stepHistory != null) {
            for(int i = stepHistory.size()-1; i >= 0; i--){
                this.hist.add(stepHistory.get(i));
            }
        //}
    }

    public ArrayList<Day> getHistory(){
        return hist;
    }

    public Day getCurrentDay(){
        return hist.get(hist.size() - 1);
    }

    @Override
    public void updateSteps(int steps, User user, Calendar calendar) {
        Day today = getCurrentDay();
        if (user.getCurrentWalk() == null)
            today.addNormalSteps(steps);
        else
            today.addPlannedSteps(steps);
    }
}
