package com.example.team17_personalbest.fitness;

import android.util.Log;
import android.widget.TextView;

import com.example.team17_personalbest.User;
import com.google.android.gms.common.data.DataBufferObserver;

import java.util.ArrayList;
import java.util.List;

public class CloudStroage implements DataBufferObserver.Observable {

    private User user;

    public CloudStroage(User user){
        this.user = user;
    }

    public ArrayList<String> getFriends(String username){
        return new ArrayList<String>();
    }
    public ArrayList<String> getPendingFriends(String username){
        return new ArrayList<String>();
    }
    public ArrayList<String> getPendingRequests(String username){
        return new ArrayList<String>();
    }

    public void addFriend(String user, String friend){  }
    public void addPendingFriend(String user, String friend){  }
    public void addPendingRequest(String user, String friend){  }

    public void removeFriend(String user, String friend){  }
    public void removePendingFriend(String user, String friend){  }
    public void removePendingRequest(String user, String friend){  }

    public boolean userExists(String email) {
        return true;
    }

    @Override
    public void addObserver(DataBufferObserver dataBufferObserver) {

    }

    @Override
    public void removeObserver(DataBufferObserver dataBufferObserver) {

    }

    private void initMessageUpdateListener() {
        chat.addSnapshotListener((newChatSnapShot, error) -> {


            if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    sb.append(document.get(FROM_KEY));
                    sb.append(":\n");
                    sb.append(document.get(TEXT_KEY));
                    sb.append("\n");
                    sb.append("---\n");
                });

                TextView chatView = findViewById(R.id.chat);
                chatView.append(sb.toString());
            }
        });
    }
}
