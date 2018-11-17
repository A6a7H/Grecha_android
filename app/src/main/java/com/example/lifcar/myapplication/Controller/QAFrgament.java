package com.example.lifcar.myapplication.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lifcar.myapplication.R;

public class QAFrgament extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_single_ans, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        String[] list = {"l", "k"};

        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.qall);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final String item : list) {
            View view  = inflater.inflate(R.layout.test_relative, linearLayout, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "onClick:" + item);
                }
            });

            TextView textView = (TextView)view.findViewById(R.id.textView);
            textView.setText(item);
            // set item content in view
            linearLayout.addView(view);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.qall);
        linearLayout.removeAllViews();
    }
}
