package com.example.myapplicationinst.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message implements Comparable<Message> {

    @ServerTimestamp
    private Date timeStamp;
    private String currentUser;
    private String message;
    private String messageIds;

    public Message() {
    }

    public Message(String currentUser, String message) {
        this.currentUser = currentUser;
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Exclude
    public String getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(String messageIds) {
        this.messageIds = messageIds;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int compareTo(Message o) {
        return getTimeStamp().compareTo(o.getTimeStamp());
    }
}
