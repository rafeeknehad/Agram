package com.example.myapplicationinst.model;

import java.util.ArrayList;
import java.util.List;

public class Post {

    private String userId;
    private List<UriString> imageList;
    private String date;
    private String time;
    private String description;
    private List<String> likeList;
    private List<String> commentList;

    public Post() {
    }

    public Post(String userId, List<UriString> uriStrings, String date, String time, String description) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.description = description;
        likeList = new ArrayList<>();
        commentList = new ArrayList<>();
        imageList = uriStrings;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UriString> getImageViewPagers() {
        return imageList;
    }

    public void setImageViewPagers(List<UriString> imageViewPagers) {
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
}
