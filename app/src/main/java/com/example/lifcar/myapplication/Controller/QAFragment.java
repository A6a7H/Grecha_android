package com.example.lifcar.myapplication.Controller;

import android.os.Bundle;
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

import com.example.lifcar.myapplication.R;
import com.labo.kaji.fragmentanimations.MoveAnimation;

public class QAFragment extends Fragment{

    public int a;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_single_ans, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        String[] list;

        if (a == 1){
            list = new String[]{"l", "k"};
        } else {
            list = new String[]{"a", "b", "c"};
        }

        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.qall);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final String item : list) {
            View view  = inflater.inflate(R.layout.test_relative, linearLayout, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "onClick:" + item);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    QAFragment n = new QAFragment();
                    if(a == 1){
                        n.a = 2;
                    } else {
                        n.a = 1;
                    }
                    transaction.replace(R.id.qa_fragment, n);
                    transaction.commit();
                }
            });

            TextView textView = (TextView)view.findViewById(R.id.textView);
            textView.setText(item);
            // set item content in view
            linearLayout.addView(view);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return  MoveAnimation.create(MoveAnimation.LEFT, enter, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.qall);
        linearLayout.removeAllViews();
    }
}
