package com.jayk22.Model;

import com.google.firebase.Timestamp;

public class Image
{

    private String imageUrl;
    private String title;
   // private String comment;
    private Timestamp timestamp;
    private String imageid;
    private int likes;
    private int comments;
    private boolean userliked;

    public Image() {
    }

    public Image(String imageUrl, String title,Timestamp timestamp, int likes, int comments,boolean userliked) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.timestamp = timestamp;
        this.likes = likes;
        this.comments = comments;
        this.userliked=userliked;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public boolean isUserliked() {
        return userliked;
    }

    public void setUserliked(boolean userliked) {
        this.userliked = userliked;
    }
}
