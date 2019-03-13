package com.example.team17_personalbest.Step;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.team17_personalbest.Friends.ShowFriendsActivity;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.User;
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
import java.util.List;

public class ShowHistoryActivity extends AppCompatActivity {

    private BarChart barChart1;
    private BarChart barChart2;
    private BarChart barChart3;
    private BarChart barChart4;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        barChart1 = findViewById(R.id.bar_graph);
        barChart2 = findViewById(R.id.bar_graph2);
        barChart3 = findViewById(R.id.bar_graph3);
        barChart4 = findViewById(R.id.bar_graph4);
        loadUser();
        ArrayList<Day> hist = user.getStepHistory().getHist();
        showHistory(hist.subList(0,7), barChart1);
        showHistory(hist.subList(7,14), barChart2);
        showHistory(hist.subList(14,21), barChart3);
        showHistory(hist.subList(21,28), barChart4);
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
                            case R.id.navigation_history:
                                return true;
                            case R.id.navigation_friends:
                                launchFriends();
                                return true;
                        }
                        return false;
                    }
                });
        navigation.setSelectedItemId(R.id.navigation_history);

    }

    public void showHistory(List<Day> hist, BarChart barChart){
        // Get data from hist
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        for(int i = 0; i < hist.size(); i++){
            BarEntry entry = new BarEntry(i, new float[]{hist.get(i).getNormalSteps(), hist.get(i).getPlannedSteps()});
            barEntries.add(entry);
            days.add(Integer.toString( hist.get(i).getDay()));
        }

        // Format data
        BarDataSet barDataSet = new BarDataSet(barEntries, "Step History");
        barDataSet.setColors(Color.GREEN, Color.RED);
        barDataSet.setStackLabels(new String[] {"Normal Steps", "Planned Steps"});
        barDataSet.setValueFormatter(new StackedValueFormatter(true, "", 0));

        // Format axes
        final String[] axes = days.toArray(new String[0]);

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
        barChart.setExtraOffsets(10,10,10,10);
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

    /**
     * Displays friend list
     */
    private void launchFriends() {
        finish();
        Intent intent = new Intent(this, ShowFriendsActivity.class);
        startActivity(intent);
    }
}
