package com.example.team17_personalbest.Chat;

import android.support.annotation.NonNull;

import com.example.team17_personalbest.Chat.MyCollectionReference;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class CollectionReferenceAdapter extends MyCollectionReference {
    CollectionReference ref;

    public CollectionReferenceAdapter(CollectionReference ref){
        this.ref = ref;
    }

    public Task<DocumentReference> add(@NonNull Object data) {
        return ref.add(data);
    }

    public Query orderBy(@NonNull String field, @NonNull Query.Direction direction) {
        return ref.orderBy(field, direction);
    }
}
