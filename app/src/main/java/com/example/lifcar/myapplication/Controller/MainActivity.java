package com.example.lifcar.myapplication.Controller;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dagang.library.GradientButton;
import com.example.lifcar.myapplication.Manager.ServerManager;
import com.example.lifcar.myapplication.Model.OnMesResponse;
import com.example.lifcar.myapplication.R;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private GradientButton chooseTarif;
    private final String TAG = "MAIN_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String body =
                "{\n" +
                "                \"response\": {\n" +
                "            \"text\": \"Здравствуйте! Это мы, хороводоведы.\",\n" +
                "                    \"tts\": \"Здравствуйте! Это мы, хоров+одо в+еды.\",\n" +
                "                    \"buttons\": [\n" +
                "            {\n" +
                "                \"title\": \"Надпись на кнопке\",\n" +
                "                    \"payload\": {},\n" +
                "                \"url\": \"https://example.com/\",\n" +
                "                    \"hide\": true\n" +
                "            }\n" +
                "    ],\n" +
                "            \"end_session\": false\n" +
                "        },\n" +
                "        \"session\": {\n" +
                "            \"session_id\": \"2eac4854-fce721f3-b845abba-20d60\",\n" +
                "                    \"message_id\": 4,\n" +
                "                    \"user_id\": \"AC9WC3DF6FCE052E45A4566A48E6B7193774B84814CE49A922E163B8B29881DC\"\n" +
                "        },\n" +
                "        \"version\": \"1.0\"\n" +
                "}";

        try {
            OnMesResponse onMesResponse = new Gson().fromJson(body, OnMesResponse.class);
            if (onMesResponse != null) {
                Log.d(TAG, "onCreate:" + onMesResponse.session.session_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // получение вью нижнего экрана
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);


        setFragment();


        // настройка поведения нижнего экрана
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // настройка состояний нижнего экрана
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // настройка возможности скрыть элемент при свайпе вниз
        bottomSheetBehavior.setHideable(true);

        final RelativeLayout loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);
        loadingPanel.setBackgroundColor(Color.parseColor("#000000"));
        loadingPanel.setAlpha(0);

        // настройка колбэков при изменениях
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    loadingPanel.setAlpha(0);
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.qall);
                    linearLayout.removeAllViews();
                    setFragment();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(Double.isNaN(slideOffset)){
                    slideOffset = 0;
                }
                loadingPanel.setAlpha((1+slideOffset)/2);
                Log.d(TAG, "onSlide: "+ slideOffset);
            }
        });

        chooseTarif = findViewById(R.id.chooseTarif);
        chooseTarif.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ServerManager.GetQuestion("");

                Log.d(TAG, "onClick: work!");

                setFragment();

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setFragment(){
        QAFragment qaFragment = new QAFragment();
        qaFragment.a = 1;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.qa_fragment, qaFragment);
        transaction.commit();
    }
}
