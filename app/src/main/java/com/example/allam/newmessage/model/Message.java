package com.example.allam.newmessage.model;

import java.io.Serializable;


public class Message implements Serializable {
    private String mUser;
    private String mBody;
    private String mDate;

    public Message(String user, String body, String date) {
        this.mUser = user;
        this.mBody = body;
        this.mDate = date;
    }

    public String getUser() {
        return mUser;
    }

    public String getBody() {
        return mBody;
    }

    public String getDate() {
        return mDate;
    }
}
