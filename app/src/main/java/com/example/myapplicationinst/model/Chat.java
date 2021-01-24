package com.example.myapplicationinst.model;

import com.example.myapplicationinst.util.Message;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String messageId;
    private List<Message> sendList;
    private List<Message> receiveList;



    public Chat() {
        this.sendList = new ArrayList<>();
        this.receiveList = new ArrayList<>();
    }

    @Exclude
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<Message> getSendList() {
        return sendList;
    }

    public void setSendList(List<Message> sendList) {
        this.sendList = sendList;
    }

    public List<Message> getReceiveList() {
        return receiveList;
    }

    public void setReceiveList(List<Message> receiveList) {
        this.receiveList = receiveList;
    }
}
