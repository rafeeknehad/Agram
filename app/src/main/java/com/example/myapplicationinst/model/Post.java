package com.example.myapplicationinst.model;

import com.example.myapplicationinst.util.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String userId;
    private List<ImageViewPager> imageViewPagers;
    private String date;
    private String time;
    private String description;
    private List<String> likeList;
    private List<String> commentList;

    public Post() {
    }

    public Post(String userId, List<ImageViewPager> imageViewPagers, String date, String time, String description) {
        this.userId = userId;
        this.imageViewPagers = imageViewPagers;
        this.date = date;
        this.time = time;
        this.description = description;
        likeList = new ArrayList<>();
        commentList = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ImageViewPager> getImageViewPagers() {
        return imageViewPagers;
    }

    public void setImageViewPagers(List<ImageViewPager> imageViewPagers) {
        this.imageViewPagers = imageViewPagers;
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
