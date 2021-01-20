package com.example.myapplicationinst.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String userName;
    private String userPassword;
    private String userGender;
    private String userProfile;
    private String userAbout;
    private List<String> follewers;
    private List<String> followings;
    private List<String> posts;
    private String userKey;
    private List<String> waitList;
    private List<String> recieveList;
    private List<String> searchingList;

    public User() {
    }

    public User(String userName, String userPassword, String userGender, String userProfile,
                String userAbout) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userProfile = userProfile;
        this.userAbout = userAbout;
        this.follewers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.posts = new ArrayList<>();
        waitList = new ArrayList<>();
        recieveList = new ArrayList<>();
        searchingList = new ArrayList<>();
    }

    public List<String> getSearchingList() {
        return searchingList;
    }

    public void setSearchingList(List<String> searchingList) {
        this.searchingList = searchingList;
    }

    public List<String> getRecieveList() {
        return recieveList;
    }

    public void setRecieveList(List<String> recieveList) {
        this.recieveList = recieveList;
    }

    @Exclude
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }

    public List<String> getFollewers() {
        return follewers;
    }

    public void setFollewers(List<String> follewers) {
        this.follewers = follewers;
    }

    public List<String> getFollowings() {
        return followings;
    }

    public void setFollowings(List<String> followings) {
        this.followings = followings;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public List<String> getWaitList() {
        return waitList;
    }

    public void setWaitList(List<String> waitList) {
        this.waitList = waitList;
    }
}
