package com.example.lifcar.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InnerResponse{
    @SerializedName("text")
    public String title;

    @SerializedName("type")
    public int type;

    @SerializedName("buttons")
    public List<CButton> buttons;
}
