package com.example.team17_personalbest.Firestore;

import android.util.Log;

import com.example.team17_personalbest.GoogleFit.FitnessService;
import com.example.team17_personalbest.GoogleFit.FitnessServiceFactory;
import com.example.team17_personalbest.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class DatabaseFactory {
    private static DatabaseFactory instance;

    public static DatabaseFactory getInstance() {
        if (instance == null) {
            instance = new DatabaseFactory();
        }
        return instance;
    }

    private static final String TAG = "[DatabaseFactory]";

    private static Map<String, DatabaseFactory.BluePrint> blueprints = new HashMap<>();

    public static void put(String key, DatabaseFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static IDatabase create(String key, MainActivity activity) {
        Log.i(TAG, String.format("creating IDatabase with key %s", key));
        return blueprints.get(key).create();
    }

    public IDatabase get(String key) {
        return getOrDefault(key, () -> null);
    }

    public IDatabase getOrDefault(String key, BluePrint defaultBlueprint) {
        BluePrint blueprint = blueprints.get(key);
        if (blueprint == null) {
            return defaultBlueprint.create();
        }
        return blueprint.create();
    }

    public interface BluePrint {
        IDatabase create();
    }
}
