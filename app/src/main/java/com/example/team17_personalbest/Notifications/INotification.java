package com.example.team17_personalbest.Notifications;

import com.google.android.gms.tasks.Task;

import java.util.function.Consumer;

public interface INotification {
    void subscribeToNotificationsTopic(String topic, Consumer<Task<Void>> callback);
}