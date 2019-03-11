package com.example.team17_personalbest.Chat;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class MyCollectionReference {

    public MyCollectionReference(){};

    public Task<DocumentReference> add(@NonNull Object data) {
        return null;
    }

    public Query orderBy(@NonNull String field, @NonNull Query.Direction direction) {
        return null;
    }

}
