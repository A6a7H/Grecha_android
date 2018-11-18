package com.example.lifcar.myapplication.Controller;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lifcar.myapplication.Manager.ServerManager;
import com.example.lifcar.myapplication.Model.AnsRequest;
import com.example.lifcar.myapplication.Model.CButton;
import com.example.lifcar.myapplication.Model.InnerResponse;
import com.example.lifcar.myapplication.Model.OnMesResponse;
import com.example.lifcar.myapplication.Model.Session;
import com.example.lifcar.myapplication.Other.GetQuestionCallback;
import com.example.lifcar.myapplication.R;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QAFragment extends Fragment{

    public static final int SINGLE_ASNWER_QUESTION = 0;
    public static final int MULTI_ANSWER_QUESTION = 1;
    public static final int MES_ANSWER_QUESTION = 2;
    public static final int RESULT = 3;


    private LinearLayout linearLayout;
    private List<String> list;

    public List<CButton> buttons;
    public String question;
    public int type;

    BottomSheetBehavior bottomSheetBehavior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_single_ans, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        linearLayout = (LinearLayout)getActivity().findViewById(R.id.qall);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        switch (type){
            case SINGLE_ASNWER_QUESTION:{
                for (final CButton item : buttons) {
                    View view  = inflater.inflate(R.layout.item_sheet, linearLayout, false);
                    setItemSheetOnClick(view, item);

                    TextView textView = (TextView)view.findViewById(R.id.textView);
                    textView.setText(item.title);

                    linearLayout.addView(view);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            }
            case MULTI_ANSWER_QUESTION:{
                linearLayout.removeAllViews();

                list = new ArrayList<>();

                for (final CButton item : buttons) {
                    View view  = inflater.inflate(R.layout.item_sheet, linearLayout, false);
                    final TextView textViewBut = view.findViewById(R.id.textView);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(textViewBut.isEnabled()){
                                list.remove(textViewBut.getText().toString());
                                textViewBut.setEnabled(false);
                            } else {
                                list.add(textViewBut.getText().toString());
                                textViewBut.setEnabled(true);
                            }
                        }
                    });

                    TextView textView = (TextView)view.findViewById(R.id.textView);
                    textView.setText(item.title);
                    linearLayout.addView(view);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                View view  = inflater.inflate(R.layout.button_next_sheet, linearLayout, false);
                final TextView nextButton = view.findViewById(R.id.nextButton);
                setNextOnClick(nextButton);
                linearLayout.addView(view);
                break;
            }
            case MES_ANSWER_QUESTION:{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            }
            case RESULT:{
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            }
            default:
                break;
        }
    }

    private void setItemSheetOnClick(View view, final CButton item){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick:" + item.title);
                ServerManager.GetQuestion(new GetQuestionCallback() {
                    @Override
                    public void setOnMesResponse(final OnMesResponse onMesResponse) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                linearLayout.removeAllViews();
                                changeContent(onMesResponse.response, bottomSheetBehavior);
                            }
                        });
                    }
                }, genRequest(item.title));
            }
        });
    }

    private void setNextOnClick(TextView nextButton){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RRRRRRRR", "onClick: " + list.size());
                if (list.size() != 0){
                    String command = null;
                    for (String item: list){
                        command += item;
                    }



                    ServerManager.GetQuestion(new GetQuestionCallback() {
                        @Override
                        public void setOnMesResponse(final OnMesResponse onMesResponse) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    linearLayout.removeAllViews();
                                    changeContent(onMesResponse.response, bottomSheetBehavior);
                                }
                            });
                        }
                    }, genRequest(command));
                }
            }
        });
    }

    private AnsRequest genRequest(String command){
        AnsRequest ansRequest = new AnsRequest();
        AnsRequest.Request request = new AnsRequest.Request();
        Session session = new Session();
        request.command = command;
        session.session_id = String.valueOf(ThreadLocalRandom.current().nextInt(10000, 30000));
        session.isNew = false;
        ansRequest.session = session;
        ansRequest.request = request;
        return ansRequest;
    }

    private void changeContent(InnerResponse response, BottomSheetBehavior bottomSheetBehavior){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        QAFragment qaFragment = new QAFragment();
        qaFragment.bottomSheetBehavior = bottomSheetBehavior;
        qaFragment.type = 1;
        qaFragment.buttons = response.buttons;
        qaFragment.question = response.title;
        transaction.replace(R.id.qa_fragment, qaFragment);
        transaction.commit();
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return  MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
//    }

    @Override
    public void onPause() {
        super.onPause();
        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.qall);
        linearLayout.removeAllViews();
    }
}
