package com.jayk22.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Text
{
    private String text;
    private int likes;
    private int comments;
    private Timestamp timeadded;
    private String textId;
    private boolean userliked;

    public Text() {
    }

    public Text(String text, int likes, int comments, Timestamp timestamp,String textId,boolean userliked) {
        this.text = text;
        this.likes = likes;
        this.comments = comments;
        this.timeadded = timestamp;
        this.textId=textId;
        this.userliked=userliked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Timestamp getTimestamp() {
        return timeadded;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timeadded = timestamp;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }


    public boolean isUserliked() {
        return userliked;
    }

    public void setUserliked(boolean userliked) {
        this.userliked = userliked;
    }
}
