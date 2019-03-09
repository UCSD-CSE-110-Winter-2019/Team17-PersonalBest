package com.example.team17_personalbest;

import com.example.team17_personalbest.fitness.CloudStroage;

public class FriendManager {

    User user;
    CloudStroage cloud;

    public FriendManager(User user, CloudStroage cloud){
        this.user = user;
        this.cloud = cloud;
    }

    public void addFriend(String friend){
        //TODO: verify if email is valid
        if(!cloud.userExists(friend)) {
            // return ERROR;
        }
        cloud.addPendingFriend(this.user.getUserName(), friend);
        cloud.addPendingRequest(friend, this.user.getUserName());
    }

    public void removeFriend(String friend){
        cloud.removeFriend(this.user.getUserName(), friend);
        cloud.removeFriend(friend, this.user.getUserName());
    }

    public void acceptFriendRequest(String friend){
        cloud.removePendingFriend(this.user.getUserName(), friend);
        cloud.addFriend(this.user.getUserName(), friend);

        cloud.removePendingFriend(friend, this.user.getUserName());
        cloud.addFriend(friend, this.user.getUserName());
    }

    public void denyFriendRequest(String friend){
        cloud.removePendingFriend(friend, this.user.getUserName());
        cloud.removePendingRequest(this.user.getUserName(), friend);
    }

}
