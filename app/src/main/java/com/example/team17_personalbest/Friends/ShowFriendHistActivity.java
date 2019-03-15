package com.example.team17_personalbest.Friends;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.team17_personalbest.Firestore.FirebaseAdapter;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.Step.Day;
import com.example.team17_personalbest.Step.ShowHistoryActivity;
import com.example.team17_personalbest.Step.StepHistory;
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

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend_hist);

        FirebaseAdapter cloud = new FirebaseAdapter(FirebaseFirestore.getInstance());
        String friendEmail = getIntent().getStringExtra("friend_email");
        String friendName = getIntent().getStringExtra("friend_name");
        String userEmail = getIntent().getStringExtra("user_name");

        TextView name = findViewById(R.id.name);
        name.setText(friendName);

        StepHistory friendHistory = new StepHistory();
        cloud.getStepHistory(friendEmail, friendHistory);
        barChart = findViewById(R.id.bar_chart);
        showHistory(friendHistory.getHist(), barChart);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText message = findViewById(R.id.message);
        String content = message.getText().toString();
        Button sendMessageButton = findViewById(R.id.send_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cloud.sendMessage(userEmail,friendEmail, content);
            }
        });


        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                // Switching between home screen and history
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                finish();
                                return true;
                            case R.id.navigation_history:
                                launchHistory();
                                return true;
                        }
                        return false;
                    }
                });
        navigation.setSelectedItemId(R.id.navigation_friends);
    }

    public void showHistory(List<Day> hist, BarChart barChart){
        // Get data from hist
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        for(int i = 0; i < hist.size(); i++){
            BarEntry entry = new BarEntry(i, hist.get(i).getNormalSteps() + hist.get(i).getPlannedSteps());
            barEntries.add(entry);
            days.add(Integer.toString( hist.get(i).getDay()));
        }

        // Format data
        BarDataSet barDataSet = new BarDataSet(barEntries, "Step History");
        barDataSet.setColors(Color.CYAN);
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
        barChart.setVisibleYRange(0, 5000, YAxis.AxisDependency.LEFT);
        barChart.setExtraOffsets(10,10,10,10);
        barChart.setDescription(desc);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setFitBars(true);
    }

    /**
     * Displays step history
     */
    private void launchHistory() {
        finish();
        Intent intent = new Intent(this, ShowHistoryActivity.class);
        startActivity(intent);
    }
}
