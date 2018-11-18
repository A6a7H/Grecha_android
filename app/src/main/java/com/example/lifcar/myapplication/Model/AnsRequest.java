package com.example.lifcar.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class AnsRequest {

    public static class Request{

        @SerializedName("command")
        public String command;

        @SerializedName("type")
        public String type;

//        @SerializedName("payload")
//        public String payload;
    }

    @SerializedName("request")
    public Request request;

    @SerializedName("session")
    public Session session;
}
