package com.example.myapplicationinst.util;

import java.util.List;

public class RequestAndLikeAndComment {

    private List<String> request;
    private List<String> like;
    private List<String> comment;

    public RequestAndLikeAndComment() {
    }

    public List<String> getRequest() {
        return request;
    }

    public void setRequest(List<String> request) {
        this.request = request;
    }

    public List<String> getLike() {
        return like;
    }

    public void setLike(List<String> like) {
        this.like = like;
    }

    public List<String> getComment() {
        return comment;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }
}
