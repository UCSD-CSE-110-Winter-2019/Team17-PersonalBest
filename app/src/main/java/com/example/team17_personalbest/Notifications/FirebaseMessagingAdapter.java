package com.example.team17_personalbest.Notifications;

package edu.ucsd.cse110.firebaselab.notification;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.function.Consumer;


public class FirebaseMessagingAdapter implements INotification {
    private static INotification instance;

    public static INotification getInstance() {
        if (instance == null) {
            instance = new FirebaseMessagingAdapter();
        }
        return instance;
    }

    @Override
    public void subscribeToNotificationsTopic(String topic, Consumer<Task<Void>> callback) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(callback::accept);
    }
}
