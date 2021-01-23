package com.example.myapplicationinst.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class Comment {
    @ServerTimestamp
    private Date timestamp;
    private String postId;
    private String userId;
    private String commentDescription;
    private List<String> userFav;

    public Comment(String postId, String userId, String commentDescription, List<String> userFav) {
        this.postId = postId;
        this.userId = userId;
        this.commentDescription = commentDescription;
        this.userFav = userFav;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public List<String> getUserFav() {
        return userFav;
    }

    public void setUserFav(List<String> userFav) {
        this.userFav = userFav;
    }
}
