package com.example.lifcar.myapplication.Controller;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dagang.library.GradientButton;
import com.example.lifcar.myapplication.Manager.ServerManager;
import com.example.lifcar.myapplication.Model.AnsRequest;
import com.example.lifcar.myapplication.Model.InnerResponse;
import com.example.lifcar.myapplication.Model.OnMesResponse;
import com.example.lifcar.myapplication.Model.Session;
import com.example.lifcar.myapplication.Other.GetQuestionCallback;
import com.example.lifcar.myapplication.R;
import com.google.gson.Gson;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private GradientButton chooseTarif;
    private final String TAG = "MAIN_LOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // получение вью нижнего экрана
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);


        // настройка поведения нижнего экрана
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // настройка состояний нижнего экрана

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
//                    setFragment(null);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(Double.isNaN(slideOffset)){
                    slideOffset = 0;
                }
                loadingPanel.setAlpha((1+slideOffset)/2);
                Log.d(TAG, "onSlide: "+ slideOffset);

//                setFragment(null);
            }
        });

        chooseTarif = findViewById(R.id.chooseTarif);
        chooseTarif.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ServerManager.GetQuestion(new GetQuestionCallback() {
                    @Override
                    public void setOnMesResponse(OnMesResponse onMesResponse) {
                        setFragment(onMesResponse.response, bottomSheetBehavior);
                    }
                }, genRequest(""));

                Log.d(TAG, "onClick: work!");
            }
        });
    }

    private AnsRequest genRequest(String command){
        AnsRequest ansRequest = new AnsRequest();
        AnsRequest.Request request = new AnsRequest.Request();
        Session session = new Session();
        request.command = command;
        session.session_id = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 30000));
        session.isNew = true;
        ansRequest.session = session;
        ansRequest.request = request;
        return ansRequest;
    }

    private void setFragment(InnerResponse response, BottomSheetBehavior bottomSheetBehavior){
        QAFragment qaFragment = new QAFragment();
        qaFragment.bottomSheetBehavior = bottomSheetBehavior;
        if(response != null){
            qaFragment.type = 0;
            qaFragment.buttons = response.buttons;
            qaFragment.question = response.title;
        } else qaFragment.type = 0;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.qa_fragment, qaFragment);
        transaction.commit();
    }
}
