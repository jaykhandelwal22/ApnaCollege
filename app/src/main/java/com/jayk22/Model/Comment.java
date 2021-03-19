package com.jayk22.Model;

import com.google.firebase.Timestamp;

public class Comment {

    private String text;
    private Timestamp timestamp;


    public Comment() {
    }

    public Comment(String text, Timestamp timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
