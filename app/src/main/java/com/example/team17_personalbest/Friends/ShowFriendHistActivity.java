package com.example.team17_personalbest.Friends;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.Day;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShowFriendHistActivity extends AppCompatActivity {

    User user;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend_hist);

        Bundle bundle = getIntent().getExtras();
        String friendName = "";
        if(bundle != null){
            friendName = bundle.getString("friend_name");
        }
        TextView name = findViewById(R.id.name);
        name.setText(friendName);

        barChart = findViewById(R.id.bar_chart);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showHistory(List<Day> hist, BarChart barChart){
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
}
