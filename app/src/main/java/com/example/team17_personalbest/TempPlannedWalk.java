package com.example.team17_personalbest;

public class TempPlannedWalk implements IPlannedWalk{
    int steps;

    public TempPlannedWalk(){
        this.steps = 0;
    }

    public void walk(int steps){
        this.steps += steps;
    }

    public int getSteps() {
        return steps;
    }
}
