package com.example.lifcar.myapplication.Manager;

import android.content.Context;
import android.util.Log;

import com.example.lifcar.myapplication.Model.AnsRequest;
import com.example.lifcar.myapplication.Model.OnMesResponse;
import com.example.lifcar.myapplication.Other.GetQuestionCallback;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerManager {


    private static final String BASE_URL =  "http://grecharsb.pythonanywhere.com";
    private static final String TAG = "SERVER_MANAGER_LOG";

    static Gson gson = new Gson();
    public static Context context;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static void GetQuestion(final GetQuestionCallback getQuestionCallback, AnsRequest req) {
        Log.d(TAG, "Sart GetQuestion");
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL;

        Gson gson = new Gson();
        String json = gson.toJson(req);

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "GetQuestion FAIL: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Log.d(TAG, "onResponse:" + body);

                    try {
                        OnMesResponse onMesResponse = new Gson().fromJson(body, OnMesResponse.class);
                        Log.d(TAG, "onResponse:" + onMesResponse.response.buttons.get(0).title);
                        if (onMesResponse != null) {
                            getQuestionCallback.setOnMesResponse(onMesResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG, "GetQuestion response is NOT success: " + response.toString());
                }
            }
        });
    }


}
