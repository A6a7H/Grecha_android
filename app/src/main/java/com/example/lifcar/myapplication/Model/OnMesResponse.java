package com.example.lifcar.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnMesResponse {

    private static class Response{
        @SerializedName("title")
        public String title;

        @SerializedName("buttons")
        public List<CButton> buttons;
    }

    @SerializedName("response")
    public Response response;

    @SerializedName("session")
    public Session session;

}
