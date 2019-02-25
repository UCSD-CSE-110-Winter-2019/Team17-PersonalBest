package com.example.team17_personalbest;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowHistoryActivity extends AppCompatActivity {

    private BarChart barChart;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        barChart = (BarChart) findViewById(R.id.bar_graph);
        loadUser();
        showHistory(user.getStepHistory().getHist());
        user.getStepHistory().printHist();

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                // Switching between home screen and history
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                //mTextMessage.setText(R.string.title_home);
                                finish();
                                return true;
                            case R.id.navigation_dashboard:
                                return true;
                        }
                        return false;
                    }
                });
        navigation.setSelectedItemId(R.id.navigation_dashboard);

    }

    public void showHistory(ArrayList<Day> hist){
        // Get data from hist
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < hist.size(); i++){
            BarEntry entry = new BarEntry(i, new float[]{hist.get(i).getNormalSteps(), hist.get(i).getPlannedSteps()});
            barEntries.add(entry);
        }

        // Format data
        BarDataSet barDataSet = new BarDataSet(barEntries, "Step History");
        barDataSet.setColors(Color.GREEN, Color.RED);
        barDataSet.setStackLabels(new String[] {"Normal Steps", "Planned Steps"});
        barDataSet.setValueFormatter(new StackedValueFormatter(true, "", 0));

        // Format axes
        final String[] axes = new String[]{"Sun", "Mon","Tue","Wed","Thu","Fri","Sat"};
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return axes[(int) value];
            }

        };
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(formatter);


        // Format bar chart
        barChart.setDrawGridBackground(false);
        Description desc = new Description();
        desc.setText("");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setVisibleYRange(0, (float)user.getGoal() + 1000, YAxis.AxisDependency.LEFT);
        barChart.setExtraOffsets(20,20,20,20);
        barChart.setDescription(desc);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setFitBars(true);
    }

    /**
     * Loads the user settings and history from sharedPreferences
     */
    public void loadUser() {

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Gson gson = new Gson();
        int height = sharedPreferences.getInt("height", 0);
        if (height == 0){
            user = null;
        } else {

            user= new User(height, Calendar.getInstance());
            StepHistory stepHistory = gson.fromJson(sharedPreferences.getString("stepHist", ""), StepHistory.class);
            PlannedWalk plannedWalk = gson.fromJson(sharedPreferences.getString("plannedWalk", ""), PlannedWalk.class);
            Day day = gson.fromJson(sharedPreferences.getString("day", ""), Day.class);
            user.setGoal(sharedPreferences.getInt("goal", 0));
            user.setTotalDailySteps(sharedPreferences.getInt("daily_steps", 0));
            user.setHasBeenEncouragedToday(sharedPreferences.getBoolean("encouraged", false));
            user.setHasBeenCongratulatedToday(sharedPreferences.getBoolean("congratulated",false));
            user.setStepHistory(stepHistory);
            user.setCurrentWalk(plannedWalk);
            user.setCurrentDayStats(day);


        }
    }
}
