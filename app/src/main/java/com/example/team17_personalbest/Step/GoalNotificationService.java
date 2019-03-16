package com.example.team17_personalbest.Step;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.team17_personalbest.GoogleFit.FitnessService;
import com.example.team17_personalbest.GoogleFit.FitnessServiceFactory;
import com.example.team17_personalbest.GoogleFit.GoogleFitAdapter;
import com.example.team17_personalbest.GoogleFit.TestFitnessService;
import com.example.team17_personalbest.MainActivity;
import com.example.team17_personalbest.R;
import com.example.team17_personalbest.User;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class GoalNotificationService extends Service {
    private final String CHANNEL_ID = "goal_notifications";
    private final int NOTIFICATION_ID = 1;
    private String fitnessServiceKey = "GOOGLE_FIT";
    private FitnessService fitnessService;
    private static int goal;
    Context context = this;
    Timer goalCheckInterval;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        goal = intent.getIntExtra("goal", 0);
        Toast.makeText(this, "Goal notification service starting. Goal: " + goal, Toast.LENGTH_SHORT).show();

        // Check for goal notification every 5 seconds
        goalCheckInterval = new Timer();
        goalCheckInterval.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                goalNotify();
            }
        }, 10000, 5000);

        return START_REDELIVER_INTENT;
    }

    public void goalNotify(){
        GoogleFitAdapter.getGoogleFitSteps(this).addOnSuccessListener(
                new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        long total =
                                dataSet.isEmpty()
                                        ? 0
                                        : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                        // Send notification if goal has been reached
                        if (total >= goal) {
                            sendNotification();
                            goalCheckInterval.cancel();
                        }
                        Log.d("Goal Notification Service", "Total: "  + Long.toString(total) + " Goal: " + goal);
                    }
                });
    }

    public void sendNotification(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Goals", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class)
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Personal Best")
                .setContentText("You have reached your goal!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
