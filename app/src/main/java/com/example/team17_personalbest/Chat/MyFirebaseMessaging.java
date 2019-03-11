package com.example.team17_personalbest.Chat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.Executor;

public class MyFirebaseMessaging{
    public String document;

    public Task<Void> subscribeToTopic(String s) {
        document = s;

        return new Task<Void>() {
            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isSuccessful() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Nullable
            @Override
            public Void getResult() {
                return null;
            }

            @Nullable
            @Override
            public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
                return null;
            }

            public Task<Void> addOnCompleteListener(@NonNull OnCompleteListener onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        };
    }
}
