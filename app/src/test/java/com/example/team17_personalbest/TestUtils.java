package com.example.team17_personalbest;

import android.content.Intent;

import org.robolectric.RuntimeEnvironment;

import com.example.team17_personalbest.Firestore.DatabaseFactory;
import com.example.team17_personalbest.Firestore.IDatabase;


public class TestUtils {
    public static final String DATABASE_EXTRA = "DATABASE";


    public static Intent getFriendActivityIntent(IDatabase db) {
        String testFriendKey = "test friend functions";
        DatabaseFactory.getInstance().put(testFriendKey, () -> db);

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(DATABASE_EXTRA, testFriendKey);

        return intent;
    }


}
