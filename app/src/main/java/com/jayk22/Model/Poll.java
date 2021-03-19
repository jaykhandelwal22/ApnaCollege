package com.jayk22.Model;

import com.google.firebase.Timestamp;

public class Poll {

    private String question;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private Timestamp timestamp;
    private int likes;
    private int comments;
    private int op1v;
    private int op2v;
    private int op3v;
    private int op4v;
    private String pollid;
    private boolean seen;
    private boolean userliked;

    public Poll() {
    }

    public Poll(String question, String op1, String op2, String op3, String op4, Timestamp timestamp, int likes, int comments, int op1v, int op2v, int op3v, int op4v,String pollid,boolean seen,boolean userliked) {
        this.question = question;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.timestamp = timestamp;
        this.likes = likes;
        this.comments = comments;
        this.op1v = op1v;
        this.op2v = op2v;
        this.op3v = op3v;
        this.op4v = op4v;
        this.pollid=pollid;
        this.seen=seen;
        this.userliked=userliked;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
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

    public int getOp1v() {
        return op1v;
    }

    public void setOp1v(int op1v) {
        this.op1v = op1v;
    }

    public int getOp2v() {
        return op2v;
    }

    public void setOp2v(int op2v) {
        this.op2v = op2v;
    }

    public int getOp3v() {
        return op3v;
    }

    public void setOp3v(int op3v) {
        this.op3v = op3v;
    }

    public int getOp4v() {
        return op4v;
    }

    public void setOp4v(int op4v) {
        this.op4v = op4v;
    }

    public String getPollid() {
        return pollid;
    }

    public void setPollid(String pollid) {
        this.pollid = pollid;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isUserliked() {
        return userliked;
    }

    public void setUserliked(boolean userliked) {
        this.userliked = userliked;
    }
}
