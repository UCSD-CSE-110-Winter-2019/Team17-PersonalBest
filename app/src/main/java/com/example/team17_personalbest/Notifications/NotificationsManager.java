package com.example.team17_personalbest.Notifications

import com.example.team17_personalbest.R;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationsManager {
	private NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	private String channel_ID;
	private String channel_name;
	
	public void createNotificationChannel() {
		NotificationChannel channel = new NotificationChannel(channel_ID, channel_name, manager.IMPORTANCE_DEFAULT);
		manager.createNotificationChannel(channel);
	}
	
	public void set_channel_ID(String cid) {
		channel_ID = cid;
	}
	
	public void set_channel_Name(String cname) {
		channel_name = cname;
	}
	
	public NotificationManager getManager() {
		return manager;
	}
	
	public Notification.Builder addNotification(String title, String text, PendingIntent pi) {
		return new Notification.Builder(this)
			.setSmallIcon(R.drawable.abc)
			.setContentTitle(title)
			.setContentText(text)
			.setContentIntent(pi);
	}
	
	public Notification.Builder addNotification(String title, String text) {
		return new Notification.Builder(this)
			.setSmallIcon(R.drawable.abc)
			.setContentTitle(title)
			.setContentText(text);
	}
}