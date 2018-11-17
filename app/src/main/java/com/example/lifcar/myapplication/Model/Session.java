package com.example.lifcar.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("new")
    public boolean isNew;

    @SerializedName("message_id")
    public int message_id;

    @SerializedName("session_id")
    public String session_id;

    @SerializedName("skill_id")
    public String skill_id;

    @SerializedName("user_id")
    public String user_id;
}
