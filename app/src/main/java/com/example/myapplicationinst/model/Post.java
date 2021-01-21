package com.example.myapplicationinst.model;

import android.util.Log;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post implements Comparable<Post> {

    private static final String TAG = "Post";

    @ServerTimestamp
    private Date timestamp;
    private String userId;
    private List<String> imageList;
    private String date;
    private String time;
    private String description;
    private List<String> likeList;
    private List<String> commentList;
    private String postKey;

    public Post() {
    }

    public Post(String userId, List<String> imageList, String date, String time, String description) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.description = description;
        likeList = new ArrayList<>();
        commentList = new ArrayList<>();
        this.imageList = imageList;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    public List<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<String> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int compareTo(Post o) {
        Log.d(TAG, "compareTo: ////////s/s/s/s/");
        return getTimestamp().compareTo(o.getTimestamp());
    }
}
