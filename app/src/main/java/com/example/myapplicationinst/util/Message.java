package com.example.myapplicationinst.util;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    @ServerTimestamp
    private Date timeStamp;
    private String currentUser;
    private String message;

    public Message() {
    }

    public Message(String currentUser, String message) {
        this.currentUser = currentUser;
        this.message = message;
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
}
