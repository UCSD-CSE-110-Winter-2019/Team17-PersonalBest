package com.example.team17_personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class ShowHistoryActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        barChart = (BarChart) findViewById(R.id.bar_graph);


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
}
