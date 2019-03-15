package com.example.team17_personalbest.Notifications

public class NotificationFactory {
    private static NotificationFactory instance;

    public static NotificationFactory getInstance() {
        if (instance == null) {
            instance = new NotificationFactory();
        }
        return instance;
    }
	
	private static final String TAG = "[NotificationFactory]";

    private static Map<String, NotificationFactory.BluePrint> blueprints = new HashMap<>();

    public static void put(String key, NotificationFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static INotification create(String key, MainActivity activity) {
        Log.i(TAG, String.format("creating INotification with key %s", key));
        return blueprints.get(key).create();
    }

    public INotification get(String key) {
        return getOrDefault(key, () -> null);
    }

    public INotification getOrDefault(String key, BluePrint defaultBlueprint) {
        BluePrint blueprint = blueprints.get(key);
        if (blueprint == null) {
            return defaultBlueprint.create();
        }
        return blueprint.create();
    }

    public interface BluePrint {
        INotification create();
    }
}