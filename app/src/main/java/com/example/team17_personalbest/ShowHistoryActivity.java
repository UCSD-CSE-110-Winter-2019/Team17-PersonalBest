package com.example.team17_personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class ShowHistoryActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        barChart = (BarChart) findViewById(R.id.bar_graph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(15,1676));
        barEntries.add(new BarEntry(16,1923));
        barEntries.add(new BarEntry(17,2534));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        ArrayList<String> dates = new ArrayList<>();
        dates.add("Feb 15");
        dates.add("Feb 16");
        dates.add("Feb 17");

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

    }
}
