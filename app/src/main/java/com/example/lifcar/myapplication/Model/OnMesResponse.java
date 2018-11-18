package com.example.lifcar.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnMesResponse {

    @SerializedName("response")
    public InnerResponse response;

    @SerializedName("session")
    public Session session;

}
