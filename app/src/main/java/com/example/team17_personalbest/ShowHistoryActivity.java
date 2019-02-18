package com.example.team17_personalbest;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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

    }

    public void showHistory(ArrayList<Day> hist){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < hist.size(); i++){
            barEntries.add(new BarEntry(i, hist.get(i).getNormalSteps()));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        final String[] axes = new String[]{"Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return axes[(int) value];
            }

        };
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(formatter);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

    /**
     * Loads the user settings and history from sharedPreferences
     */
    public void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Gson gson = new Gson();
        String userjson = sharedPreferences.getString("user", "");
        if (userjson.equals("")){
            user = null;
        } else {
            user = new User(gson.fromJson(userjson, User.class));
        }
    }
}
