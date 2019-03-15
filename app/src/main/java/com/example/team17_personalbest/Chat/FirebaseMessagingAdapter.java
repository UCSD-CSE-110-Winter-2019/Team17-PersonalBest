package com.example.team17_personalbest.Chat;

import com.example.team17_personalbest.Chat.MyFirebaseMessaging;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseMessagingAdapter extends MyFirebaseMessaging {
    FirebaseMessaging fbm;

    public FirebaseMessagingAdapter(FirebaseMessaging fbm){
        this.fbm = fbm;
    }

    public Task<Void> subscribeToTopic(String s){
        return fbm.subscribeToTopic(s);
    }


}
